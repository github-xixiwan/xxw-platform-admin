package com.xxw.platform.plugin.system.business.user.factory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.enums.SexEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.auth.api.password.PasswordStoredEncryptApi;
import com.xxw.platform.plugin.system.api.enums.UserStatusEnum;
import com.xxw.platform.plugin.system.api.expander.SystemConfigExpander;
import com.xxw.platform.plugin.system.api.pojo.user.OAuth2AuthUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.business.user.entity.SysUser;

/**
 * 用户信息填充，用于创建和修改用户时，添加一些基础信息
 *
 * @author liaoxiting
 * @date 2020/11/21 12:55
 */
public class SysUserCreateFactory {

    /**
     * 新增用户时候的用户信息填充
     *
     * @author liaoxiting
     * @date 2020/11/21 12:56
     */
    public static void fillAddSysUser(SysUser sysUser) {

        // 默认设置为非超级管理员
        sysUser.setSuperAdminFlag(YesOrNotEnum.N.getCode());

        // 添加用户时，设置为启用状态
        sysUser.setStatusFlag(UserStatusEnum.ENABLE.getCode());

        // 密码为空则设置为默认密码
        PasswordStoredEncryptApi passwordStoredEncryptApi = SpringUtil.getBean(PasswordStoredEncryptApi.class);
        if (ObjectUtil.isEmpty(sysUser.getPassword())) {
            String defaultPassword = SystemConfigExpander.getDefaultPassWord();
            sysUser.setPassword(passwordStoredEncryptApi.encrypt(defaultPassword));
        } else {
            // 密码不为空，则将密码加密存储到库中
            sysUser.setPassword(passwordStoredEncryptApi.encrypt(sysUser.getPassword()));
        }

        // 用户头像为空
        if (ObjectUtil.isEmpty(sysUser.getAvatar())) {
            sysUser.setAvatar(null);
        }

        // 用户性别为空，则默认设置成男
        if (ObjectUtil.isEmpty(sysUser.getSex())) {
            sysUser.setSex(SexEnum.M.getCode());
        }
    }

    /**
     * 编辑用户时候的用户信息填充
     *
     * @author liaoxiting
     * @date 2020/11/21 12:56
     */
    public static void fillEditSysUser(SysUser sysUser) {

        // 编辑用户不修改用户状态
        sysUser.setStatusFlag(null);

        // 不能修改原密码，通过重置密码或修改密码来修改
        sysUser.setPassword(null);

    }

    /**
     * 编辑用户时候的用户信息填充
     *
     * @author liaoxiting
     * @date 2020/11/21 12:56
     */
    public static void fillUpdateInfo(SysUserRequest sysUserRequest, SysUser sysUser) {

        // 性别（M-男，F-女）
        sysUser.setSex(sysUserRequest.getSex());

        // 邮箱
        sysUser.setEmail(sysUserRequest.getEmail());

        // 姓名
        sysUser.setRealName(sysUserRequest.getRealName());

        // 生日
        sysUser.setBirthday(DateUtil.parse(sysUserRequest.getBirthday()));

        // 手机
        sysUser.setPhone(sysUserRequest.getPhone());
    }

    /**
     * 创建第三方应用在本应用的用户
     *
     * @author liaoxiting
     * @Date 2019/6/9 19:11
     */
    public static SysUser createOAuth2User(OAuth2AuthUserDTO oAuth2AuthUserDTO) {

        SysUser systemUser = new SysUser();

        // 设置名字
        systemUser.setRealName(oAuth2AuthUserDTO.getNickname());
        systemUser.setNickName(oAuth2AuthUserDTO.getNickname());

        // 设置账号
        systemUser.setAccount("OAUTH2_" + oAuth2AuthUserDTO.getSource() + "_" + oAuth2AuthUserDTO.getUsername());

        // 设置密码
        PasswordStoredEncryptApi passwordStoredEncryptApi = SpringUtil.getBean(PasswordStoredEncryptApi.class);
        systemUser.setPassword(passwordStoredEncryptApi.encrypt(RandomUtil.randomString(20)));

        // 设置性别
        systemUser.setSex(oAuth2AuthUserDTO.getSexEnum().getCode());

        // 设置邮箱电话
        systemUser.setEmail(oAuth2AuthUserDTO.getEmail());
        systemUser.setPhone("未设置");

        return systemUser;
    }

}
