package com.xxw.platform.frame.common.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 请求基类，所有接口请求可继承此类
 *
 * @author liaoxiting
 * @date 2020/10/14 18:12
 */
@Data
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */
    private String searchBeginTime;

    /**
     * 结束时间
     */
    private String searchEndTime;

    /**
     * 分页：每页大小（默认20）
     */
    private Integer pageSize;

    /**
     * 分页：第几页（从1开始）
     */
    private Integer pageNo;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 正序或者倒序排列（asc 或 desc）
     */
    private String sortBy;

    /**
     * 其他参数（如有需要）
     */
    private Map<String, Object> otherParams;

    /**
     * 唯一请求号
     */
    private String requestNo;

    /**
     * 业务节点id
     */
    private String spanId;

    /**
     * 当前登录用户的token
     */
    private String token;

    /**
     * 分组查询条件，例如：所有分组 、 未分组、 我的分组等名称
     */
    @ChineseDescription("分组名称，例如：所有分组 、 未分组、 我的分组等名称")
    private String conditionGroupName;
    /**
     * 查询分组时候in标识：固定传true或false，如果是true，则为in，false为为not in
     */
    private Boolean conditionGroupInFlag;
    /**
     * 业务id集合，当查询未分组或者指定分组时候需要填充此字段，用来查到用户在这个组下有多少个业务id
     */
    private List<Long> conditionGroupUserBizIdList;

    /**
     * 搜索内容，通用查询条件的值
     */
    private String searchText;

    /**
     * 参数校验分组：分页
     */
    public @interface page {
    }

    /**
     * 参数校验分组：查询所有
     */
    public @interface list {
    }

    /**
     * 参数校验分组：增加
     */
    public @interface add {
    }

    /**
     * 参数校验分组：编辑
     */
    public @interface edit {
    }

    /**
     * 参数校验分组：删除
     */
    public @interface delete {
    }

    /**
     * 参数校验分组：详情
     */
    public @interface detail {
    }

    /**
     * 参数校验分组：导出
     */
    public @interface export {
    }

    /**
     * 参数校验分组：修改状态
     */
    public @interface updateStatus {
    }

    /**
     * 参数校验分组：批量删除
     */
    public @interface batchDelete {
    }

}
