package com.xxw.platform.plugin.validator.unique.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.constants.TenantConstants;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.db.api.DbOperatorApi;
import com.xxw.platform.plugin.db.api.context.DbOperatorContext;
import com.xxw.platform.plugin.validator.api.exception.ParamValidateException;
import com.xxw.platform.plugin.validator.api.exception.enums.ValidatorExceptionEnum;
import com.xxw.platform.plugin.validator.api.pojo.UniqueValidateParam;

/**
 * 判断表中字段是否是唯一值的业务封装
 *
 * @author liaoxiting
 * @date 2020/11/4 15:06
 */
public class TableUniqueValueService {

    /**
     * 判断表中某个字段是否已经存在该值
     *
     * @author liaoxiting
     * @date 2020/11/4 15:08
     */
    public static boolean getFiledUniqueFlag(UniqueValidateParam uniqueValidateParam) {
        try {
            return doValidate(uniqueValidateParam);
        } catch (Exception exception) {
            throw new ParamValidateException(ValidatorExceptionEnum.UNIQUE_VALIDATE_SQL_ERROR, exception.getMessage());
        }
    }

    private static boolean doValidate(UniqueValidateParam uniqueValidateParam) {
        DbOperatorApi dbOperatorApi = DbOperatorContext.me();

        int resultCount = 0;

        // 参数校验
        paramValidate(uniqueValidateParam);

        // 不排除当前记录，不排除逻辑删除的内容
        if (!uniqueValidateParam.getExcludeCurrentRecord()
                && !uniqueValidateParam.getExcludeLogicDeleteItems()) {
            String sqlTemplate = "select count(*) from {} where {} = {0}";
            String finalSql = StrUtil.format(sqlTemplate, calcTenantTableName(uniqueValidateParam.getTableName()), uniqueValidateParam.getColumnName());
            resultCount = dbOperatorApi.selectCount(finalSql, uniqueValidateParam.getValue());
        }

        // 不排除当前记录，排除逻辑删除的内容
        if (!uniqueValidateParam.getExcludeCurrentRecord()
                && uniqueValidateParam.getExcludeLogicDeleteItems()) {
            String sqlTemplate = "select count(*) from {} where {} = {0}  and ({} is null or {} <> '{}')";
            String finalSql = StrUtil.format(sqlTemplate,
                    calcTenantTableName(uniqueValidateParam.getTableName()),
                    uniqueValidateParam.getColumnName(),
                    uniqueValidateParam.getLogicDeleteFieldName(),
                    uniqueValidateParam.getLogicDeleteFieldName(),
                    uniqueValidateParam.getLogicDeleteValue());
            resultCount = dbOperatorApi.selectCount(finalSql, uniqueValidateParam.getValue());
        }

        // 排除当前记录，不排除逻辑删除的内容
        if (uniqueValidateParam.getExcludeCurrentRecord()
                && !uniqueValidateParam.getExcludeLogicDeleteItems()) {

            // id判空
            paramIdValidate(uniqueValidateParam);

            String sqlTemplate = "select count(*) from {} where {} = {0} and {} <> {1}";
            String finalSql = StrUtil.format(sqlTemplate, calcTenantTableName(uniqueValidateParam.getTableName()), uniqueValidateParam.getColumnName(), uniqueValidateParam.getIdFieldName());
            resultCount = dbOperatorApi.selectCount(finalSql, uniqueValidateParam.getValue(), uniqueValidateParam.getId());
        }

        // 排除当前记录，排除逻辑删除的内容
        if (uniqueValidateParam.getExcludeCurrentRecord()
                && uniqueValidateParam.getExcludeLogicDeleteItems()) {

            // id判空
            paramIdValidate(uniqueValidateParam);

            String sqlTemplate = "select count(*) from {} where {} = {0} and {} <> {1} and ({} is null or {} <> '{}')";
            String finalSql = StrUtil.format(sqlTemplate,
                    calcTenantTableName(uniqueValidateParam.getTableName()),
                    uniqueValidateParam.getColumnName(),
                    uniqueValidateParam.getIdFieldName(),
                    uniqueValidateParam.getLogicDeleteFieldName(),
                    uniqueValidateParam.getLogicDeleteFieldName(),
                    uniqueValidateParam.getLogicDeleteValue());
            resultCount = dbOperatorApi.selectCount(finalSql, uniqueValidateParam.getValue(), uniqueValidateParam.getId());
        }

        // 如果大于0，代表不是唯一的当前校验的值
        return resultCount <= 0;
    }
    /**
     * 几个参数的为空校验
     *
     * @author liaoxiting
     * @date 2020/11/4 15:11
     */
    private static void paramValidate(UniqueValidateParam uniqueValidateParam) {
        if (StrUtil.isBlank(uniqueValidateParam.getTableName())) {
            throw new ParamValidateException(ValidatorExceptionEnum.TABLE_UNIQUE_VALIDATE_ERROR, "@TableUniqueValue注解上tableName属性为空");
        }
        if (StrUtil.isBlank(uniqueValidateParam.getColumnName())) {
            throw new ParamValidateException(ValidatorExceptionEnum.TABLE_UNIQUE_VALIDATE_ERROR, "@TableUniqueValue注解上columnName属性为空");
        }
        if (ObjectUtil.isEmpty(uniqueValidateParam.getValue())) {
            throw new ParamValidateException(ValidatorExceptionEnum.TABLE_UNIQUE_VALIDATE_ERROR, "@TableUniqueValue被校验属性的值为空");
        }
    }

    /**
     * id参数的为空校验
     *
     * @author liaoxiting
     * @date 2020/11/4 15:16
     */
    private static void paramIdValidate(UniqueValidateParam uniqueValidateParam) {
        if (uniqueValidateParam.getId() == null) {
            throw new ParamValidateException(ValidatorExceptionEnum.TABLE_UNIQUE_VALIDATE_ERROR, StrUtil.toCamelCase(uniqueValidateParam.getIdFieldName()) + "参数值为空");
        }
    }

    /**
     * 计算携带租户code情况下的表名称
     *
     * @author liaoxiting
     * @date 2021/9/24 17:01
     */
    private static String calcTenantTableName(String originTableName) {

        // 获取租户编码
        LoginUser loginUser = LoginContext.me().getLoginUser();
        String tenantCode = loginUser.getTenantCode();

        // 如果是主数据源可以忽略
        if (StrUtil.isBlank(tenantCode) || TenantConstants.MASTER_DATASOURCE_NAME.equals(tenantCode)) {
            return originTableName;
        }

        return TenantConstants.TENANT_DB_PREFIX + tenantCode + "." + originTableName;
    }

}
