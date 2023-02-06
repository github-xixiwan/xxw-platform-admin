package com.xxw.platform.plugin.file.api;

import com.xxw.platform.plugin.file.api.pojo.AntdvFileInfo;
import com.xxw.platform.plugin.file.api.pojo.request.SysFileInfoRequest;
import com.xxw.platform.plugin.file.api.pojo.response.SysFileInfoResponse;

import java.io.File;

/**
 * 获取文件信息的api
 *
 * @author liaoxiting
 * @date 2020/11/29 16:21
 */
public interface FileInfoApi {

    /**
     * 获取文件详情
     *
     * @param fileId 文件id，在文件信息表的id
     * @return 文件的信息，不包含文件本身的字节信息
     * @author liaoxiting
     * @date 2020/11/29 16:26
     */
    SysFileInfoResponse getFileInfoWithoutContent(Long fileId);

    /**
     * 获取文件的下载地址（带鉴权的），生成外网地址
     *
     * @param fileId 文件id
     * @return 外部系统可以直接访问的url
     * @author liaoxiting
     * @date 2020/10/26 10:40
     */
    String getFileAuthUrl(Long fileId);

    /**
     * 获取文件的下载地址（带鉴权的），生成外网地址
     *
     * @param fileId 文件id
     * @param token  用户的token
     * @return 外部系统可以直接访问的url
     * @author liaoxiting
     * @date 2020/10/26 10:40
     */
    String getFileAuthUrl(Long fileId, String token);

    /**
     * 获取文件的下载地址（不带鉴权的），生成外网地址
     *
     * @param fileId 文件id
     * @return 外部系统可以直接访问的url
     * @author liaoxiting
     * @date 2020/10/26 10:40
     */
    String getFileUnAuthUrl(Long fileId);

    /**
     * 获取AntdV组件格式对应的文件信息封装
     *
     * @author liaoxiting
     * @date 2022/3/28 14:32
     */
    AntdvFileInfo buildAntdvFileInfo(Long fileId);

    /**
     * 真实删除文件
     *
     * @author liaoxiting
     * @date 2022/7/22 23:19
     */
    void removeFile(Long fileId);

    /**
     * 上传到服务器文件，并保存文件信息到库中
     *
     * @param file               原始
     * @param sysFileInfoRequest 文件附属信息（需要2个参数：是否是机密文件、bucket信息）
     * @return 返回文件id等信息
     * @author liaoxiting
     * @date 2022/10/19 18:24
     */
    SysFileInfoResponse uploadFileAndSave(File file, SysFileInfoRequest sysFileInfoRequest);

}
