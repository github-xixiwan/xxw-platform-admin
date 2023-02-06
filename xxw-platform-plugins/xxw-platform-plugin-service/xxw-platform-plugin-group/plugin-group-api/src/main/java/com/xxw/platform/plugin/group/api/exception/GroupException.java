package com.xxw.platform.plugin.group.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.group.api.constants.GroupConstants;

/**
 * 业务分组异常
 *
 * @author liaoxiting
 * @date 2022-06-24 17:15:41
 */
public class GroupException extends ServiceException {

    public GroupException(AbstractExceptionEnum exception, Object... params) {
        super(GroupConstants.GROUP_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public GroupException(AbstractExceptionEnum exception) {
        super(GroupConstants.GROUP_MODULE_NAME, exception);
    }

}
