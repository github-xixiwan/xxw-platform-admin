package com.xxw.platform.plugin.system.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.constants.RuleConstants;
import com.xxw.platform.frame.common.constants.TenantConstants;
import com.xxw.platform.frame.common.format.BaseSimpleFieldFormatProcess;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.ds.api.context.CurrentDataSourceContext;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;

/**
 * Json响应的针对用户的处理
 *
 * @author liaoxiting
 * @date 2022/9/7 10:09
 */
public class UserFormatProcess extends BaseSimpleFieldFormatProcess {

    /**
     * 未知人员的名称
     */
    private static final String NOT_FIND_USER_NAME = "未知人员";

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {

        LoginUser loginUserNullable = LoginContext.me().getLoginUserNullable();
        if (loginUserNullable == null) {
            return execute(businessId);
        }

        // 如果当前登录用户有租户标识
        try {
            String tenantCode = loginUserNullable.getTenantCode();
            if (StrUtil.isNotEmpty(tenantCode) && !TenantConstants.MASTER_DATASOURCE_NAME.equals(tenantCode)) {
                CurrentDataSourceContext.setDataSourceName(RuleConstants.TENANT_DB_PREFIX + tenantCode);
            }
            return execute(businessId);
        } finally {
            // 清除数据源信息
            CurrentDataSourceContext.clearDataSourceName();
        }
    }

    /**
     * 业务逻辑执行
     *
     * @author liaoxiting
     * @date 2022/11/10 1:29
     */
    private Object execute(Object businessId) {
        Long userId = Convert.toLong(businessId);
        UserServiceApi bean = SpringUtil.getBean(UserServiceApi.class);
        SysUserDTO userInfoByUserId = bean.getUserInfoByUserId(userId);
        if (userInfoByUserId == null) {
            return NOT_FIND_USER_NAME;
        }
        return userInfoByUserId.getRealName();
    }

}
