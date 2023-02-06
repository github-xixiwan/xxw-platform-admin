package com.xxw.platform.plugin.db.api.pojo.page;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 *
 * @author liaoxiting
 * @date 2020/10/15 15:53
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 第几页
     */
    @ChineseDescription("第几页,从1开始")
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    @ChineseDescription("每页条数")
    private Integer pageSize = 20;

    /**
     * 总页数
     */
    @ChineseDescription("总页数")
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    @ChineseDescription("总记录数")
    private Integer totalRows = 0;

    /**
     * 结果集
     */
    @ChineseDescription("结果集")
    private List<T> rows;

}
