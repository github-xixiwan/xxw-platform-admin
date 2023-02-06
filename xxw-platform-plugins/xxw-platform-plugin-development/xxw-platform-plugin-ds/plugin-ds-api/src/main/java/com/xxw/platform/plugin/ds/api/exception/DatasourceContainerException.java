package com.xxw.platform.plugin.ds.api.exception;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.exception.AbstractExceptionEnum;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.ds.api.constants.DatasourceContainerConstants;

/**
 * 数据源容器操作异常
 *
 * @author liaoxiting
 * @date 2020/10/31 22:10
 */
public class DatasourceContainerException extends ServiceException {

    public DatasourceContainerException(AbstractExceptionEnum exception, Object... params) {
        super(DatasourceContainerConstants.DS_CTN_MODULE_NAME, exception.getErrorCode(), StrUtil.format(exception.getUserTip(), params));
    }

    public DatasourceContainerException(AbstractExceptionEnum exception) {
        super(DatasourceContainerConstants.DS_CTN_MODULE_NAME, exception);
    }

}
