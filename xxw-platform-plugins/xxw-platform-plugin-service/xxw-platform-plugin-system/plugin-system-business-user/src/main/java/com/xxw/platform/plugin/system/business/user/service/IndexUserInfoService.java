package com.xxw.platform.plugin.system.business.user.service;

import com.xxw.platform.plugin.system.api.pojo.login.v3.IndexUserInfoV3;

/**
 * 获取首页用户登录信息
 *
 * @author liaoxiting
 * @date 2022/4/8 15:40
 */
public interface IndexUserInfoService {

    /**
     * 获取用户信息（新版Antdv3版本）
     *
     * @param menuFrontType 菜单类型：前台菜单还是后台菜单
     * @param devopsFlag    是否查询包含devops平台的菜单（可为空）
     * @return 用户信息和菜单信息
     * @author liaoxiting
     * @date 2022/4/8 15:31
     */
    IndexUserInfoV3 userInfoV3(Integer menuFrontType, Boolean devopsFlag);

}
