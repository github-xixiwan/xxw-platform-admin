package com.xxw.platform.plugin.system.starter.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.system.api.constants.StatisticsCacheConstants;
import com.xxw.platform.plugin.system.api.constants.SystemCachesConstants;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserOrgDTO;
import com.xxw.platform.plugin.system.business.home.cache.InterfaceStatisticsMemoryCache;
import com.xxw.platform.plugin.system.business.role.cache.RoleDataScopeMemoryCache;
import com.xxw.platform.plugin.system.business.role.cache.RoleMemoryCache;
import com.xxw.platform.plugin.system.business.role.cache.RoleResourceMemoryCache;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;
import com.xxw.platform.plugin.system.business.theme.cache.ThemeMemoryCache;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;
import com.xxw.platform.plugin.system.business.user.cache.SysUserMemoryCache;
import com.xxw.platform.plugin.system.business.user.cache.UserOrgMemoryCache;
import com.xxw.platform.plugin.system.business.user.cache.UserRoleMemoryCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 系统管理缓存的自动配置（默认内存缓存）
 *
 * @author liaoxiting
 * @date 2021/2/28 10:29
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.connection.RedisConnectionFactory")
public class SystemMemoryCacheAutoConfiguration {

    /**
     * 用户的缓存，非在线用户缓存，此缓存为了加快查看用户相关操作
     *
     * @author liaoxiting
     * @date 2021/2/28 10:30
     */
    @Bean
    public CacheOperatorApi<SysUserDTO> sysUserCacheOperatorApi() {
        TimedCache<String, SysUserDTO> sysUserTimedCache = CacheUtil.newTimedCache(SystemCachesConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new SysUserMemoryCache(sysUserTimedCache);
    }

    /**
     * 用户角色对应的缓存
     *
     * @author liaoxiting
     * @date 2021/7/29 23:00
     */
    @Bean
    public CacheOperatorApi<List<Long>> userRoleCacheApi() {
        TimedCache<String, List<Long>> userRoleCache = CacheUtil.newTimedCache(SystemCachesConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new UserRoleMemoryCache(userRoleCache);
    }

    /**
     * 角色信息对应的缓存
     *
     * @author liaoxiting
     * @date 2021/7/29 23:00
     */
    @Bean
    public CacheOperatorApi<SysRole> roleInfoCacheApi() {
        TimedCache<String, SysRole> roleCache = CacheUtil.newTimedCache(SystemCachesConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new RoleMemoryCache(roleCache);
    }

    /**
     * 用户组织机构的缓存
     *
     * @author liaoxiting
     * @date 2021/7/30 23:09
     */
    @Bean
    public CacheOperatorApi<SysUserOrgDTO> userOrgCacheApi() {
        TimedCache<String, SysUserOrgDTO> roleCache = CacheUtil.newTimedCache(SystemCachesConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new UserOrgMemoryCache(roleCache);
    }

    /**
     * 用户资源绑定的缓存
     *
     * @author liaoxiting
     * @date 2021/7/30 23:29
     */
    @Bean
    public CacheOperatorApi<List<String>> roleResourceCacheApi() {
        TimedCache<String, List<String>> roleCache = CacheUtil.newTimedCache(SystemCachesConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new RoleResourceMemoryCache(roleCache);
    }

    /**
     * 角色绑定的数据范围的缓存
     *
     * @author liaoxiting
     * @date 2021/7/31 17:59
     */
    @Bean
    public CacheOperatorApi<List<Long>> roleDataScopeCacheApi() {
        TimedCache<String, List<Long>> roleCache = CacheUtil.newTimedCache(SystemCachesConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new RoleDataScopeMemoryCache(roleCache);
    }

    /**
     * 主题的缓存
     *
     * @author liaoxiting
     * @date 2021/7/31 17:59
     */
    @Bean
    public CacheOperatorApi<DefaultTheme> themeCacheApi() {
        TimedCache<String, DefaultTheme> themeCache = CacheUtil.newTimedCache(Long.MAX_VALUE);
        return new ThemeMemoryCache(themeCache);
    }

    /**
     * 接口统计的缓存
     *
     * @author liaoxiting
     * @date 2022/2/9 16:53
     */
    @Bean
    public CacheOperatorApi<Map<Long, Integer>> requestCountCacheApi() {
        TimedCache<String, Map<Long, Integer>> timedCache = CacheUtil.newTimedCache(StatisticsCacheConstants.INTERFACE_STATISTICS_CACHE_TIMEOUT_SECONDS);
        return new InterfaceStatisticsMemoryCache(timedCache);
    }
}
