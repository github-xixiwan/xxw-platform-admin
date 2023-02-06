package com.xxw.platform.plugin.cache.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.frame.common.constants.TenantConstants;
import com.xxw.platform.frame.common.tenant.TenantPrefixApi;
import com.xxw.platform.plugin.cache.api.constants.CacheConstants;

import java.util.Collection;
import java.util.Map;
/**
 * 缓存操作的基础接口，可以实现不同种缓存实现
 * <p>
 * 泛型为cache的值类class类型
 *
 * @author liaoxiting
 * @date 2020/7/8 22:02
 */
public interface CacheOperatorApi<T> {

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     * @author liaoxiting
     * @date 2020/7/8 22:06
     */
    void put(String key, T value);

    /**
     * 添加缓存（带过期时间，单位是秒）
     *
     * @param key            键
     * @param value          值
     * @param timeoutSeconds 过期时间，单位秒
     * @author liaoxiting
     * @date 2020/7/8 22:07
     */
    void put(String key, T value, Long timeoutSeconds);

    /**
     * 通过缓存key获取缓存
     *
     * @param key 键
     * @return 值
     * @author liaoxiting
     * @date 2020/7/8 22:08
     */
    T get(String key);

    /**
     * 删除缓存
     *
     * @param key 键，多个
     * @author liaoxiting
     * @date 2020/7/8 22:09
     */
    void remove(String... key);

    /**
     * 删除缓存
     *
     * @param key 键，多个
     * @author liaoxiting
     * @date 2020/7/8 22:09
     */
    void expire(String key, Long expiredSeconds);

    /**
     * 判断某个key值是否存在于缓存
     *
     * @param key 缓存的键
     * @return true-存在，false-不存在
     * @author liaoxiting
     * @date 2020/11/20 16:50
     */
    boolean contains(String key);

    /**
     * 获得缓存的所有key列表（不带common prefix的）
     *
     * @return key列表
     * @author liaoxiting
     * @date 2020/7/8 22:11
     */
    Collection<String> getAllKeys();

    /**
     * 获得缓存的所有值列表
     *
     * @return 值列表
     * @author liaoxiting
     * @date 2020/7/8 22:11
     */
    Collection<T> getAllValues();

    /**
     * 获取所有的key，value
     *
     * @return 键值map
     * @author liaoxiting
     * @date 2020/7/8 22:11
     */
    Map<String, T> getAllKeyValues();

    /**
     * 通用缓存的前缀，用于区分不同业务
     * <p>
     * 如果带了前缀，所有的缓存在添加的时候，key都会带上这个前缀
     *
     * @return 缓存前缀
     * @author liaoxiting
     * @date 2020/7/9 11:06
     */
    String getCommonKeyPrefix();

    /**
     * 是否按租户维度去切割缓存（不推荐开启）
     * <p>
     * key的组成方式：租户前缀:业务前缀:业务key
     * <p>
     * 如果不开启租户切割，则租户前缀一直会为master:
     *
     * @author liaoxiting
     * @date 2022/11/9 19:02
     */
    default Boolean divideByTenant() {
        return false;
    }

    /**
     * 获取最终的计算前缀
     * <p>
     * key的组成方式：租户前缀:业务前缀:业务key
     *
     * @author liaoxiting
     * @date 2022/11/9 10:41
     */
    default String getFinalPrefix() {
        // 获取租户前缀
        String tenantPrefix = getTenantPrefix();

        // 计算最终前缀
        return tenantPrefix + CacheConstants.CACHE_DELIMITER + getCommonKeyPrefix() + CacheConstants.CACHE_DELIMITER;
    }

    /**
     * 计算最终插入缓存的key值
     * <p>
     * key的组成方式：租户前缀:业务前缀:业务key
     *
     * @param keyParam 用户传递的key参数
     * @return 最终插入缓存的key值
     * @author liaoxiting
     * @date 2021/7/30 21:18
     */
    default String calcKey(String keyParam) {
        if (StrUtil.isEmpty(keyParam)) {
            return getFinalPrefix();
        } else {
            return getFinalPrefix() + keyParam;
        }
    }

    /**
     * 删除缓存key的前缀，返回用户最原始的key
     *
     * @param finalKey 最终存在CacheOperator的key
     * @return 用户最原始的key
     * @author liaoxiting
     * @date 2022/11/9 10:31
     */
    default String removePrefix(String finalKey) {

        if (ObjectUtil.isEmpty(finalKey)) {
            return "";
        }

        return StrUtil.removePrefix(finalKey, getFinalPrefix());
    }

    /**
     * 获取租户前缀
     *
     * @author liaoxiting
     * @date 2022/11/9 10:35
     */
    default String getTenantPrefix() {

        // 缓存是否按租户维度切分
        Boolean divideByTenantFlag = divideByTenant();

        // 如果不按租户维度切分，则默认都返回为master
        if (!divideByTenantFlag) {
            return TenantConstants.MASTER_DATASOURCE_NAME;
        }

        // 用户的租户前缀
        String tenantPrefix = "";
        try {
            TenantPrefixApi tenantPrefixApi = SpringUtil.getBean(TenantPrefixApi.class);
            if (tenantPrefixApi != null) {
                tenantPrefix = tenantPrefixApi.getTenantPrefix();
            }
        } catch (Exception e) {
            // 如果找不到这个bean，则没有加载多租户插件
        }

        // 如果租户前缀为空，则设置为主租户的编码
        if (ObjectUtil.isEmpty(tenantPrefix)) {
            tenantPrefix = TenantConstants.MASTER_DATASOURCE_NAME;
        }

        return tenantPrefix;
    }

}
