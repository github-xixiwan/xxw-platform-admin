package com.xxw.platform.plugin.group.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxw.platform.plugin.group.api.pojo.SysGroupDTO;
import com.xxw.platform.plugin.group.business.entity.SysGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务分组 Mapper 接口
 *
 * @author fengshuonan
 * @date 2022/05/11 12:54
 */
public interface SysGroupMapper extends BaseMapper<SysGroup> {

    /**
     * 获取用户分组列表
     *
     * @author fengshuonan
     * @date 2022/5/11 16:49
     */
    List<SysGroupDTO> getUserGroupList(@Param("groupBizCode") String groupBizCode, @Param("userId") Long userId, @Param("getTotal") boolean getTotal);

}
