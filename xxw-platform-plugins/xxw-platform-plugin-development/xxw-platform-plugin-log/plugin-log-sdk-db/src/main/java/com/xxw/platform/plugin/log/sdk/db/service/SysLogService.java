package com.xxw.platform.plugin.log.sdk.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.pojo.manage.LogManagerRequest;
import com.xxw.platform.plugin.log.sdk.db.entity.SysLog;

import java.util.List;

/**
 * 日志记录 service接口
 *
 * @author liaoxiting
 * @date 2020/11/2 17:44
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 新增
     *
     * @param logManagerRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void add(LogManagerRequest logManagerRequest);

    /**
     * 删除
     *
     * @param logManagerRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void del(LogManagerRequest logManagerRequest);

    /**
     * 删除所有数据
     *
     * @param logManagerRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void delAll(LogManagerRequest logManagerRequest);

    /**
     * 查看日志详情
     *
     * @author liaoxiting
     * @date 2021/1/11 17:51
     */
    SysLog detail(LogManagerRequest logManagerParam);

    /**
     * 查询-列表-按实体对象
     *
     * @param logManagerParam 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    List<SysLog> findList(LogManagerRequest logManagerParam);

    /**
     * 查询-列表-分页-按实体对象
     *
     * @param logManagerParam 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    PageResult<SysLog> findPage(LogManagerRequest logManagerParam);

}
