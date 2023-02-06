package com.xxw.platform.plugin.dict.business.sqladapter;

import com.xxw.platform.plugin.db.api.sqladapter.AbstractSql;
import lombok.Getter;

/**
 * 创建数据库的sql，可用在租户的创建
 *
 * @author liaoxiting
 * @date 2019-07-16-13:06
 */
@Getter
public class DictTypeSql extends AbstractSql {

    @Override
    protected String mysql() {
        return "CREATE TABLE `sys_dict_type` (\n" +
                "  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型id',\n" +
                "  `dict_type_class` int(11) DEFAULT NULL COMMENT '字典类型： 1-业务类型，2-系统类型，参考 DictTypeClassEnum',\n" +
                "  `dict_type_bus_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典类型业务编码',\n" +
                "  `dict_type_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典类型编码',\n" +
                "  `dict_type_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典类型名称',\n" +
                "  `dict_type_name_pinyin` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典类型名称首字母拼音',\n" +
                "  `dict_type_desc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字典类型描述',\n" +
                "  `status_flag` tinyint(4) DEFAULT NULL COMMENT '字典类型的状态：1-启用，2-禁用，参考 StatusEnum',\n" +
                "  `dict_type_sort` decimal(10,2) DEFAULT NULL COMMENT '排序，带小数点',\n" +
                "  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT 'N' COMMENT '是否删除：Y-被删除，N-未删除',\n" +
                "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                "  `create_user` bigint(20) DEFAULT NULL COMMENT '创建用户id',\n" +
                "  `update_time` datetime DEFAULT NULL COMMENT '修改时间',\n" +
                "  `update_user` bigint(20) DEFAULT NULL COMMENT '修改用户id',\n" +
                "  PRIMARY KEY (`dict_type_id`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型';";
    }

    @Override
    protected String sqlServer() {
        return "";
    }

    @Override
    protected String pgSql() {
        return "";
    }

    @Override
    protected String oracle() {
        return "";
    }
}
