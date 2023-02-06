package com.xxw.platform.plugin.system.business.organization.controller;

import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.dict.SimpleDict;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.business.organization.entity.HrOrgApprover;
import com.xxw.platform.plugin.system.business.organization.request.HrOrgApproverRequest;
import com.xxw.platform.plugin.system.business.organization.service.HrOrgApproverService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组织机构审批人控制器
 *
 * @author liaoxiting
 * @date 2022/09/13 23:15
 */
@RestController
@ApiResource(name = "组织机构审批人", resBizType = ResBizTypeEnum.SYSTEM)
public class HrOrgApproverController {

    @Resource
    private HrOrgApproverService hrOrgApproverService;

    /**
     * 获取组织机构审批人绑定列表
     *
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    @GetResource(name = "获取组织机构审批人绑定列表", path = "/hrOrgApprover/getBindingList")
    public ResponseData<List<HrOrgApprover>> getBindingList(@Validated(HrOrgApproverRequest.list.class) HrOrgApproverRequest hrOrgApproverRequest) {
        return new SuccessResponseData<>(hrOrgApproverService.getBindingList(hrOrgApproverRequest));
    }

    /**
     * 更新组织机构绑定审批人
     *
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    @PostResource(name = "更新组织机构绑定审批人", path = "/hrOrgApprover/bindUserList")
    public ResponseData<HrOrgApprover> bindUserList(@RequestBody @Validated(HrOrgApproverRequest.add.class) HrOrgApproverRequest hrOrgApproverRequest) {
        hrOrgApproverService.bindUserList(hrOrgApproverRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除绑定审批人
     *
     * @author liaoxiting
     * @date 2022/09/13 23:15
     */
    @PostResource(name = "删除绑定审批人", path = "/hrOrgApprover/delete")
    public ResponseData<?> delete(@RequestBody @Validated(HrOrgApproverRequest.delete.class) HrOrgApproverRequest hrOrgApproverRequest) {
        hrOrgApproverService.del(hrOrgApproverRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取组织机构审批人类型列表
     *
     * @author liaoxiting
     * @date 2022/9/26 10:44
     */
    @GetResource(name = "获取组织机构审批人类型列表", path = "/hrOrgApprover/getApproverTypeList")
    public ResponseData<List<SimpleDict>> getApproverTypeList() {
        return new SuccessResponseData<>(hrOrgApproverService.getApproverTypeList());
    }

}
