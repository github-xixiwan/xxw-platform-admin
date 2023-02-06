package com.xxw.platform.plugin.system.business.organization.wrapper;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.expand.api.ExpandApi;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrganization;
import com.xxw.platform.plugin.wrapper.api.BaseWrapper;

import java.util.Map;

/**
 * 组织机构的拓展
 *
 * @author liaoxiting
 * @date 2022/09/01 18:10
 */
public class OrgExpandWrapper implements BaseWrapper<HrOrganization> {

    @Override
    public Map<String, Object> doWrap(HrOrganization beWrappedModel) {
        ExpandApi expandApi = SpringUtil.getBean(ExpandApi.class);
        return expandApi.getExpandDataInfo("org_expand", beWrappedModel.getOrgId());
    }

}
