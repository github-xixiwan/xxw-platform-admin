package com.xxw.platform.plugin.db.mp.fieldfill;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.db.api.constants.DbFieldConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 字段自动填充工具，在mybatis-plus执行更新和新增操作时候，会对指定字段进行自动填充，例如 create_time 等字段
 *
 * @author liaoxiting
 * @date 2020/10/16 17:14
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        try {
            // 设置createUser（BaseEntity)
            setValue(metaObject, DbFieldConstants.CREATE_USER, this.getUserUniqueId());

            // 设置createTime（BaseEntity)
            setValue(metaObject, DbFieldConstants.CREATE_TIME, new Date());

            // 设置删除标记 默认N-删除
            setDelFlagDefaultValue(metaObject);

            // 设置状态字段 默认1-启用
            setStatusDefaultValue(metaObject);

            // 设置乐观锁字段，从0开始
            setValue(metaObject, DbFieldConstants.VERSION_FLAG, 0L);

            // 设置组织id
            setValue(metaObject, DbFieldConstants.ORG_ID, this.getUserOrgId());

        } catch (ReflectionException e) {
            log.warn("CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {

        try {
            // 设置updateUser（BaseEntity)
            setValue(metaObject, DbFieldConstants.UPDATE_USER, this.getUserUniqueId());

            // 设置updateTime（BaseEntity)
            setValue(metaObject, DbFieldConstants.UPDATE_TIME, new Date());
        } catch (ReflectionException e) {
            log.warn("CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }

    }

    /**
     * 获取用户唯一id
     *
     * @author liaoxiting
     * @date 2021/10/29 10:01
     */
    private Long getUserUniqueId() {

        try {
            return LoginContext.me().getLoginUser().getUserId();
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }

    }

    /**
     * 设置属性
     *
     * @author liaoxiting
     * @date 2021/10/29 10:01
     */
    private void setValue(MetaObject metaObject, String fieldName, Object value) {
        Object originalAttr = getFieldValByName(fieldName, metaObject);
        if (ObjectUtil.isEmpty(originalAttr)) {
            setFieldValByName(fieldName, value, metaObject);
        }
    }

    /**
     * 获取用户唯一id
     *
     * @author liaoxiting
     * @date 2022/09/01 10:14
     */
    private Long getUserOrgId() {

        try {
            return LoginContext.me().getLoginUser().getOrganizationId();
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }

    }

    /**
     * 设置属性，针对逻辑删除字段
     *
     * @author liaoxiting
     * @date 2022/9/7 17:23
     */
    private void setDelFlagDefaultValue(MetaObject metaObject) {
        Object originalAttr = getFieldValByName(DbFieldConstants.DEL_FLAG, metaObject);
        if (ObjectUtil.isNotEmpty(originalAttr)) {
            return;
        }
        Object originalObject = metaObject.getOriginalObject();
        try {
            // 获取delFlag字段的类型，如果是枚举类型，则设置枚举
            Field declaredField = originalObject.getClass().getDeclaredField(DbFieldConstants.DEL_FLAG);
            if (ClassUtil.isEnum(declaredField.getType())) {
                setFieldValByName(DbFieldConstants.DEL_FLAG, YesOrNotEnum.N, metaObject);
            } else {
                setFieldValByName(DbFieldConstants.DEL_FLAG, YesOrNotEnum.N.getCode(), metaObject);
            }
        } catch (NoSuchFieldException ignored) {
            // 没有字段，忽略
        }
    }

    /**
     * 设置属性，针对状态字段
     *
     * @author liaoxiting
     * @date 2022/9/7 17:23
     */
    private void setStatusDefaultValue(MetaObject metaObject) {
        Object originalAttr = getFieldValByName(DbFieldConstants.STATUS_FLAG, metaObject);
        if (ObjectUtil.isNotEmpty(originalAttr)) {
            return;
        }
        Object originalObject = metaObject.getOriginalObject();
        try {
            // 获取statusFlag字段的类型，如果是枚举类型，则设置枚举
            Field declaredField = originalObject.getClass().getDeclaredField(DbFieldConstants.STATUS_FLAG);
            if (ClassUtil.isEnum(declaredField.getType())) {
                setFieldValByName(DbFieldConstants.STATUS_FLAG, StatusEnum.ENABLE, metaObject);
            } else {
                setFieldValByName(DbFieldConstants.STATUS_FLAG, StatusEnum.ENABLE.getCode(), metaObject);
            }
        } catch (NoSuchFieldException ignored) {
            // 没有字段，忽略
        }
    }
}
