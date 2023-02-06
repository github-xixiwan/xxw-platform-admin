package com.xxw.platform.plugin.system.starter.cache;
import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.cache.sdk.redis.util.CreateRedisTemplateUtil;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserOrgDTO;
import com.xxw.platform.plugin.system.business.home.cache.InterfaceStatisticsRedisCache;
import com.xxw.platform.plugin.system.business.role.cache.RoleDataScopeRedisCache;
import com.xxw.platform.plugin.system.business.role.cache.RoleRedisCache;
import com.xxw.platform.plugin.system.business.role.cache.RoleResourceRedisCache;
import com.xxw.platform.plugin.system.business.role.entity.SysRole;
import com.xxw.platform.plugin.system.business.theme.cache.ThemeRedisCache;
import com.xxw.platform.plugin.system.business.theme.pojo.DefaultTheme;
import com.xxw.platform.plugin.system.business.user.cache.SysUserRedisCache;
import com.xxw.platform.plugin.system.business.user.cache.UserOrgRedisCache;
import com.xxw.platform.plugin.system.business.user.cache.UserRoleRedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * 系统管理缓存的自动配置，Redis配置
 *
 * @author liaoxiting
 * @date 2022/11/8 23:26
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
public class SystemRedisCacheAutoConfiguration {

    /**
     * 用户的缓存，非在线用户缓存，此缓存为了加快查看用户相关操作
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<SysUserDTO> sysUserCacheOperatorApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SysUserDTO> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new SysUserRedisCache(redisTemplate);
    }

    /**
     * 用户角色对应的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<List<Long>> userRoleCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Long>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new UserRoleRedisCache(redisTemplate);
    }

    /**
     * 角色信息对应的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<SysRole> roleInfoCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SysRole> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RoleRedisCache(redisTemplate);
    }

    /**
     * 用户组织机构的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<SysUserOrgDTO> userOrgCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SysUserOrgDTO> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new UserOrgRedisCache(redisTemplate);
    }

    /**
     * 用户资源绑定的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<List<String>> roleResourceCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<String>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RoleResourceRedisCache(redisTemplate);
    }

    /**
     * 角色绑定的数据范围的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<List<Long>> roleDataScopeCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Long>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RoleDataScopeRedisCache(redisTemplate);
    }

    /**
     * 主题的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<DefaultTheme> themeCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, DefaultTheme> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new ThemeRedisCache(redisTemplate);
    }

    /**
     * 接口统计的缓存
     *
     * @author liaoxiting
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<Map<Long, Integer>> requestCountCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Map<Long, Integer>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new InterfaceStatisticsRedisCache(redisTemplate);
    }

}
