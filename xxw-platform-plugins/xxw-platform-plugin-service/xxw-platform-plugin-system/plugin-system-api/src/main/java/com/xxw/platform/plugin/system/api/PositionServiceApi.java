package com.xxw.platform.plugin.system.api;

import com.xxw.platform.plugin.system.api.pojo.organization.HrPositionDTO;

import java.util.List;

/**
 * 职位api
 *
 * @author linjinfeng
 * @date 2021/3/24 17:20
 */
public interface PositionServiceApi {

    /**
     * 查询职位总数
     *
     * @author liaoxiting
     * @date 2022/2/9 9:37
     */
    Integer positionNum();

    /**
     * 获取职位详情
     *
     * @author yexing
     * @date 2022/3/8 23:33
     */
    HrPositionDTO getPositionDetail(Long positionId);

    /**
     * 获取职位详情列表
     *
     * @author liaoxiting
     * @date 2022/10/31 20:33
     */
    List<HrPositionDTO> getPositionDetailList(List<Long> positionIdList);

}
