package com.xxw.platform.plugin.file.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.frame.common.util.StrFilterUtil;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.file.api.FileInfoApi;
import com.xxw.platform.plugin.file.api.FileOperatorApi;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import com.xxw.platform.plugin.file.api.enums.FileLocationEnum;
import com.xxw.platform.plugin.file.api.enums.FileStatusEnum;
import com.xxw.platform.plugin.file.api.exception.FileException;
import com.xxw.platform.plugin.file.api.exception.enums.FileExceptionEnum;
import com.xxw.platform.plugin.file.api.expander.FileConfigExpander;
import com.xxw.platform.plugin.file.api.pojo.AntdvFileInfo;
import com.xxw.platform.plugin.file.api.pojo.request.SysFileInfoRequest;
import com.xxw.platform.plugin.file.api.pojo.response.SysFileInfoListResponse;
import com.xxw.platform.plugin.file.api.pojo.response.SysFileInfoResponse;
import com.xxw.platform.plugin.file.api.util.DownloadUtil;
import com.xxw.platform.plugin.file.api.util.PdfFileTypeUtil;
import com.xxw.platform.plugin.file.api.util.PicFileTypeUtil;
import com.xxw.platform.plugin.file.business.entity.SysFileInfo;
import com.xxw.platform.plugin.file.business.entity.SysFileStorage;
import com.xxw.platform.plugin.file.business.factory.FileInfoFactory;
import com.xxw.platform.plugin.file.business.mapper.SysFileInfoMapper;
import com.xxw.platform.plugin.file.business.service.SysFileInfoService;
import com.xxw.platform.plugin.file.business.service.SysFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件信息表 服务实现类
 *
 * @author liaoxiting
 * @date 2020/6/7 22:15
 */
@Service
@Slf4j
public class SysFileInfoServiceImpl extends ServiceImpl<SysFileInfoMapper, SysFileInfo> implements SysFileInfoService, FileInfoApi {

    @Resource
    private FileOperatorApi fileOperatorApi;

    @Resource
    private SysFileStorageService sysFileStorageService;

    @Override
    public SysFileInfoResponse getFileInfoResult(Long fileId) {

        // 查询库中文件信息
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        SysFileInfo sysFileInfo = this.querySysFileInfo(sysFileInfoRequest);

        // 获取文件字节码
        byte[] fileBytes;
        try {
            // 如果是存储在数据库，从数据库中获取，其他的方式走FileOperatorApi
            if (FileLocationEnum.DB.getCode().equals(sysFileInfo.getFileLocation())) {
                SysFileStorage storage = sysFileStorageService.getById(fileId);
                fileBytes = storage.getFileBytes();
            } else {
                fileBytes = fileOperatorApi.getFileBytes(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName());
            }
        } catch (Exception e) {
            log.error("获取文件流异常，具体信息为：{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR, e.getMessage());
        }

        // 设置文件字节码
        SysFileInfoResponse sysFileInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, sysFileInfoResult);
        sysFileInfoResult.setFileBytes(fileBytes);

        return sysFileInfoResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileInfoResponse uploadFile(MultipartFile file, SysFileInfoRequest sysFileInfoRequest) {

        // 文件请求转换存入数据库的附件信息
        SysFileInfo sysFileInfo = FileInfoFactory.createSysFileInfo(file, sysFileInfoRequest);

        // 保存文件信息
        this.save(sysFileInfo);

        // 存储文件到云或者本地
        try {
            byte[] bytes = file.getBytes();

            // 如果是存在数据库库里，单独处理
            if (FileLocationEnum.DB.getCode().equals(sysFileInfoRequest.getFileLocation())) {
                sysFileStorageService.saveFile(sysFileInfo.getFileId(), bytes);
            } else {
                fileOperatorApi.storageFile(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), bytes);
            }
        } catch (IOException e) {
            throw new FileException(FileExceptionEnum.ERROR_FILE, e.getMessage());
        }

        // 返回文件信息体
        SysFileInfoResponse fileUploadInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, fileUploadInfoResult);

        // 拼接文件可直接访问的url
        String fileAuthUrl;
        if (YesOrNotEnum.Y.getCode().equals(sysFileInfoRequest.getSecretFlag())) {
            fileAuthUrl = fileOperatorApi.getFileAuthUrl(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), FileConfigExpander.getDefaultFileTimeoutSeconds() * 1000);
        } else {
            fileAuthUrl = fileOperatorApi.getFileUnAuthUrl(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName());
        }
        fileUploadInfoResult.setFileUrl(fileAuthUrl);

        return fileUploadInfoResult;
    }

    @Override
    public SysFileInfoResponse updateFile(MultipartFile file, SysFileInfoRequest sysFileInfoRequest) {

        Long fileCode = sysFileInfoRequest.getFileCode();

        // 查询该code下的最新版本号附件信息
        LambdaQueryWrapper<SysFileInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFileInfo::getFileCode, fileCode);
        queryWrapper.eq(SysFileInfo::getDelFlag, YesOrNotEnum.N.getCode());
        queryWrapper.eq(SysFileInfo::getFileStatus, FileStatusEnum.NEW.getCode());
        SysFileInfo oldFileInfo = this.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(oldFileInfo)) {
            throw new FileException(FileExceptionEnum.NOT_EXISTED);
        }

        // 旧文件状态的改变
        oldFileInfo.setFileStatus(FileStatusEnum.OLD.getCode());
        this.updateById(oldFileInfo);

        // 创建新文件信息
        SysFileInfo newFileInfo = FileInfoFactory.createSysFileInfo(file, sysFileInfoRequest);

        // 设置版本号在原本的基础上加一
        newFileInfo.setFileCode(fileCode);
        newFileInfo.setFileVersion(oldFileInfo.getFileVersion() + 1);

        // 存储新版本文件信息
        this.save(newFileInfo);

        // 返回文件信息体
        SysFileInfoResponse fileUploadInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(newFileInfo, fileUploadInfoResult);
        return fileUploadInfoResult;
    }

    @Override
    public void download(SysFileInfoRequest sysFileInfoRequest, HttpServletResponse response) {

        // 根据文件id获取文件信息结果集
        SysFileInfoResponse sysFileInfoResponse = this.getFileInfoResult(sysFileInfoRequest.getFileId());

        // 如果文件加密等级不符合，文件不允许被访问
        if (YesOrNotEnum.Y.getCode().equals(sysFileInfoResponse.getSecretFlag())) {
            if (YesOrNotEnum.N.getCode().equals(sysFileInfoRequest.getSecretFlag())) {
                throw new FileException(FileExceptionEnum.FILE_DENIED_ACCESS);
            }
        }

        DownloadUtil.download(sysFileInfoResponse.getFileOriginName(), sysFileInfoResponse.getFileBytes(), response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteReally(SysFileInfoRequest sysFileInfoRequest) {

        // 查询该Code的所有历史版本
        LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysFileInfo::getFileId, sysFileInfoRequest.getFileId());

        if (ObjectUtil.isNotEmpty(sysFileInfoRequest.getFileCode())) {
            wrapper.or().eq(SysFileInfo::getFileCode, sysFileInfoRequest.getFileCode());
        }

        List<SysFileInfo> fileInfos = this.list(wrapper);

        // 批量删除
        this.removeByIds(fileInfos.stream().map(SysFileInfo::getFileId).collect(Collectors.toList()));

        // 删除具体文件
        for (SysFileInfo fileInfo : fileInfos) {
            //如果文件是在数据库存储，则特殊处理
            if (fileInfo.getFileLocation().equals(FileLocationEnum.DB.getCode())) {
                this.sysFileStorageService.removeById(fileInfo.getFileId());
            } else {
                this.fileOperatorApi.deleteFile(fileInfo.getFileBucket(), fileInfo.getFileObjectName());
            }
        }
    }

    @Override
    public PageResult<SysFileInfoListResponse> fileInfoListPage(SysFileInfoRequest sysFileInfoRequest) {
        Page<SysFileInfoListResponse> page = PageFactory.defaultPage();
        List<SysFileInfoListResponse> list = this.baseMapper.fileInfoList(page, sysFileInfoRequest);

        // 排除defaultAvatar.png这个图片,这个是默认头像
        List<SysFileInfoListResponse> newList = list.stream().filter(i -> !i.getFileOriginName().equals(FileConstants.DEFAULT_AVATAR_FILE_OBJ_NAME)).collect(Collectors.toList());

        // 拼接图片url地址
        for (SysFileInfoListResponse sysFileInfoListResponse : newList) {
            // 判断是否是可以预览的文件
            if (PicFileTypeUtil.getFileImgTypeFlag(sysFileInfoListResponse.getFileSuffix())) {
                sysFileInfoListResponse.setFileUrl(this.getFileAuthUrl(sysFileInfoListResponse.getFileId()));
            }
        }

        return PageResultFactory.createPageResult(page.setRecords(newList));
    }

    @Override
    public void packagingDownload(String fileIds, String secretFlag, HttpServletResponse response) {

        // 获取文件信息
        List<Long> fileIdList = Arrays.stream(fileIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<SysFileInfoResponse> fileInfoResponseList = this.getFileInfoListByFileIds(fileIdList);

        // 获取bucket名称
        String bucketName = FileConfigExpander.getDefaultBucket();
        if (ObjectUtil.isNotEmpty(fileInfoResponseList)) {
            bucketName = fileInfoResponseList.get(0).getFileBucket();
        }

        // 输出流等信息
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bos);

        try {
            for (int i = 0; i < fileInfoResponseList.size(); i++) {
                SysFileInfoResponse sysFileInfoResponse = fileInfoResponseList.get(i);
                if (ObjectUtil.isNotEmpty(sysFileInfoResponse)) {
                    String fileOriginName = sysFileInfoResponse.getFileOriginName();
                    // 判断公有文件下载时是否包含私有文件
                    if (secretFlag.equals(YesOrNotEnum.N.getCode()) && !secretFlag.equals(sysFileInfoResponse.getSecretFlag())) {
                        throw new FileException(FileExceptionEnum.SECRET_FLAG_INFO_ERROR, fileOriginName);
                    }

                    byte[] fileBytes = fileOperatorApi.getFileBytes(bucketName, sysFileInfoResponse.getFileObjectName());
                    ZipEntry entry = new ZipEntry(i + 1 + "." + fileOriginName);
                    entry.setSize(fileBytes.length);
                    zip.putNextEntry(entry);
                    zip.write(fileBytes);
                }
            }
            zip.finish();

            // 下载文件
            DownloadUtil.download(DateUtil.now() + "-打包下载" + FileConstants.FILE_POSTFIX_SEPARATOR + "zip", bos.toByteArray(), response);
        } catch (Exception e) {
            log.error("获取文件流异常，具体信息为：{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR, e.getMessage());
        } finally {
            try {
                zip.closeEntry();
                zip.close();
                bos.close();
            } catch (IOException e) {
                log.error("关闭数据流失败，具体信息为：{}", e.getMessage());
            }
        }
    }

    @Override
    public List<SysFileInfoResponse> getFileInfoListByFileIds(String fileIds) {
        List<Long> fileIdList = Arrays.stream(fileIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return this.getFileInfoListByFileIds(fileIdList);
    }

    @Override
    public void preview(SysFileInfoRequest sysFileInfoRequest, HttpServletResponse response) {

        // 根据文件id获取文件信息结果集
        SysFileInfoResponse sysFileInfoResponse = this.getFileInfoResult(sysFileInfoRequest.getFileId());

        // 如果文件加密等级不符合，文件不允许被访问
        if (YesOrNotEnum.Y.getCode().equals(sysFileInfoResponse.getSecretFlag())) {
            if (YesOrNotEnum.N.getCode().equals(sysFileInfoRequest.getSecretFlag())) {
                throw new FileException(FileExceptionEnum.FILE_DENIED_ACCESS);
            }
        }

        // 获取文件后缀
        String fileSuffix = sysFileInfoResponse.getFileSuffix().toLowerCase();

        // 获取文件字节码
        byte[] fileBytes = sysFileInfoResponse.getFileBytes();

        // 附件预览
        this.renderPreviewFile(response, fileSuffix, fileBytes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileInfoResponse versionBack(SysFileInfoRequest sysFileInfoRequest) {

        LambdaQueryWrapper<SysFileInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFileInfo::getFileId, sysFileInfoRequest.getFileId());
        SysFileInfo fileInfo = this.getOne(queryWrapper);

        // 判断有没有这个文件
        if (ObjectUtil.isEmpty(fileInfo)) {
            String userTip = FileExceptionEnum.FILE_NOT_FOUND.getUserTip();
            String errorMessage = StrUtil.format(userTip, "文件:" + fileInfo.getFileId() + "未找到！");
            throw new FileException(FileExceptionEnum.FILE_NOT_FOUND, errorMessage);
        }

        // 把之前的文件刷回
        LambdaUpdateWrapper<SysFileInfo> oldFileInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        oldFileInfoLambdaUpdateWrapper.eq(SysFileInfo::getFileCode, fileInfo.getFileCode());
        oldFileInfoLambdaUpdateWrapper.eq(SysFileInfo::getFileStatus, FileStatusEnum.NEW.getCode());
        oldFileInfoLambdaUpdateWrapper.set(SysFileInfo::getFileStatus, FileStatusEnum.OLD.getCode());
        this.update(oldFileInfoLambdaUpdateWrapper);

        // 修改文件状态
        LambdaUpdateWrapper<SysFileInfo> newFileInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        newFileInfoLambdaUpdateWrapper.eq(SysFileInfo::getFileId, sysFileInfoRequest.getFileId());
        newFileInfoLambdaUpdateWrapper.set(SysFileInfo::getFileStatus, FileStatusEnum.NEW.getCode());
        newFileInfoLambdaUpdateWrapper.set(SysFileInfo::getDelFlag, YesOrNotEnum.N.getCode());
        this.update(newFileInfoLambdaUpdateWrapper);

        // 返回
        return BeanUtil.toBean(fileInfo, SysFileInfoResponse.class);
    }

    @Override
    public void previewByBucketAndObjName(SysFileInfoRequest sysFileInfoRequest, HttpServletResponse response) {

        if (StrUtil.isNotBlank(sysFileInfoRequest.getFileObjectName())) {
            sysFileInfoRequest.setFileObjectName(StrFilterUtil.filterFileName(sysFileInfoRequest.getFileObjectName()));
        }

        // 判断文件是否需要鉴权，需要鉴权的需要带token访问
        LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFileInfo::getFileObjectName, sysFileInfoRequest.getFileObjectName());
        wrapper.eq(SysFileInfo::getSecretFlag, YesOrNotEnum.Y.getCode());
        long count = this.count(wrapper);
        if (count > 0) {
            if (!LoginContext.me().hasLogin()) {
                throw new FileException(FileExceptionEnum.FILE_PERMISSION_DENIED);
            }
        }

        // 获取文件字节码
        byte[] fileBytes;
        try {
            fileBytes = fileOperatorApi.getFileBytes(sysFileInfoRequest.getFileBucket(), sysFileInfoRequest.getFileObjectName());
        } catch (Exception e) {
            log.error("获取文件流异常，具体信息为：{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR, e.getMessage());
        }

        // 获取文件后缀
        String fileSuffix = FileUtil.getSuffix(sysFileInfoRequest.getFileObjectName());

        // 附件预览
        this.renderPreviewFile(response, fileSuffix, fileBytes);
    }

    @Override
    public SysFileInfo detail(SysFileInfoRequest sysFileInfoRequest) {
        return this.querySysFileInfo(sysFileInfoRequest);
    }

    @Override
    public List<SysFileInfoResponse> getFileInfoListByFileIds(List<Long> fileIdList) {
        LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysFileInfo::getFileId, fileIdList);
        List<SysFileInfo> list = this.list(wrapper);

        // bean转化
        return list.stream().map(i -> {
            SysFileInfoResponse sysFileInfoResponse = new SysFileInfoResponse();
            BeanUtil.copyProperties(i, sysFileInfoResponse);
            return sysFileInfoResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public SysFileInfoResponse getFileInfoWithoutContent(Long fileId) {

        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);

        // 获取文件的基本信息
        SysFileInfo sysFileInfo = querySysFileInfo(sysFileInfoRequest);

        // 转化实体
        SysFileInfoResponse sysFileInfoResponse = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, sysFileInfoResponse);

        return sysFileInfoResponse;
    }

    @Override
    public String getFileAuthUrl(Long fileId) {
        return this.getFileAuthUrl(fileId, LoginContext.me().getToken());
    }

    @Override
    public String getFileAuthUrl(Long fileId, String token) {
        // 获取文件的基本信息
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        SysFileInfo sysFileInfo = querySysFileInfo(sysFileInfoRequest);

        // 如果是数据库存储，则返回previewUrl
        if (sysFileInfo.getFileLocation().equals(FileLocationEnum.DB.getCode())) {
            return this.sysFileStorageService.getFileAuthUrl(String.valueOf(fileId));
        } else {
            // 返回第三方存储文件url
            return fileOperatorApi.getFileAuthUrl(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), FileConfigExpander.getDefaultFileTimeoutSeconds());
        }
    }

    @Override
    public String getFileUnAuthUrl(Long fileId) {
        // 获取文件的基本信息
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        SysFileInfo sysFileInfo = querySysFileInfo(sysFileInfoRequest);

        // 如果是数据库存储，则返回previewUrl
        if (sysFileInfo.getFileLocation().equals(FileLocationEnum.DB.getCode())) {
            return this.sysFileStorageService.getFileUnAuthUrl(String.valueOf(fileId));
        } else {
            // 返回第三方存储文件url
            return fileOperatorApi.getFileUnAuthUrl(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName());
        }
    }

    @Override
    public AntdvFileInfo buildAntdvFileInfo(Long fileId) {
        AntdvFileInfo antdvFileInfo = new AntdvFileInfo();
        // 设置唯一id
        antdvFileInfo.setUid(IdWorker.getIdStr());
        // 设置文件名称
        SysFileInfoResponse fileInfoWithoutContent;
        try {
            fileInfoWithoutContent = this.getFileInfoWithoutContent(fileId);
            antdvFileInfo.setName(fileInfoWithoutContent.getFileOriginName());
        } catch (Exception e) {
            // 未存在文件信息，忽略
        }
        // 设置文件访问url
        String fileAuthUrl = this.getFileAuthUrl(fileId);
        antdvFileInfo.setThumbUrl(fileAuthUrl);
        return antdvFileInfo;
    }

    @Override
    public void removeFile(Long fileId) {
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        this.deleteReally(sysFileInfoRequest);
    }

    @Override
    public SysFileInfoResponse uploadFileAndSave(File file, SysFileInfoRequest sysFileInfoRequest) {

        // 创建文件信息
        SysFileInfo sysFileInfo = FileInfoFactory.createSysFileInfo(file, sysFileInfoRequest);

        // 保存文件到库中
        this.save(sysFileInfo);

        // 保存文件到存储中
        try {
            this.fileOperatorApi.storageFile(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.warn("保存文件到storage中出错!", e);
        }

        // 转化响应
        SysFileInfoResponse fileUploadInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, fileUploadInfoResult);

        return fileUploadInfoResult;
    }

    /**
     * 渲染被预览的文件到servlet的response流中
     *
     * @author liaoxiting
     * @date 2020/11/29 17:13
     */
    private void renderPreviewFile(HttpServletResponse response, String fileSuffix, byte[] fileBytes) {

        // 如果文件后缀是图片或者pdf，则直接输出流
        if (PicFileTypeUtil.getFileImgTypeFlag(fileSuffix) || PdfFileTypeUtil.getFilePdfTypeFlag(fileSuffix)) {
            try {
                // 设置contentType
                if (PicFileTypeUtil.getFileImgTypeFlag(fileSuffix)) {
                    response.setContentType(MediaType.IMAGE_PNG_VALUE);
                } else if (PdfFileTypeUtil.getFilePdfTypeFlag(fileSuffix)) {
                    response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                }

                // 获取outputStream
                ServletOutputStream outputStream = response.getOutputStream();

                // 输出字节流
                IoUtil.write(outputStream, true, fileBytes);
            } catch (IOException e) {
                throw new FileException(FileExceptionEnum.WRITE_BYTES_ERROR, e.getMessage());
            }
        } else {
            // 不支持别的文件预览
            throw new FileException(FileExceptionEnum.PREVIEW_ERROR_NOT_SUPPORT);
        }
    }

    /**
     * 获取文件信息表
     *
     * @author liaoxiting
     * @date 2020/11/29 13:40
     */
    private SysFileInfo querySysFileInfo(SysFileInfoRequest sysFileInfoRequest) {
        SysFileInfo sysFileInfo = this.getById(sysFileInfoRequest.getFileId());
        if (ObjectUtil.isEmpty(sysFileInfo) || sysFileInfo.getDelFlag().equals(YesOrNotEnum.Y.getCode())) {
            throw new FileException(FileExceptionEnum.NOT_EXISTED, sysFileInfoRequest.getFileId());
        }
        return sysFileInfo;
    }

}
