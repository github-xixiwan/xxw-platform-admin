package com.xxw.platform.plugin.system.api.pojo.login.v3;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 最新Antd3版本的首页登录用户信息需要的数据
 *
 * @author liaoxiting
 * @date 2022/4/8 14:56
 */
@Data
public class IndexUserInfoV3 {

    /**
     * 用户主键id
     */
    @ChineseDescription("用户主键id")
    private Long userId;

    /**
     * 账号
     */
    @ChineseDescription("账号")
    private String username;

    /**
     * 姓名
     */
    @ChineseDescription("姓名")
    private String realName;
    /**
     * 昵称
     */
    @ChineseDescription("昵称")
    private String nickname;

    /**
     * 用户头像（url）
     */
    @ChineseDescription("用户头像（url）")
    private String avatar;

    /**
     * 性别（字典）
     */
    @ChineseDescription("性别（字典）")
    private String sex;

    /**
     * 电话
     */
    @ChineseDescription("电话")
    private String phone;

    /**
     * 邮箱
     */
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 出生日期
     */
    @ChineseDescription("出生日期")
    private Date birthday;

    /**
     * 组织机构id
     */
    @ChineseDescription("组织机构id")
    private Long organizationId;

    /**
     * 用户状态
     */
    @ChineseDescription("状态")
    private Integer status;

    /**
     * 性别名称
     */
    @ChineseDescription("性别名称")
    private String sexName;

    /**
     * 组织机构名称
     */
    @ChineseDescription("组织机构名称")
    private String organizationName;

    /**
     * 角色信息
     */
    @ChineseDescription("角色信息")
    private List<IndexRoleInfo> roles;

    /**
     * 权限信息
     */
    @ChineseDescription("权限信息")
    private List<IndexMenuInfo> authorities;

    /**
     * 登录人的ws-url，用来获取右上角的消息
     */
    @ChineseDescription("登录人的ws-url")
    private String wsUrl;

    /**
     * 权限标识编码
     */
    @ChineseDescription("权限标识编码")
    private Set<String> authCodes;

}
