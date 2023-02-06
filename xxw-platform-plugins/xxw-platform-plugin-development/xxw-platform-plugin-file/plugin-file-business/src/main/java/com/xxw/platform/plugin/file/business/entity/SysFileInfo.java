package com.xxw.platform.plugin.file.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author liaoxiting
 * @date 2020/6/7 22:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_file_info")
public class SysFileInfo extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "file_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long fileId;

    /**
     * 文件编码,本号升级的依据
     * <p>
     * 解决一个文件多个版本问题，多次上传文件编码不变
     */
    @TableField("file_code")
    @ChineseDescription("文件编码")
    private Long fileCode;

    /**
     * 文件版本，从1开始
     */
    @TableField("file_version")
    @ChineseDescription("文件版本")
    private Integer fileVersion;

    /**
     * 当前状态：0-历史版，1-最新版
     */
    @TableField("file_status")
    @ChineseDescription("当前状态：0-历史版，1-最新版")
    private String fileStatus;

    /**
     * 文件存储位置：1-阿里云，2-腾讯云，3-minio，4-本地
     */
    @TableField("file_location")
    @ChineseDescription("文件存储位置")
    private Integer fileLocation;

    /**
     * 文件仓库（文件夹）
     */
    @TableField("file_bucket")
    @ChineseDescription("文件仓库(文件夹)")
    private String fileBucket;

    /**
     * 文件名称（上传时候的文件全名）
     */
    @TableField("file_origin_name")
    @ChineseDescription("文件名称（上传时候的文件全名）")
    private String fileOriginName;

    /**
     * 文件后缀，例如.txt
     */
    @TableField("file_suffix")
    @ChineseDescription("文件后缀")
    private String fileSuffix;

    /**
     * 文件大小kb为单位
     */
    @TableField("file_size_kb")
    @ChineseDescription("文件大小")
    private Long fileSizeKb;

    /**
     * 文件大小信息，计算后的
     */
    @TableField("file_size_info")
    @ChineseDescription("文件大小信息，计算后的")
    private String fileSizeInfo;

    /**
     * 存储到bucket中的名称，主键id+.后缀
     */
    @TableField("file_object_name")
    @ChineseDescription("存储到bucket中的名称，主键id+.后缀")
    private String fileObjectName;

    /**
     * 存储路径
     */
    @TableField("file_path")
    @ChineseDescription("存储路径")
    private String filePath;

    /**
     * 是否为机密文件
     */
    @TableField("secret_flag")
    @ChineseDescription("是否为机密文件")
    private String secretFlag;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("是否删除")
    private String delFlag;

}
