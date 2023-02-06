package com.xxw.platform.plugin.db.mp.dboperator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.xxw.platform.frame.common.enums.DbTypeEnum;
import com.xxw.platform.frame.common.util.DatabaseTypeUtil;
import com.xxw.platform.plugin.db.api.DbOperatorApi;
import com.xxw.platform.plugin.db.api.pojo.druid.DruidProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据库操作的实现
 *
 * @author liaoxiting
 * @date 2020/11/4 14:48
 */
@Service
public class DbOperatorImpl implements DbOperatorApi {

    @Resource
    private DruidProperties druidProperties;

    @Override
    public DbTypeEnum getCurrentDbType() {
        return DatabaseTypeUtil.getDbType(druidProperties.getUrl());
    }

    @Override
    public int selectCount(String sql, Object... args) {
        long selectCount = SqlRunner.db().selectCount(sql, args);
        return Convert.toInt(selectCount);
    }

    @Override
    public Set<Long> findSubListByParentId(String tableName, String parentIdsFieldName, String keyFieldName, Long keyFieldValue) {

        // 组装sql
        String sqlTemplate = "select {} from {} where {} like '%[{}]%'";
        String sql = StrUtil.format(sqlTemplate, keyFieldName, tableName, parentIdsFieldName, keyFieldValue.toString());

        // 查询所有子级的id集合，结果不包含被查询的keyFieldValue
        List<Object> subIds = SqlRunner.db().selectObjs(sql);

        // 转为Set<Long>
        return subIds.stream().map(i -> Long.valueOf(i.toString())).collect(Collectors.toSet());
    }

}
