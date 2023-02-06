package com.xxw.platform.plugin.log.api;

import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.pojo.manage.LogManagerRequest;
import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;

import java.util.List;

/**
 * 日志管理相关的接口
 * <p>
 * 接口有多种实现，例如基于文件存储的日志，基于数据库存储的日志，基于es存储的日志
 *
 * @author liaoxiting
 * @date 2020/10/27 16:19
 */
public interface LogManagerApi {

    /**
     * 查询日志列表
     *
     * @param logManagerRequest 查询条件
     * @return 返回查询日志列表
     * @author liaoxiting
     * @date 2020/10/28 11:27
     */
    List<LogRecordDTO> findList(LogManagerRequest logManagerRequest);

    /**
     * 查询日志列表
     *
     * @param logManagerRequest 查询条件
     * @return 返回查询日志列表分页结果
     * @author liaoxiting
     * @date 2020/11/3 10:40
     */
    PageResult<LogRecordDTO> findPage(LogManagerRequest logManagerRequest);

    /**
     * 批量删除日志
     * <p>
     * 删除日志条件必须传入开始时间、结束时间、服务名称三个参数
     *
     * @param logManagerRequest 参数的封装
     * @author liaoxiting
     * @date 2020/10/28 11:47
     */
    void del(LogManagerRequest logManagerRequest);

    /**
     * 查询日志详情
     *
     * @author liaoxiting
     * @date 2021/2/1 19:47
     */
    LogRecordDTO detail(LogManagerRequest logManagerRequest);

}
