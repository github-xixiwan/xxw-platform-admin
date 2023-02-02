package com.xxw.platform.plugin.file.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.util.HttpServletUtil;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.file.api.constants.FileConstants;
import com.xxw.platform.plugin.file.api.expander.FileConfigExpander;
import com.xxw.platform.plugin.file.business.entity.SysFileStorage;
import com.xxw.platform.plugin.file.business.mapper.SysFileStorageMapper;
import com.xxw.platform.plugin.file.business.service.SysFileStorageService;
import org.springframework.stereotype.Service;

/**
 * 文件存储信息业务实现层
 *
 * @author fengshuonan
 * @date 2022/01/08 15:53
 */
@Service
public class SysFileStorageServiceImpl extends ServiceImpl<SysFileStorageMapper, SysFileStorage> implements SysFileStorageService {

    @Override
    public void saveFile(Long fileId, byte[] bytes) {
        SysFileStorage sysFileStorage = new SysFileStorage();
        sysFileStorage.setFileId(fileId);
        sysFileStorage.setFileBytes(bytes);
        this.save(sysFileStorage);
    }

    @Override
    public String getFileAuthUrl(String fileId) {
        // 获取登录用户的token
        String token = LoginContext.me().getToken();
        // 获取context-path
        String contextPath = HttpServletUtil.getRequest().getContextPath();
        return FileConfigExpander.getServerDeployHost() + contextPath + FileConstants.FILE_PRIVATE_PREVIEW_URL + "?fileId=" + fileId + "&token=" + token;
    }

    @Override
    public String getFileUnAuthUrl(String fileId) {
        // 获取context-path
        String contextPath = HttpServletUtil.getRequest().getContextPath();
        return FileConfigExpander.getServerDeployHost() + contextPath + FileConstants.FILE_PUBLIC_PREVIEW_URL + "?fileId=" + fileId;
    }

}