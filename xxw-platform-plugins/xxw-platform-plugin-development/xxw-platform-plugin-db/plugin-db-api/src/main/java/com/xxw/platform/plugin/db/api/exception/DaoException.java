package com.xxw.platform.plugin.db.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.db.api.constants.DbConstants;

/**
 * 数据库操作异常
 *
 * @author liaoxiting
 * @date 2020/10/15 15:59
 */
public class DaoException extends ServiceException {

    public DaoException(AbstractExceptionEnum exception) {
        super(DbConstants.DB_MODULE_NAME, exception);
    }

    public DaoException(AbstractExceptionEnum exception, Object... params) {
        super(DbConstants.DB_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

}
