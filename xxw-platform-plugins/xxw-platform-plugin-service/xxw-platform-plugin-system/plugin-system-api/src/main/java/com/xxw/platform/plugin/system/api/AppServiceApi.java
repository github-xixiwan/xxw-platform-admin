package com.xxw.platform.plugin.system.api;

import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.system.api.pojo.app.SysAppResult;

import java.util.List;
import java.util.Set;

/**
 * 应用相关api
 *
 * @author liaoxiting
 * @date 2020/11/24 21:37
 */
public interface AppServiceApi {

    /**
     * 通过app编码获取app的详情
     *
     * @param appCodes 应用的编码
     * @return 应用的信息
     * @author liaoxiting
     * @date 2020/11/29 20:06
     */
    Set<SimpleDict> getAppsByAppCodes(Set<String> appCodes);

    /**
     * 通过app编码获取app的中文名
     *
     * @param appCode 应用的编码
     * @return 应用的中文名
     * @author liaoxiting
     * @date 2020/11/29 20:06
     */
    String getAppNameByAppCode(String appCode);

    /**
     * 获取当前激活的应用编码
     *
     * @return 激活的应用编码
     * @author liaoxiting
     * @date 2021/1/8 19:01
     */
    String getActiveAppCode();

    /**
     * 获取应用信息详情
     *
     * @author liaoxiting
     * @date 2021/8/24 20:12
     */
    SysAppResult getAppInfoByAppCode(String appCode);

    /**
     * 按顺序获取app的编码和名称
     *
     * @author liaoxiting
     * @date 2022/4/6 22:34
     */
    List<SysAppResult> getSortedApps();

    /**
     * 按顺序获取app的编码和名称
     *
     * @param devopsFlag 是否包含运维平台的应用
     * @author liaoxiting
     * @date 2022/4/6 22:34
     */
    List<SysAppResult> getSortedApps(Boolean devopsFlag);

}
