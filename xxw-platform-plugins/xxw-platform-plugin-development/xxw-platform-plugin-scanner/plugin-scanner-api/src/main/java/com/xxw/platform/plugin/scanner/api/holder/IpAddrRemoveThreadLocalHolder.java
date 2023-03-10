package com.xxw.platform.plugin.scanner.api.holder;

import com.xxw.platform.frame.common.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除ip地址相关的ThreadLocalHolder
 *
 * @author liaoxiting
 * @date 2021/10/29 11:42
 */
@Component
public class IpAddrRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        IpAddrHolder.clear();
    }

}
