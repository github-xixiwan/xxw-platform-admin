package com.xxw.platform.plugin.db.init.actuator;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.xxw.platform.frame.common.exception.base.ServiceException;
import com.xxw.platform.plugin.db.api.exception.enums.DbInitEnum;
import com.xxw.platform.plugin.db.api.pojo.db.TableFieldInfo;
import com.xxw.platform.plugin.db.api.pojo.db.TableInfo;
import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import com.xxw.platform.plugin.db.api.util.DatabaseUtil;
import com.xxw.platform.plugin.db.init.util.SqlExe;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * 数据库初始化，可初始化表，校验字段，校验表名是否存在等
 *
 * @author liaoxiting
 * @date 2018-07-29 22:05
 */
@Slf4j
@Getter
@Setter
public abstract class DbInitializer {

    /**
     * 如果为true，则数据库校验失败会抛出异常
     */
    private Boolean fieldValidatorExceptionFlag = true;

    public DbInitializer() {

    }

    public DbInitializer(Boolean fieldValidatorExceptionFlag) {
        this.fieldValidatorExceptionFlag = fieldValidatorExceptionFlag;
    }

    @Resource
    @Getter
    private DruidProperties druidProperties;

    /**
     * 初始化数据库
     *
     * @author liaoxiting
     * @date 2018/7/30 上午10:30
     */
    public void dbInit() {

        // 初始化表
        initTable();

        // 校验实体和对应表结构是否有不一致的
        fieldsValidate();
    }

    /**
     * 初始化表结构
     *
     * @author liaoxiting
     * @date 2018/7/30 上午10:24
     */
    private void initTable() {

        // 校验参数
        String tableName = this.getTableName();
        String tableInitSql = this.getTableInitSql();
        if (ObjectUtil.isEmpty(tableName) || ObjectUtil.isEmpty(tableInitSql)) {
            if (fieldValidatorExceptionFlag) {
                throw new ServiceException(DbInitEnum.INIT_TABLE_EMPTY_PARAMS);
            }
        }

        // 列出数据库中所有的表
        List<TableInfo> tableInfos = DatabaseUtil.selectTables(druidProperties);
        boolean haveSmsTableFlag = false;
        for (TableInfo tableInfo : tableInfos) {
            if (tableInfo.getTableName().equalsIgnoreCase(tableName)) {
                haveSmsTableFlag = true;
                break;
            }
        }

        // 判断数据库中是否有这张表，如果没有就初始化
        if (!haveSmsTableFlag) {
            SqlExe.update(tableInitSql);
            log.info("初始化" + getTableName() + "成功！");
        }

    }

    /**
     * 校验实体和对应表结构是否有不一致的
     *
     * @author liaoxiting
     * @date 2018/7/30 上午10:24
     */
    private void fieldsValidate() {

        //检查数据库中的字段，是否和实体字段一致
        List<TableFieldInfo> tableFields = DatabaseUtil.getTableFields(druidProperties, getTableName());
        if (tableFields != null && !tableFields.isEmpty()) {

            //用于保存实体中不存在的字段的名称集合
            List<String> fieldsNotInClass = new ArrayList<>();

            //反射获取字段的所有字段名称
            List<String> classFields = this.getClassFields();
            for (TableFieldInfo tableField : tableFields) {
                String fieldName = tableField.getColumnName();
                if (!classFields.contains(fieldName.toLowerCase())) {
                    fieldsNotInClass.add(fieldName);
                }
            }

            //如果集合不为空，代表有实体和数据库不一致的数据
            if (!fieldsNotInClass.isEmpty()) {
                log.error("实体中和数据库字段不一致的字段如下：" + JSON.toJSONString(fieldsNotInClass));
                if (fieldValidatorExceptionFlag) {
                    throw new ServiceException(DbInitEnum.FIELD_VALIDATE_ERROR);
                }
            }
        }
    }

    /**
     * 反射获取类的所有字段
     *
     * @author liaoxiting
     * @date 2018/7/30 上午10:06
     */
    private List<String> getClassFields() {
        Class<?> entityClass = this.getEntityClass();
        Field[] declaredFields = ClassUtil.getDeclaredFields(entityClass);
        ArrayList<String> filedNamesUnderlineCase = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            String fieldName = StrUtil.toUnderlineCase(declaredField.getName());
            filedNamesUnderlineCase.add(fieldName);
        }

        // 获取父类的所有字段名称
        Field[] superfields = ReflectUtil.getFields(entityClass.getSuperclass());
        for (Field superfield : superfields) {
            String fieldName = StrUtil.toUnderlineCase(superfield.getName());
            filedNamesUnderlineCase.add(fieldName);
        }

        return filedNamesUnderlineCase;
    }

    /**
     * 获取表的初始化语句
     *
     * @author liaoxiting
     * @date 2018/7/29 22:10
     */
    protected abstract String getTableInitSql();

    /**
     * 获取表的名称
     *
     * @author liaoxiting
     * @date 2018/7/29 22:10
     */
    protected abstract String getTableName();

    /**
     * 获取表对应的实体
     *
     * @author liaoxiting
     * @date 2018/7/29 22:49
     */
    protected abstract Class<?> getEntityClass();

}
