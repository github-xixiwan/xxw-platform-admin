package com.xxw.platform.plugin.system.business.home.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxw.platform.frame.common.constants.SymbolConstant;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.auth.api.context.LoginContext;
import com.xxw.platform.plugin.auth.api.pojo.login.LoginUser;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.LogManagerApi;
import com.xxw.platform.plugin.log.api.pojo.manage.LogManagerRequest;
import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;
import com.xxw.platform.plugin.system.api.HomePageServiceApi;
import com.xxw.platform.plugin.system.api.PositionServiceApi;
import com.xxw.platform.plugin.system.api.UserServiceApi;
import com.xxw.platform.plugin.system.api.pojo.home.HomeCompanyInfo;
import com.xxw.platform.plugin.system.api.pojo.user.OnlineUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.request.OnlineUserRequest;
import com.xxw.platform.plugin.system.api.pojo.user.request.SysUserRequest;
import com.xxw.platform.plugin.system.business.home.entity.SysStatisticsCount;
import com.xxw.platform.plugin.system.business.home.entity.SysStatisticsUrl;
import com.xxw.platform.plugin.system.business.home.mapper.SysStatisticsUrlMapper;
import com.xxw.platform.plugin.system.business.home.pojo.OnlineUserStat;
import com.xxw.platform.plugin.system.business.home.service.HomePageService;
import com.xxw.platform.plugin.system.business.home.service.SysStatisticsCountService;
import com.xxw.platform.plugin.system.business.home.service.SysStatisticsUrlService;
import com.xxw.platform.plugin.system.business.menu.entity.SysMenu;
import com.xxw.platform.plugin.system.business.menu.mapper.SysMenuMapper;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrganization;
import com.xxw.platform.plugin.system.business.organization.service.HrOrganizationService;
import com.xxw.platform.plugin.system.business.user.entity.SysUserOrg;
import com.xxw.platform.plugin.system.business.user.service.SysUserOrgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ?????????????????????
 *
 * @author liaoxiting
 * @date 2022/2/11 20:41
 */
@Service
public class HomePageServiceImpl implements HomePageService, HomePageServiceApi {

    @Resource
    private LogManagerApi logManagerApi;

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private HrOrganizationService hrOrganizationService;

    @Resource
    private PositionServiceApi positionServiceApi;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource(name = "requestCountCacheApi")
    private CacheOperatorApi<Map<Long, Integer>> requestCountCacheApi;

    @Resource
    private SysStatisticsCountService sysStatisticsCountService;

    @Resource
    private SysStatisticsUrlService sysStatisticsUrlService;

    @Resource
    private SysStatisticsUrlMapper sysStatisticsUrlMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<LogRecordDTO> getRecentLogs() {

        // ????????????????????????
        LogManagerRequest logManagerRequest = new LogManagerRequest();
        logManagerRequest.setUserId(LoginContext.me().getLoginUser().getUserId());

        PageResult<LogRecordDTO> page = logManagerApi.findPage(logManagerRequest);
        return page.getRows();
    }

    @Override
    public OnlineUserStat getOnlineUserList(OnlineUserRequest onlineUserRequest) {

        OnlineUserStat onlineUserStat = new OnlineUserStat();

        // ?????????????????????
        List<OnlineUserDTO> onlineUserDTOS = userServiceApi.onlineUserList(onlineUserRequest);

        // ??????????????????????????????
        HashSet<String> onlineUserList = new HashSet<>();
        for (OnlineUserDTO onlineUserDTO : onlineUserDTOS) {
            if (ObjectUtil.isNotEmpty(onlineUserDTO.getRealName())) {
                onlineUserList.add(onlineUserDTO.getRealName());
            }
        }
        onlineUserStat.setTotalNum(onlineUserList.size());

        // ?????????20??????
        Set<String> newSet = onlineUserList.stream().limit(20).collect(Collectors.toSet());
        onlineUserStat.setTotalUserNames(newSet);

        return onlineUserStat;
    }

    @Override
    public HomeCompanyInfo getHomeCompanyInfo() {
        HomeCompanyInfo homeCompanyInfo = new HomeCompanyInfo();

        // ???????????????????????????
        long count = hrOrganizationService.count();
        homeCompanyInfo.setOrganizationNum(Convert.toInt(count));

        // ???????????????????????????
        SysUserRequest sysUserRequest = new SysUserRequest();
        List<Long> allUserIdList = userServiceApi.queryAllUserIdList(sysUserRequest);
        homeCompanyInfo.setEnterprisePersonNum(allUserIdList.size());

        // ????????????????????????
        int positionNum = positionServiceApi.positionNum();
        homeCompanyInfo.setPositionNum(positionNum);

        // ????????????????????????????????????id
        LoginUser loginUser = LoginContext.me().getLoginUser();
        Long organizationId = loginUser.getOrganizationId();

        // ??????????????????????????????????????????(???????????????)
        LambdaQueryWrapper<HrOrganization> wrapper = Wrappers.lambdaQuery(HrOrganization.class)
                .like(HrOrganization::getOrgPids, SymbolConstant.LEFT_SQUARE_BRACKETS + organizationId + SymbolConstant.RIGHT_SQUARE_BRACKETS)
                .or()
                .eq(HrOrganization::getOrgId, organizationId)
                .select(HrOrganization::getOrgId);
        List<HrOrganization> organizations = hrOrganizationService.list(wrapper);
        homeCompanyInfo.setCurrentDeptNum(organizations.size());

        // ???????????????????????????????????????????????????
        List<Long> orgIds = organizations.stream().map(HrOrganization::getOrgId).collect(Collectors.toList());
        Long currentOrgPersonNum = sysUserOrgService.count(Wrappers.lambdaQuery(SysUserOrg.class).in(SysUserOrg::getOrgId, orgIds));
        homeCompanyInfo.setCurrentCompanyPersonNum(Convert.toInt(currentOrgPersonNum));

        return homeCompanyInfo;
    }

    @Override
    public List<SysMenu> getCommonFunctions() {

        // ??????????????????????????????????????????????????????
        LoginUser loginUser = LoginContext.me().getLoginUser();
        List<SysStatisticsCount> statList = sysStatisticsCountService.list(
                Wrappers.lambdaQuery(SysStatisticsCount.class).eq(SysStatisticsCount::getUserId, loginUser.getUserId()).orderByDesc(SysStatisticsCount::getStatCount));
        List<Long> statUrlIdList = statList.stream().map(SysStatisticsCount::getStatUrlId).collect(Collectors.toList());

        // ??????????????????????????????
        LambdaQueryWrapper<SysStatisticsUrl> wrapper = Wrappers.lambdaQuery(SysStatisticsUrl.class)
                .eq(SysStatisticsUrl::getAlwaysShow, YesOrNotEnum.Y)
                .select(SysStatisticsUrl::getStatUrlId);
        List<SysStatisticsUrl> alwaysShowList = sysStatisticsUrlService.list(wrapper);

        // ???????????????????????????????????????????????????
        if (ObjectUtil.isNotEmpty(alwaysShowList)) {
            statUrlIdList.addAll(0, alwaysShowList.stream().map(SysStatisticsUrl::getStatUrlId).collect(Collectors.toList()));
        }

        // ??????statUrlId??????8???????????????8???
        if (statUrlIdList.size() > 8) {
            statUrlIdList = statUrlIdList.subList(0, 8);
        }

        // ????????????id??????
        List<Long> usualMenuIds = sysStatisticsUrlMapper.getMenuIdsByStatUrlIdList(statUrlIdList);

        // ??????????????????????????????????????????
        List<SysMenu> list = sysMenuMapper.getMenuStatInfoByMenuIds(usualMenuIds);

        // ?????????icon????????????????????????
        for (SysMenu sysMenu : list) {
            if (sysMenu.getAntdvIcon() != null) {
                String replace = sysMenu.getAntdvIcon().replace("-", "_");
                sysMenu.setAntdvIcon(StrUtil.upperFirst(StrUtil.toCamelCase(replace)));
            }
        }

        return list;
    }

    @Override
    public void saveStatisticsCacheToDb() {
        // key?????????id???value???key???statUrlId????????????value?????????
        Map<String, Map<Long, Integer>> userRequestStats = requestCountCacheApi.getAllKeyValues();
        Set<String> userIds = userRequestStats.keySet();

        // ???????????????????????????????????????
        if (ObjectUtil.isEmpty(userIds)) {
            return;
        }

        // ?????????????????????????????????
        List<SysStatisticsCount> cacheCountList = new ArrayList<>();
        for (String userId : userIds) {
            // ????????????????????????????????????key???statUrlId???value?????????
            Map<Long, Integer> userCounts = userRequestStats.get(userId);
            for (Map.Entry<Long, Integer> userCountItem : userCounts.entrySet()) {
                Long statUrlId = userCountItem.getKey();
                Integer count = userCountItem.getValue();
                SysStatisticsCount sysStatisticsCount = new SysStatisticsCount();
                sysStatisticsCount.setUserId(Long.valueOf(userId));
                sysStatisticsCount.setStatUrlId(statUrlId);
                sysStatisticsCount.setStatCount(count);
                cacheCountList.add(sysStatisticsCount);
            }
        }

        // ??????????????????????????????????????????
        List<Long> userIdsLong = userIds.stream().map(Long::valueOf).collect(Collectors.toList());
        List<SysStatisticsCount> sysStatisticsCounts = sysStatisticsCountService.list(
                Wrappers.lambdaQuery(SysStatisticsCount.class).in(SysStatisticsCount::getUserId, userIdsLong));

        for (SysStatisticsCount cacheItem : cacheCountList) {
            boolean haveRecord = false;
            for (SysStatisticsCount dbItem : sysStatisticsCounts) {
                // ?????????????????????????????????????????????????????????
                if (dbItem.getStatUrlId().equals(cacheItem.getStatUrlId()) && dbItem.getUserId().equals(cacheItem.getUserId())) {
                    haveRecord = true;
                    cacheItem.setStatCountId(dbItem.getStatCountId());
                }
            }
            // ????????????????????????????????????????????????????????????id
            if (!haveRecord) {
                cacheItem.setStatCountId(IdWorker.getId());
            }
        }

        // ????????????????????????????????????????????????
        sysStatisticsCountService.saveOrUpdateBatch(cacheCountList);
    }
}
