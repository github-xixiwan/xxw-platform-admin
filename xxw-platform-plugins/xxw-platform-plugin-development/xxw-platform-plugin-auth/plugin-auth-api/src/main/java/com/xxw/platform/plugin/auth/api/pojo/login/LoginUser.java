package com.xxw.platform.plugin.auth.api.pojo.login;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.plugin.auth.api.enums.DataScopeTypeEnum;
import com.xxw.platform.plugin.auth.api.pojo.login.basic.SimpleRoleInfo;
import com.xxw.platform.plugin.auth.api.pojo.login.basic.SimpleUserInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 登录用户信息
 *
 * @author liaoxiting
 * @date 2020/10/17 9:58
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键id
     */
    @ChineseDescription("用户主键id")
    private Long userId;

    /**
     * 账号
     */
    @ChineseDescription("账号")
    private String account;

    /**
     * 超级管理员标识，true-是超级管理员
     */
    @ChineseDescription("超级管理员标识，true-是超级管理员")
    private Boolean superAdmin;

    /**
     * 用户基本信息
     */
    @ChineseDescription("用户基本信息")
    private SimpleUserInfo simpleUserInfo;

    /**
     * 用户角色信息
     */
    @ChineseDescription("用户角色信息")
    private List<SimpleRoleInfo> simpleRoleInfoList;

    /**
     * 公司/组织id
     */
    @ChineseDescription("公司/组织id")
    private Long organizationId;

    /**
     * 职务信息
     */
    @ChineseDescription("职务信息")
    private Long positionId;

    /**
     * 用户数据范围枚举
     */
    @ChineseDescription("用户数据范围枚举")
    private Set<DataScopeTypeEnum> dataScopeTypeEnums;

    /**
     * 用户数据范围用户信息
     */
    @ChineseDescription("用户数据范围用户信息")
    private Set<Long> dataScopeUserIds;

    /**
     * 用户数据范围组织信息
     */
    @ChineseDescription("用户数据范围组织信息")
    private Set<Long> dataScopeOrganizationIds;

    /**
     * 可用资源集合
     */
    @ChineseDescription("可用资源集合")
    private Set<String> resourceUrls;

    /**
     * 用户拥有的按钮编码集合
     */
    @ChineseDescription("用户拥有的按钮编码集合")
    private Set<String> buttonCodes;

    /**
     * 登录的时间
     */
    @ChineseDescription("登录的时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    /**
     * 用户的token，当返回用户会话信息时候回带token
     */
    @ChineseDescription("用户的token，当返回用户会话信息时候回带token")
    private String token;

    /**
     * 其他信息，Dict为Map的拓展
     */
    @ChineseDescription("其他信息，Dict为Map的拓展")
    private Dict otherInfos;

    /**
     * 用户的ws-url
     */
    @ChineseDescription("用户的ws-url")
    private String wsUrl;

    /**
     * 头像url
     */
    @ChineseDescription("用户头像url")
    private String avatarUrl;

    /**
     * 当前用户语种的标识，例如：chinese，english
     * <p>
     * 这个值是根据字典获取，字典类型编码 languages
     * <p>
     * 默认语种是中文
     */
    @ChineseDescription("当前用户语种的标识")
    private String tranLanguageCode = RuleConstants.CHINESE_TRAN_LANGUAGE_CODE;

    /**
     * 租户的编码
     */
    @ChineseDescription("租户的编码")
    private String tenantCode;

    /**
     * 当前登录用户是否是C端用户（默认不是C端用户）
     */
    @ChineseDescription("是否是C端用户")
    private Boolean customerFlag = false;

    /**
     * 用户拥有的菜单类型：1-前台 ，2-后台，3-所有
     */
    @ChineseDescription("用户拥有的菜单类型：1-前台 ，2-后台，3-所有")
    private Integer menuType;

    public String getWsUrl() {
        if (ObjectUtil.isEmpty(this.wsUrl)) {
            return "";
        }

        Map<String, String> params = new HashMap<>(1);
        params.put("token", this.token);
        return StrUtil.format(this.wsUrl, params);
    }

}
