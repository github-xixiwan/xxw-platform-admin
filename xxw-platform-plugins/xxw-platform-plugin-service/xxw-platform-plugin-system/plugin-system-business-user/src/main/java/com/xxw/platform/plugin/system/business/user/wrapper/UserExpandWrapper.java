package com.xxw.platform.plugin.system.business.user.wrapper;

import cn.hutool.extra.spring.SpringUtil;
import com.xxw.platform.plugin.expand.api.ExpandApi;
import com.xxw.platform.plugin.system.api.pojo.user.SysUserDTO;
import com.xxw.platform.plugin.wrapper.api.BaseWrapper;

import java.util.Map;

/**
 * 系统用户的拓展
 *
 * @author liaoxiting
 * @date 2022/4/1 11:30
 */
public class UserExpandWrapper implements BaseWrapper<SysUserDTO> {

    @Override
    public Map<String, Object> doWrap(SysUserDTO beWrappedModel) {
        ExpandApi expandApi = SpringUtil.getBean(ExpandApi.class);
        return expandApi.getExpandDataInfo("user_expand", beWrappedModel.getUserId());
    }

}
