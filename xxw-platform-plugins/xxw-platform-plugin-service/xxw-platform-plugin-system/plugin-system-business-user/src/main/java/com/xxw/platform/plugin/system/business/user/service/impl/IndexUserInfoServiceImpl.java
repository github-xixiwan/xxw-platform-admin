package com.xxw.platform.plugin.system.business.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xxw.platform.frame.common.enums.SexEnum;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.auth.api.pojo.login.basic.SimpleRoleInfo;
import com.xxw.platform.plugin.system.api.MenuServiceApi;
import com.xxw.platform.plugin.system.api.enums.AntdvFrontTypeEnum;
import com.xxw.platform.plugin.system.api.pojo.login.v3.IndexRoleInfo;
import com.xxw.platform.plugin.system.api.pojo.login.v3.IndexUserInfoV3;
import com.xxw.platform.plugin.system.business.user.service.IndexUserInfoService;
import com.xxw.platform.plugin.system.business.user.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取首页用户登录信息
 *
 * @author liaoxiting
 * @date 2022/4/8 15:40
 */
@Service
public class IndexUserInfoServiceImpl implements IndexUserInfoService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private MenuServiceApi menuServiceApi;

    @Override
    public IndexUserInfoV3 userInfoV3(Integer menuFrontType, Boolean devopsFlag) {

        // 获取当前登录用户
        LoginUser loginUser = LoginContext.me().getLoginUser();

        IndexUserInfoV3 indexUserInfoV3 = new IndexUserInfoV3();

        // 设置用户id
        indexUserInfoV3.setUserId(loginUser.getUserId());

        // 设置用户姓名
        indexUserInfoV3.setRealName(loginUser.getSimpleUserInfo().getRealName());

        // 设置用户账号
        indexUserInfoV3.setUsername(loginUser.getAccount());

        // 设置昵称
        indexUserInfoV3.setNickname(loginUser.getSimpleUserInfo().getNickName());

        // 设置头像，并获取头像的url
        Long avatarFileId = loginUser.getSimpleUserInfo().getAvatar();
        String userAvatarUrl = sysUserService.getUserAvatarUrl(avatarFileId, loginUser.getToken());
        indexUserInfoV3.setAvatar(userAvatarUrl);

        // 设置性别
        indexUserInfoV3.setSex(loginUser.getSimpleUserInfo().getSex());

        // 设置电话
        indexUserInfoV3.setPhone(loginUser.getSimpleUserInfo().getPhone());

        // 设置邮箱
        indexUserInfoV3.setEmail(loginUser.getSimpleUserInfo().getEmail());

        // 设置出生日期
        indexUserInfoV3.setBirthday(loginUser.getSimpleUserInfo().getBirthday());

        // 设置组织机构id
        indexUserInfoV3.setOrganizationId(loginUser.getOrganizationId());

        // 设置状态（暂时不设置）
        indexUserInfoV3.setStatus(null);

        // 设置性别名称
        indexUserInfoV3.setSexName(SexEnum.codeToMessage(indexUserInfoV3.getSex()));

        // 设置组织机构名称，暂时不设置，用到再设置
        indexUserInfoV3.setOrganizationName(null);

        // 设置角色
        indexUserInfoV3.setRoles(buildRoles(loginUser));

        // 获取用户菜单和权限信息
        if (ObjectUtil.isEmpty(menuFrontType)) {
            menuFrontType = AntdvFrontTypeEnum.FRONT.getCode();
        }
        indexUserInfoV3.setAuthorities(menuServiceApi.buildAuthorities(menuFrontType, devopsFlag));

        // 登录人的ws-url
        indexUserInfoV3.setWsUrl(loginUser.getWsUrl());

        // 权限编码
        indexUserInfoV3.setAuthCodes(loginUser.getButtonCodes());

        return indexUserInfoV3;
    }

    /**
     * 获取用户的角色信息
     *
     * @author liaoxiting
     * @date 2022/4/8 15:53
     */
    private List<IndexRoleInfo> buildRoles(LoginUser loginUser) {

        List<SimpleRoleInfo> simpleRoleInfoList = loginUser.getSimpleRoleInfoList();

        ArrayList<IndexRoleInfo> indexRoleInfos = new ArrayList<>();
        if (simpleRoleInfoList.size() > 0) {
            for (SimpleRoleInfo simpleRoleInfo : simpleRoleInfoList) {
                IndexRoleInfo indexRoleInfo = new IndexRoleInfo();
                BeanUtil.copyProperties(simpleRoleInfo, indexRoleInfo);
                indexRoleInfos.add(indexRoleInfo);
            }
        }

        return indexRoleInfos;
    }

}
