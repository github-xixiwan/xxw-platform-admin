package com.xxw.platform.plugin.system.business.resource.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源表
 *
 * @author liaoxiting
 * @date 2020/11/23 22:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_resource")
public class SysResource extends BaseEntity {

    /**
     * 资源id
     */
    @TableId("resource_id")
    @ChineseDescription("资源id")
    private Long resourceId;

    /**
     * 应用编码
     */
    @TableField("app_code")
    @ChineseDescription("应用编码")
    private String appCode;

    /**
     * 资源编码
     */
    @TableField("resource_code")
    @ChineseDescription("资源编码")
    private String resourceCode;

    /**
     * 资源名称
     */
    @TableField("resource_name")
    @ChineseDescription("资源名称")
    private String resourceName;

    /**
     * 项目编码
     */
    @TableField("project_code")
    @ChineseDescription("项目编码")
    private String projectCode;

    /**
     * 类名称
     */
    @TableField("class_name")
    @ChineseDescription("类名称")
    private String className;

    /**
     * 方法名称
     */
    @TableField("method_name")
    @ChineseDescription("方法名称")
    private String methodName;

    /**
     * 资源模块编码
     */
    @TableField("modular_code")
    @ChineseDescription("资源模块编码")
    private String modularCode;

    /**
     * 资源模块名称
     */
    @TableField("modular_name")
    @ChineseDescription("资源模块名称")
    private String modularName;

    /**
     * 资源初始化的服务器ip地址
     */
    @TableField("ip_address")
    @ChineseDescription("资源初始化的服务器ip地址")
    private String ipAddress;

    /**
     * 是否是视图类型：Y-是，N-否
     * 如果是视图类型，url需要以 '/view' 开头，
     * 视图类型的接口会渲染出html界面，而不是json数据，
     * 视图层一般会在前后端不分离项目出现
     */
    @TableField("view_flag")
    @ChineseDescription("是否是视图类型")
    private String viewFlag;

    /**
     * 资源url
     */
    @TableField("url")
    @ChineseDescription("资源url")
    private String url;

    /**
     * http请求方法
     */
    @TableField("http_method")
    @ChineseDescription("http请求方法")
    private String httpMethod;

    /**
     * 资源的业务类型：1-业务类型，2-系统类型
     */
    @TableField("resource_biz_type")
    @ChineseDescription("资源的业务类型：1-业务类型，2-系统类型")
    private Integer resourceBizType;

    /**
     * 是否需要登录：Y-是，N-否
     */
    @TableField("required_login_flag")
    @ChineseDescription("是否需要登录：Y-是，N-否")
    private String requiredLoginFlag;

    /**
     * 是否需要鉴权：Y-是，N-否
     */
    @TableField("required_permission_flag")
    @ChineseDescription("是否需要鉴权：Y-是，N-否")
    private String requiredPermissionFlag;

    /**
     * 需要进行参数校验的分组
     * <p>
     * json形式存储
     */
    @TableField("validate_groups")
    @ChineseDescription("需要进行参数校验的分组")
    private String validateGroups;

    /**
     * 接口参数的字段描述
     * <p>
     * json形式存储
     */
    @TableField("param_field_descriptions")
    @ChineseDescription("接口参数的字段描述")
    private String paramFieldDescriptions;

    /**
     * 接口返回结果的字段描述
     * <p>
     * json形式存储
     */
    @TableField("response_field_descriptions")
    @ChineseDescription("接口返回结果的字段描述")
    private String responseFieldDescriptions;

    /**
     * 应用名称
     */
    private transient String appName;

}
