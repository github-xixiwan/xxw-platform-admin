package com.xxw.platform.plugin.system.business.home.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.business.home.entity.SysStatisticsUrl;
import com.xxw.platform.plugin.system.business.home.pojo.request.SysStatisticsUrlRequest;

import java.util.List;

/**
 * 常用功能列表 服务类
 *
 * @author fengshuonan
 * @date 2022/02/10 21:17
 */
public interface SysStatisticsUrlService extends IService<SysStatisticsUrl> {

    /**
     * 新增
     *
     * @param sysStatisticsUrlRequest 请求参数
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    void add(SysStatisticsUrlRequest sysStatisticsUrlRequest);

    /**
     * 删除
     *
     * @param sysStatisticsUrlRequest 请求参数
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    void del(SysStatisticsUrlRequest sysStatisticsUrlRequest);

    /**
     * 编辑
     *
     * @param sysStatisticsUrlRequest 请求参数
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    void edit(SysStatisticsUrlRequest sysStatisticsUrlRequest);

    /**
     * 查询详情
     *
     * @param sysStatisticsUrlRequest 请求参数
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    SysStatisticsUrl detail(SysStatisticsUrlRequest sysStatisticsUrlRequest);

    /**
     * 获取列表
     *
     * @param sysStatisticsUrlRequest 请求参数
     * @return List<SysStatisticsUrl>   返回结果
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    List<SysStatisticsUrl> findList(SysStatisticsUrlRequest sysStatisticsUrlRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysStatisticsUrlRequest 请求参数
     * @return PageResult<SysStatisticsUrl>   返回结果
     * @author fengshuonan
     * @date 2022/02/10 21:17
     */
    PageResult<SysStatisticsUrl> findPage(SysStatisticsUrlRequest sysStatisticsUrlRequest);

}
