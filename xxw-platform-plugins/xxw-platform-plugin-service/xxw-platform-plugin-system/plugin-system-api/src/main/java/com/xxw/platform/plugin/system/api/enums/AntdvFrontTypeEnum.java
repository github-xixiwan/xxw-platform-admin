package com.xxw.platform.plugin.system.api.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * 前台还是后台菜单：1-前台，2-后台，3-前后台都显示
 *
 * @author liaoxiting
 * @date 2022/9/28 16:54
 */
@Getter
public enum AntdvFrontTypeEnum {

    /**
     * 1-前台
     */
    FRONT(1, "前台"),

    /**
     * 2-后台
     */
    BACKEND(2, "后台"),

    /**
     * 3-前后台都显示
     */
    TOTAL_SHOW(3, "前后台都显示");

    private final Integer code;

    private final String message;

    AntdvFrontTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 转化int为枚举类型
     *
     * @author liaoxiting
     * @date 2022/9/29 15:50
     */
    public static AntdvFrontTypeEnum parseToEnum(Integer originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (AntdvFrontTypeEnum value : AntdvFrontTypeEnum.values()) {
            if (value.code.equals(originValue)) {
                return value;
            }
        }
        return null;
    }
}
