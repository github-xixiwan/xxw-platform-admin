package com.xxw.platform.plugin.file.business.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xxw.platform.plugin.file.api.FileOperatorApi;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import com.xxw.platform.plugin.file.api.enums.FileLocationEnum;
import com.xxw.platform.plugin.file.api.enums.FileStatusEnum;
import com.xxw.platform.plugin.file.api.expander.FileConfigExpander;
import com.xxw.platform.plugin.file.api.pojo.request.SysFileInfoRequest;
import com.xxw.platform.plugin.file.business.entity.SysFileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;

/**
 * 文件信息组装工厂
 *
 * @author liaoxiting
 * @date 2020/12/30 22:16
 */
public class FileInfoFactory {

    /**
     * 创建文件信息
     *
     * @author liaoxiting
     * @date 2022/10/19 20:19
     */
    public static SysFileInfo createSysFileInfo(MultipartFile file, SysFileInfoRequest sysFileInfoRequest) {

        // 封装存储文件信息（上传替换公共信息）
        SysFileInfo sysFileInfo = new SysFileInfo();

        // 生成文件的唯一id
        Long fileId = IdWorker.getId();
        sysFileInfo.setFileId(fileId);

        // 文件编码生成
        sysFileInfo.setFileCode(IdWorker.getId());

        // 默认版本号从1开始
        sysFileInfo.setFileVersion(1);

        // 文件状态
        sysFileInfo.setFileStatus(FileStatusEnum.NEW.getCode());

        // 如果是存在数据库库里，单独处理一下，如果不是存储到库里，则读取当前fileApi的存储位置
        if (FileLocationEnum.DB.getCode().equals(sysFileInfoRequest.getFileLocation())) {
            sysFileInfo.setFileLocation(FileLocationEnum.DB.getCode());
        } else {
            FileOperatorApi fileOperatorApi = SpringUtil.getBean(FileOperatorApi.class);
            sysFileInfo.setFileLocation(fileOperatorApi.getFileLocationEnum().getCode());
        }

        // 桶名
        String fileBucket = FileConfigExpander.getDefaultBucket();
        if (StrUtil.isNotEmpty(sysFileInfoRequest.getFileBucket())) {
            fileBucket = sysFileInfoRequest.getFileBucket();
        }
        sysFileInfo.setFileBucket(fileBucket);

        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        sysFileInfo.setFileOriginName(originalFilename);

        // 获取文件后缀（不包含点）
        String fileSuffix = null;
        if (ObjectUtil.isNotEmpty(originalFilename)) {
            fileSuffix = StrUtil.subAfter(originalFilename, FileConstants.FILE_POSTFIX_SEPARATOR, true);
        }
        sysFileInfo.setFileSuffix(fileSuffix);

        // 计算文件大小kb
        long fileSizeKb = Convert.toLong(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024)).setScale(0, BigDecimal.ROUND_HALF_UP));
        sysFileInfo.setFileSizeKb(fileSizeKb);

        // 计算文件大小信息
        String fileSizeInfo = FileUtil.readableFileSize(file.getSize());
        sysFileInfo.setFileSizeInfo(fileSizeInfo);

        // 生成文件的最终名称，存储在storage的名称
        String finalFileName = fileId + FileConstants.FILE_POSTFIX_SEPARATOR + fileSuffix;
        sysFileInfo.setFileObjectName(finalFileName);

        // 文件密级
        sysFileInfo.setSecretFlag(sysFileInfoRequest.getSecretFlag());

        return sysFileInfo;
    }

    /**
     * 创建文件存储的基础信息
     *
     * @author liaoxiting
     * @date 2022/10/19 18:37
     */
    public static SysFileInfo createSysFileInfo(File file, SysFileInfoRequest sysFileInfoRequest) {

        // 封装存储文件信息（上传替换公共信息）
        SysFileInfo sysFileInfo = new SysFileInfo();

        // 创建文件id
        sysFileInfo.setFileId(IdWorker.getId());

        // 文件编码生成
        sysFileInfo.setFileCode(IdWorker.getId());

        // 默认版本号从1开始
        sysFileInfo.setFileVersion(1);

        // 文件状态
        sysFileInfo.setFileStatus(FileStatusEnum.NEW.getCode());

        // 如果是存在数据库库里，单独处理一下，如果不是存储到库里，则读取当前fileApi的存储位置
        if (FileLocationEnum.DB.getCode().equals(sysFileInfoRequest.getFileLocation())) {
            sysFileInfo.setFileLocation(FileLocationEnum.DB.getCode());
        } else {
            FileOperatorApi fileOperatorApi = SpringUtil.getBean(FileOperatorApi.class);
            sysFileInfo.setFileLocation(fileOperatorApi.getFileLocationEnum().getCode());
        }

        // 文件bucket信息
        String fileBucket = FileConfigExpander.getDefaultBucket();
        if (StrUtil.isNotEmpty(sysFileInfoRequest.getFileBucket())) {
            fileBucket = sysFileInfoRequest.getFileBucket();
        }
        sysFileInfo.setFileBucket(fileBucket);

        // 原始文件名称
        sysFileInfo.setFileOriginName(file.getName());

        // 文件后缀
        String fileSuffix = null;
        if (ObjectUtil.isNotEmpty(sysFileInfo.getFileOriginName())) {
            fileSuffix = StrUtil.subAfter(sysFileInfo.getFileOriginName(), FileConstants.FILE_POSTFIX_SEPARATOR, true);
        }
        sysFileInfo.setFileSuffix(fileSuffix);

        // 文件大小 kb
        long fileSizeKb = Convert.toLong(NumberUtil.div(new BigDecimal(file.length()), BigDecimal.valueOf(1024)).setScale(0, BigDecimal.ROUND_HALF_UP));
        sysFileInfo.setFileSizeKb(fileSizeKb);

        // 计算文件大小信息
        String fileSizeInfo = FileUtil.readableFileSize(file.length());
        sysFileInfo.setFileSizeInfo(fileSizeInfo);

        // 最终存储名称
        String finalFileName = sysFileInfo.getFileId() + FileConstants.FILE_POSTFIX_SEPARATOR + fileSuffix;
        sysFileInfo.setFileObjectName(finalFileName);

        // 是否是机密文件
        sysFileInfo.setSecretFlag(sysFileInfoRequest.getSecretFlag());

        return sysFileInfo;
    }

}
