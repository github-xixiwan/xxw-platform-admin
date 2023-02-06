package com.xxw.platform.plugin.system.api.enums;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.xxw.platform.frame.common.base.ReadableEnum;
import lombok.Getter;

/**
 * 组织机构审批人类型
 *
 * @author liaoxiting
 * @date 2022/9/13 23:16
 */
@Getter
public enum OrgApproverTypeEnum implements ReadableEnum<OrgApproverTypeEnum> {

    /**
     * 负责人
     */
    FZR(1, "负责人"),

    /**
     * 部长
     */
    BZ(2, "部长"),

    /**
     * 体系负责人
     */
    TXFZR(3, "体系负责人"),

    /**
     * 部门助理
     */
    BMZL(4, "部门助理"),

    /**
     * 资产助理
     */
    ZCZL(5, "资产助理"),

    /**
     * 考勤专员
     */
    KQZY(6, "考勤专员"),

    /**
     * HRBP
     */
    HRBP(7, "HRBP"),

    /**
     * 门禁员
     */
    MJY(8, "门禁员"),

    /**
     * 办公账号员
     */
    BGZHY(9, "办公账号员"),

    /**
     * 转岗须知员
     */
    ZGXZY(10, "转岗须知员");

    private final Integer code;

    private final String name;

    OrgApproverTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Object getKey() {
        return this.code;
    }

    @Override
    public Object getName() {
        return this.name;
    }

    @Override
    public OrgApproverTypeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (OrgApproverTypeEnum value : OrgApproverTypeEnum.values()) {
            if (value.code.equals(Convert.toInt(originValue))) {
                return value;
            }
        }
        return null;
    }
}
