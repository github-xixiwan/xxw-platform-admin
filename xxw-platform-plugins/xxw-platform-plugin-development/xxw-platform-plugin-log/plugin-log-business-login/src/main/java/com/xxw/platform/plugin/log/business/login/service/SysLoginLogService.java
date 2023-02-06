package com.xxw.platform.plugin.log.business.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogDto;
import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogRequest;
import com.xxw.platform.plugin.log.business.login.entity.SysLoginLog;

/**
 * 登录日志service接口
 *
 * @author liaoxiting
 * @date 2021/1/13 10:56
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 删除
     *
     * @param sysLoginLogRequest 参数
     * @author liaoxiting
     * @date 2021/1/13 10:55
     */
    void del(SysLoginLogRequest sysLoginLogRequest);

    /**
     * 清空登录日志
     *
     * @author liaoxiting
     * @date 2021/1/13 10:55
     */
    void delAll();

    /**
     * 查看相信
     *
     * @param sysLoginLogRequest 参数
     * @author liaoxiting
     * @date 2021/1/13 10:56
     */
    SysLoginLog detail(SysLoginLogRequest sysLoginLogRequest);

    /**
     * 分页查询
     *
     * @param sysLoginLogRequest 参数
     * @author liaoxiting
     * @date 2021/1/13 10:57
     */
    PageResult<SysLoginLogDto> findPage(SysLoginLogRequest sysLoginLogRequest);

}
