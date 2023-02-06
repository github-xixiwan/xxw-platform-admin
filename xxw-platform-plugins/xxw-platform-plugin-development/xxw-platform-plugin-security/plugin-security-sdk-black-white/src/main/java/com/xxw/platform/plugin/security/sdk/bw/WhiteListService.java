package com.xxw.platform.plugin.security.sdk.bw;

import com.xxw.platform.plugin.cache.api.CacheOperatorApi;
import com.xxw.platform.plugin.security.api.WhiteListApi;

import java.util.Collection;

/**
 * 白名单的实现
 * <p>
 * 白名单的数据在访问资源时不受限
 *
 * @author liaoxiting
 * @date 2020/11/20 15:53
 */
public class WhiteListService implements WhiteListApi {

    private final CacheOperatorApi<String> cacheOperatorApi;

    public WhiteListService(CacheOperatorApi<String> cacheOperatorApi) {
        this.cacheOperatorApi = cacheOperatorApi;
    }

    @Override
    public void addWhiteItem(String content) {
        cacheOperatorApi.put(content, content);
    }

    @Override
    public void removeWhiteItem(String content) {
        cacheOperatorApi.remove(content);
    }

    @Override
    public Collection<String> getWhiteList() {
        return cacheOperatorApi.getAllKeys();
    }

    @Override
    public boolean contains(String content) {
        return cacheOperatorApi.contains(content);
    }

}
