package com.xxw.platform.plugin.system.business.home.service;

import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;
import com.xxw.platform.plugin.system.api.pojo.home.HomeCompanyInfo;
import com.xxw.platform.plugin.system.api.pojo.user.request.OnlineUserRequest;
import com.xxw.platform.plugin.system.business.home.pojo.OnlineUserStat;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;

import java.util.List;

/**
 * 首页服务接口
 *
 * @author fengshuonan
 * @date 2022/2/11 20:41
 */
public interface HomePageService {

    /**
     * 查询本用户最近操作记录
     *
     * @author fengshuonan
     * @date 2022/2/11 20:40
     */
    List<LogRecordDTO> getRecentLogs();

    /**
     * 获取在线用户统计
     *
     * @author fengshuonan
     * @date 2022/2/11 20:40
     */
    OnlineUserStat getOnlineUserList(OnlineUserRequest onlineUserRequest);

    /**
     * 获取首页公司部门人员信息统计
     *
     * @author fengshuonan
     * @date 2022/2/11 21:03
     */
    HomeCompanyInfo getHomeCompanyInfo();

    /**
     * 获取常用功能集合
     *
     * @author fengshuonan
     * @date 2022/2/11 22:02
     */
    List<SysMenu> getCommonFunctions();

    /**
     * 将缓存中的访问次数信息保存到数据库
     *
     * @author fengshuonan
     * @date 2022/2/11 22:02
     */
    void saveStatisticsCacheToDb();
}
