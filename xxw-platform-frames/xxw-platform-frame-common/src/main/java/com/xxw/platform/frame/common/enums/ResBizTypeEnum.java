package com.xxw.platform.frame.common.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xxw.platform.frame.common.base.ReadableEnum;
import lombok.Getter;

/**
 * 用来标识菜单或者接口资源的类别，1-系统类的菜单或资源，2-业务类的菜单或资源
 * <p>
 * 业务类的菜单就是项目开发时候的业务，例如订单管理，商品管理
 * 系统类就是用户管理，角色管理，日志管理这些
 *
 * @author liaoxiting
 * @date 2022/9/28 11:15
 */
@Getter
public enum ResBizTypeEnum implements ReadableEnum<ResBizTypeEnum> {

    /**
     * 业务类
     */
    BUSINESS(1, "业务类"),

    /**
     * 系统类
     */
    SYSTEM(2, "系统类"),

    /**
     * 默认
     * <p>
     * 如果是默认，则方法的业务类别，则会根据控制器上@ApiResource的类别决定具体方法的类别
     */
    DEFAULT(3, "默认类");

    /**
     * 使用@EnumValue注解，标记mybatis-plus保存到库中使用code值
     */
    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    ResBizTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Object getKey() {
        return this.code;
    }

    @Override
    public Object getName() {
        return this.message;
    }

    @Override
    public ResBizTypeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (ResBizTypeEnum value : ResBizTypeEnum.values()) {
            if (String.valueOf(value.code).equals(originValue)) {
                return value;
            }
        }
        return null;
    }

}
