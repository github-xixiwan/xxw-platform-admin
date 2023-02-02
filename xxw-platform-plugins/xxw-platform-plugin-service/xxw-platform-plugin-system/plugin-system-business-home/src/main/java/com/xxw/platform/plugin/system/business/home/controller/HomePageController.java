package com.xxw.platform.plugin.system.business.home.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.system.api.pojo.home.HomeCompanyInfo;
import com.xxw.platform.plugin.system.api.pojo.user.request.OnlineUserRequest;
import com.xxw.platform.plugin.system.business.home.pojo.OnlineUserStat;
import com.xxw.platform.plugin.system.business.home.service.HomePageService;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页控制器
 *
 * @author xixiaowei
 * @date 2022/1/25 9:44
 */
@RestController
@ApiResource(name = "首页", resBizType = ResBizTypeEnum.SYSTEM)
public class HomePageController {

    @Resource
    private HomePageService homePageService;

    /**
     * 查询动态列表
     *
     * @author xixiaowei
     * @date 2022/1/25 14:52
     */
    @GetResource(name = "查询动态列表", path = "/homePage/getRecentLogs", requiredPermission = false)
    public ResponseData<List<LogRecordDTO>> getRecentLogs() {
        return new SuccessResponseData<>(homePageService.getRecentLogs());
    }

    /**
     * 查询在线用户列表
     *
     * @author xixiaowei
     * @date 2022/1/25 14:11
     */
    @GetResource(name = "查询在线用户列表", path = "/homePage/getOnlineUserList", requiredPermission = false)
    public ResponseData<OnlineUserStat> getOnlineUserList(OnlineUserRequest onlineUserRequest) {
        return new SuccessResponseData<>(homePageService.getOnlineUserList(onlineUserRequest));
    }

    /**
     * 获取首页企业和公司信息
     *
     * @author xixiaowei
     * @date 2022/2/9 10:12
     */
    @GetResource(name = "获取首页企业和公司信息", path = "/homePage/getHomeCompanyInfo", requiredPermission = false)
    public ResponseData<HomeCompanyInfo> getHomeCompanyInfo() {
        return new SuccessResponseData<>(homePageService.getHomeCompanyInfo());
    }

    /**
     * 获取常用功能接口
     *
     * @author xixiaowei
     * @date 2022/2/10 11:34
     */
    @GetResource(name = "获取常用功能接口", path = "/homePage/getCommonFunctions", requiredPermission = false)
    public ResponseData<List<SysMenu>> getCommonFunctions() {
        return new SuccessResponseData<>(homePageService.getCommonFunctions());
    }
}
