package com.xxw.platform.plugin.migration.api.exception;

import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.migration.api.constants.MigrationConstants;

/**
 * migration模块异常
 *
 * @author liaoxiting
 * @date 2021/7/6 15:07
 */
public class MigrationException extends ServiceException {

    public MigrationException(AbstractExceptionEnum exception) {
        super(MigrationConstants.MIGRATION_MODULE_NAME, exception);
    }

    public MigrationException(String errorCode, String userTip) {
        super(MigrationConstants.MIGRATION_MODULE_NAME, errorCode, userTip);
    }

}
