package com.xxw.platform.plugin.system.business.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.plugin.system.api.enums.DetectModeEnum;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrgApprover;
import com.xxw.platform.plugin.system.business.organization.request.HrOrgApproverRequest;

import java.util.List;

/**
 * 组织机构审批人 服务类
 *
 * @author liaoxiting
 * @date 2022/09/13 23:15
 */
public interface HrOrgApproverService extends IService<HrOrgApprover> {

    /**
     * 新增
     *
     * @param hrOrgApproverRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    void bindUserList(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 删除
     *
     * @param hrOrgApproverRequest 请求参数
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    void del(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 查询详情
     *
     * @param hrOrgApproverRequest 请求参数
     * @return 查询到的组织机构负责人情况
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    HrOrgApprover detail(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 获取组织机构审批员绑定信息
     *
     * @param hrOrgApproverRequest 请求参数
     * @return List<HrOrgApprover>   返回结果
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    List<HrOrgApprover> getBindingList(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 获取指定用户的部门负责人
     *
     * @param userId          指定用户的部门负责人
     * @param orgApproverType 部门负责人类型
     * @param parentLevel     从0开始，0为获取指定用户同部门的领导，1为上一级部门的领导，以此类推
     * @param detectModeEnum  指定查找的模式，自上而下找对应的负责人，还是自下而上
     * @return 用户的部门负责人id集合
     * @author liaoxiting
     * @date 2022/9/18 14:52
     */
    List<Long> getUserOrgApprover(Long userId, Integer orgApproverType, Integer parentLevel, DetectModeEnum detectModeEnum);

    /**
     * 获取指定部门的部门负责人
     *
     * @param deptId          指定部门id
     * @param orgApproverType 部门负责人类型
     * @param parentLevel     从0开始，0为获取指定同部门的领导，1为上一级部门的领导，以此类推
     * @param detectModeEnum  指定查找的模式，自上而下找对应的负责人，还是自下而上
     * @return 负责人id集合
     * @author liaoxiting
     * @date 2022/9/18 14:52
     */
    List<Long> getDeptOrgApprover(Long deptId, Integer orgApproverType, Integer parentLevel, DetectModeEnum detectModeEnum);

    /**
     * 获取所有审批人类型列表
     *
     * @return 审批人类型列表
     * @author liaoxiting
     * @date 2022/9/26 10:18
     */
    List<SimpleDict> getApproverTypeList();

}