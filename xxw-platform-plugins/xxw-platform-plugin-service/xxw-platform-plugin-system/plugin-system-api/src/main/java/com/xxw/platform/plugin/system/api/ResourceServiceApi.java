package com.xxw.platform.plugin.system.api;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceDefinition;
import com.xxw.platform.plugin.scanner.api.pojo.resource.ResourceUrlParam;
import com.xxw.platform.plugin.system.api.pojo.role.dto.SysRoleResourceDTO;

import java.util.List;
import java.util.Set;

/**
 * 资源服务相关接口
 *
 * @author liaoxiting
 * @date 2020/12/3 15:45
 */
public interface ResourceServiceApi {

    /**
     * 通过url获取资源
     *
     * @param resourceUrlReq 资源url的封装
     * @return 资源详情
     * @author liaoxiting
     * @date 2020/10/19 22:06
     */
    ResourceDefinition getResourceByUrl(ResourceUrlParam resourceUrlReq);

    /**
     * 获取资源的url列表，根据资源code集合查询
     *
     * @param resourceCodes 资源编码集合
     * @return 资源url列表
     * @author liaoxiting
     * @date 2020/11/29 19:49
     */
    Set<String> getResourceUrlsListByCodes(Set<String> resourceCodes);

    /**
     * 获取当前资源url的数量
     *
     * @author liaoxiting
     * @date 2021/11/3 15:11
     */
    Integer getResourceCount();

    /**
     * 根据业务类型，获取所有的资源
     *
     * @param resBizTypeEnum 资源类型
     * @return 所有资源的编码集合
     * @author liaoxiting
     * @date 2022/9/29 14:27
     */
    List<SysRoleResourceDTO> getTotalResourceCode(ResBizTypeEnum resBizTypeEnum);

    /**
     * 获取资源编码对应的资源类型
     *
     * @author liaoxiting
     * @date 2022/10/1 14:29
     */
    Integer getResourceBizTypeByCode(String resCode);

}
