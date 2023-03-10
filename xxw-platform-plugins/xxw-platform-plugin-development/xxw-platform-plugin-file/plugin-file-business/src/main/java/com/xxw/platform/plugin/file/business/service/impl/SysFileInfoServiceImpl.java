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
 * ??????????????? ???????????????
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

        // ????????????????????????
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        SysFileInfo sysFileInfo = this.querySysFileInfo(sysFileInfoRequest);

        // ?????????????????????
        byte[] fileBytes;
        try {
            // ????????????????????????????????????????????????????????????????????????FileOperatorApi
            if (FileLocationEnum.DB.getCode().equals(sysFileInfo.getFileLocation())) {
                SysFileStorage storage = sysFileStorageService.getById(fileId);
                fileBytes = storage.getFileBytes();
            } else {
                fileBytes = fileOperatorApi.getFileBytes(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName());
            }
        } catch (Exception e) {
            log.error("??????????????????????????????????????????{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR, e.getMessage());
        }

        // ?????????????????????
        SysFileInfoResponse sysFileInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, sysFileInfoResult);
        sysFileInfoResult.setFileBytes(fileBytes);

        return sysFileInfoResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileInfoResponse uploadFile(MultipartFile file, SysFileInfoRequest sysFileInfoRequest) {

        // ????????????????????????????????????????????????
        SysFileInfo sysFileInfo = FileInfoFactory.createSysFileInfo(file, sysFileInfoRequest);

        // ??????????????????
        this.save(sysFileInfo);

        // ??????????????????????????????
        try {
            byte[] bytes = file.getBytes();

            // ?????????????????????????????????????????????
            if (FileLocationEnum.DB.getCode().equals(sysFileInfoRequest.getFileLocation())) {
                sysFileStorageService.saveFile(sysFileInfo.getFileId(), bytes);
            } else {
                fileOperatorApi.storageFile(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), bytes);
            }
        } catch (IOException e) {
            throw new FileException(FileExceptionEnum.ERROR_FILE, e.getMessage());
        }

        // ?????????????????????
        SysFileInfoResponse fileUploadInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, fileUploadInfoResult);

        // ??????????????????????????????url
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

        // ?????????code?????????????????????????????????
        LambdaQueryWrapper<SysFileInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFileInfo::getFileCode, fileCode);
        queryWrapper.eq(SysFileInfo::getDelFlag, YesOrNotEnum.N.getCode());
        queryWrapper.eq(SysFileInfo::getFileStatus, FileStatusEnum.NEW.getCode());
        SysFileInfo oldFileInfo = this.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(oldFileInfo)) {
            throw new FileException(FileExceptionEnum.NOT_EXISTED);
        }

        // ????????????????????????
        oldFileInfo.setFileStatus(FileStatusEnum.OLD.getCode());
        this.updateById(oldFileInfo);

        // ?????????????????????
        SysFileInfo newFileInfo = FileInfoFactory.createSysFileInfo(file, sysFileInfoRequest);

        // ??????????????????????????????????????????
        newFileInfo.setFileCode(fileCode);
        newFileInfo.setFileVersion(oldFileInfo.getFileVersion() + 1);

        // ???????????????????????????
        this.save(newFileInfo);

        // ?????????????????????
        SysFileInfoResponse fileUploadInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(newFileInfo, fileUploadInfoResult);
        return fileUploadInfoResult;
    }

    @Override
    public void download(SysFileInfoRequest sysFileInfoRequest, HttpServletResponse response) {

        // ????????????id???????????????????????????
        SysFileInfoResponse sysFileInfoResponse = this.getFileInfoResult(sysFileInfoRequest.getFileId());

        // ????????????????????????????????????????????????????????????
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

        // ?????????Code?????????????????????
        LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysFileInfo::getFileId, sysFileInfoRequest.getFileId());

        if (ObjectUtil.isNotEmpty(sysFileInfoRequest.getFileCode())) {
            wrapper.or().eq(SysFileInfo::getFileCode, sysFileInfoRequest.getFileCode());
        }

        List<SysFileInfo> fileInfos = this.list(wrapper);

        // ????????????
        this.removeByIds(fileInfos.stream().map(SysFileInfo::getFileId).collect(Collectors.toList()));

        // ??????????????????
        for (SysFileInfo fileInfo : fileInfos) {
            //???????????????????????????????????????????????????
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

        // ??????defaultAvatar.png????????????,?????????????????????
        List<SysFileInfoListResponse> newList = list.stream().filter(i -> !i.getFileOriginName().equals(FileConstants.DEFAULT_AVATAR_FILE_OBJ_NAME)).collect(Collectors.toList());

        // ????????????url??????
        for (SysFileInfoListResponse sysFileInfoListResponse : newList) {
            // ????????????????????????????????????
            if (PicFileTypeUtil.getFileImgTypeFlag(sysFileInfoListResponse.getFileSuffix())) {
                sysFileInfoListResponse.setFileUrl(this.getFileAuthUrl(sysFileInfoListResponse.getFileId()));
            }
        }

        return PageResultFactory.createPageResult(page.setRecords(newList));
    }

    @Override
    public void packagingDownload(String fileIds, String secretFlag, HttpServletResponse response) {

        // ??????????????????
        List<Long> fileIdList = Arrays.stream(fileIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<SysFileInfoResponse> fileInfoResponseList = this.getFileInfoListByFileIds(fileIdList);

        // ??????bucket??????
        String bucketName = FileConfigExpander.getDefaultBucket();
        if (ObjectUtil.isNotEmpty(fileInfoResponseList)) {
            bucketName = fileInfoResponseList.get(0).getFileBucket();
        }

        // ??????????????????
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bos);

        try {
            for (int i = 0; i < fileInfoResponseList.size(); i++) {
                SysFileInfoResponse sysFileInfoResponse = fileInfoResponseList.get(i);
                if (ObjectUtil.isNotEmpty(sysFileInfoResponse)) {
                    String fileOriginName = sysFileInfoResponse.getFileOriginName();
                    // ???????????????????????????????????????????????????
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

            // ????????????
            DownloadUtil.download(DateUtil.now() + "-????????????" + FileConstants.FILE_POSTFIX_SEPARATOR + "zip", bos.toByteArray(), response);
        } catch (Exception e) {
            log.error("??????????????????????????????????????????{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR, e.getMessage());
        } finally {
            try {
                zip.closeEntry();
                zip.close();
                bos.close();
            } catch (IOException e) {
                log.error("??????????????????????????????????????????{}", e.getMessage());
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

        // ????????????id???????????????????????????
        SysFileInfoResponse sysFileInfoResponse = this.getFileInfoResult(sysFileInfoRequest.getFileId());

        // ????????????????????????????????????????????????????????????
        if (YesOrNotEnum.Y.getCode().equals(sysFileInfoResponse.getSecretFlag())) {
            if (YesOrNotEnum.N.getCode().equals(sysFileInfoRequest.getSecretFlag())) {
                throw new FileException(FileExceptionEnum.FILE_DENIED_ACCESS);
            }
        }

        // ??????????????????
        String fileSuffix = sysFileInfoResponse.getFileSuffix().toLowerCase();

        // ?????????????????????
        byte[] fileBytes = sysFileInfoResponse.getFileBytes();

        // ????????????
        this.renderPreviewFile(response, fileSuffix, fileBytes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileInfoResponse versionBack(SysFileInfoRequest sysFileInfoRequest) {

        LambdaQueryWrapper<SysFileInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFileInfo::getFileId, sysFileInfoRequest.getFileId());
        SysFileInfo fileInfo = this.getOne(queryWrapper);

        // ???????????????????????????
        if (ObjectUtil.isEmpty(fileInfo)) {
            String userTip = FileExceptionEnum.FILE_NOT_FOUND.getUserTip();
            String errorMessage = StrUtil.format(userTip, "??????:" + fileInfo.getFileId() + "????????????");
            throw new FileException(FileExceptionEnum.FILE_NOT_FOUND, errorMessage);
        }

        // ????????????????????????
        LambdaUpdateWrapper<SysFileInfo> oldFileInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        oldFileInfoLambdaUpdateWrapper.eq(SysFileInfo::getFileCode, fileInfo.getFileCode());
        oldFileInfoLambdaUpdateWrapper.eq(SysFileInfo::getFileStatus, FileStatusEnum.NEW.getCode());
        oldFileInfoLambdaUpdateWrapper.set(SysFileInfo::getFileStatus, FileStatusEnum.OLD.getCode());
        this.update(oldFileInfoLambdaUpdateWrapper);

        // ??????????????????
        LambdaUpdateWrapper<SysFileInfo> newFileInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        newFileInfoLambdaUpdateWrapper.eq(SysFileInfo::getFileId, sysFileInfoRequest.getFileId());
        newFileInfoLambdaUpdateWrapper.set(SysFileInfo::getFileStatus, FileStatusEnum.NEW.getCode());
        newFileInfoLambdaUpdateWrapper.set(SysFileInfo::getDelFlag, YesOrNotEnum.N.getCode());
        this.update(newFileInfoLambdaUpdateWrapper);

        // ??????
        return BeanUtil.toBean(fileInfo, SysFileInfoResponse.class);
    }

    @Override
    public void previewByBucketAndObjName(SysFileInfoRequest sysFileInfoRequest, HttpServletResponse response) {

        if (StrUtil.isNotBlank(sysFileInfoRequest.getFileObjectName())) {
            sysFileInfoRequest.setFileObjectName(StrFilterUtil.filterFileName(sysFileInfoRequest.getFileObjectName()));
        }

        // ?????????????????????????????????????????????????????????token??????
        LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFileInfo::getFileObjectName, sysFileInfoRequest.getFileObjectName());
        wrapper.eq(SysFileInfo::getSecretFlag, YesOrNotEnum.Y.getCode());
        long count = this.count(wrapper);
        if (count > 0) {
            if (!LoginContext.me().hasLogin()) {
                throw new FileException(FileExceptionEnum.FILE_PERMISSION_DENIED);
            }
        }

        // ?????????????????????
        byte[] fileBytes;
        try {
            fileBytes = fileOperatorApi.getFileBytes(sysFileInfoRequest.getFileBucket(), sysFileInfoRequest.getFileObjectName());
        } catch (Exception e) {
            log.error("??????????????????????????????????????????{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR, e.getMessage());
        }

        // ??????????????????
        String fileSuffix = FileUtil.getSuffix(sysFileInfoRequest.getFileObjectName());

        // ????????????
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

        // bean??????
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

        // ???????????????????????????
        SysFileInfo sysFileInfo = querySysFileInfo(sysFileInfoRequest);

        // ????????????
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
        // ???????????????????????????
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        SysFileInfo sysFileInfo = querySysFileInfo(sysFileInfoRequest);

        // ????????????????????????????????????previewUrl
        if (sysFileInfo.getFileLocation().equals(FileLocationEnum.DB.getCode())) {
            return this.sysFileStorageService.getFileAuthUrl(String.valueOf(fileId));
        } else {
            // ???????????????????????????url
            return fileOperatorApi.getFileAuthUrl(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), FileConfigExpander.getDefaultFileTimeoutSeconds());
        }
    }

    @Override
    public String getFileUnAuthUrl(Long fileId) {
        // ???????????????????????????
        SysFileInfoRequest sysFileInfoRequest = new SysFileInfoRequest();
        sysFileInfoRequest.setFileId(fileId);
        SysFileInfo sysFileInfo = querySysFileInfo(sysFileInfoRequest);

        // ????????????????????????????????????previewUrl
        if (sysFileInfo.getFileLocation().equals(FileLocationEnum.DB.getCode())) {
            return this.sysFileStorageService.getFileUnAuthUrl(String.valueOf(fileId));
        } else {
            // ???????????????????????????url
            return fileOperatorApi.getFileUnAuthUrl(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName());
        }
    }

    @Override
    public AntdvFileInfo buildAntdvFileInfo(Long fileId) {
        AntdvFileInfo antdvFileInfo = new AntdvFileInfo();
        // ????????????id
        antdvFileInfo.setUid(IdWorker.getIdStr());
        // ??????????????????
        SysFileInfoResponse fileInfoWithoutContent;
        try {
            fileInfoWithoutContent = this.getFileInfoWithoutContent(fileId);
            antdvFileInfo.setName(fileInfoWithoutContent.getFileOriginName());
        } catch (Exception e) {
            // ??????????????????????????????
        }
        // ??????????????????url
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

        // ??????????????????
        SysFileInfo sysFileInfo = FileInfoFactory.createSysFileInfo(file, sysFileInfoRequest);

        // ?????????????????????
        this.save(sysFileInfo);

        // ????????????????????????
        try {
            this.fileOperatorApi.storageFile(sysFileInfo.getFileBucket(), sysFileInfo.getFileObjectName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.warn("???????????????storage?????????!", e);
        }

        // ????????????
        SysFileInfoResponse fileUploadInfoResult = new SysFileInfoResponse();
        BeanUtil.copyProperties(sysFileInfo, fileUploadInfoResult);

        return fileUploadInfoResult;
    }

    /**
     * ???????????????????????????servlet???response??????
     *
     * @author liaoxiting
     * @date 2020/11/29 17:13
     */
    private void renderPreviewFile(HttpServletResponse response, String fileSuffix, byte[] fileBytes) {

        // ?????????????????????????????????pdf?????????????????????
        if (PicFileTypeUtil.getFileImgTypeFlag(fileSuffix) || PdfFileTypeUtil.getFilePdfTypeFlag(fileSuffix)) {
            try {
                // ??????contentType
                if (PicFileTypeUtil.getFileImgTypeFlag(fileSuffix)) {
                    response.setContentType(MediaType.IMAGE_PNG_VALUE);
                } else if (PdfFileTypeUtil.getFilePdfTypeFlag(fileSuffix)) {
                    response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                }

                // ??????outputStream
                ServletOutputStream outputStream = response.getOutputStream();

                // ???????????????
                IoUtil.write(outputStream, true, fileBytes);
            } catch (IOException e) {
                throw new FileException(FileExceptionEnum.WRITE_BYTES_ERROR, e.getMessage());
            }
        } else {
            // ???????????????????????????
            throw new FileException(FileExceptionEnum.PREVIEW_ERROR_NOT_SUPPORT);
        }
    }

    /**
     * ?????????????????????
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
