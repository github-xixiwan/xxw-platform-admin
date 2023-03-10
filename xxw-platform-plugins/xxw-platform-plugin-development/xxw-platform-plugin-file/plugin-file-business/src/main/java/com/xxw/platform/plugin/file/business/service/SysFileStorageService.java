package com.xxw.platform.plugin.file.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.file.business.entity.SysFileStorage;

/**
 * 文件存储信息 服务类
 *
 * @author liaoxiting
 * @date 2022/01/08 15:53
 */
public interface SysFileStorageService extends IService<SysFileStorage> {

    /**
     * 将文件存储在库中
     *
     * @author liaoxiting
     * @date 2022/1/8 16:08
     */
    void saveFile(Long fileId, byte[] bytes);

    /**
     * 获取文件的访问url
     *
     * @param fileId 文件id
     * @author liaoxiting
     * @date 2022/1/8 16:12
     */
    String getFileAuthUrl(String fileId);

    /**
     * 获取文件不带鉴权的访问url
     *
     * @param fileId 文件id
     * @author liaoxiting
     * @date 2022/1/8 16:12
     */
    String getFileUnAuthUrl(String fileId);

}