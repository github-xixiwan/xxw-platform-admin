package com.xxw.platform.plugin.system.business.organization.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.system.api.pojo.organization.HrPositionDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrPositionRequest;
import com.xxw.platform.plugin.system.business.organization.entity.HrPosition;
import com.xxw.platform.plugin.system.business.organization.service.HrPositionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统职位控制器
 *
 * @author liaoxiting
 * @date 2020/11/18 21:56
 */
@RestController
@ApiResource(name = "系统职位管理", resBizType = ResBizTypeEnum.SYSTEM)
public class HrPositionController {

    @Resource
    private HrPositionService hrPositionService;

    /**
     * 添加系统职位
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @PostResource(name = "添加系统职位", path = "/hrPosition/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Validated(HrPositionRequest.add.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.add(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统职位
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @PostResource(name = "删除系统职位", path = "/hrPosition/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(HrPositionRequest.delete.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.del(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除系统职位
     *
     * @author liaoxiting
     * @date 2021/4/8 13:50
     */
    @PostResource(name = "批量删除系统职位", path = "/hrPosition/batchDelete")
    @BusinessLog
    public ResponseData<?> batchDelete(@RequestBody @Validated(HrPositionRequest.batchDelete.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.batchDel(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统职位
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @PostResource(name = "编辑系统职位", path = "/hrPosition/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(HrPositionRequest.edit.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.edit(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 更新职位状态
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @PostResource(name = "更新职位状态", path = "/hrPosition/updateStatus")
    @BusinessLog
    public ResponseData<?> updateStatus(@RequestBody @Validated(BaseRequest.updateStatus.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.changeStatus(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情系统职位
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @GetResource(name = "查看详情系统职位", path = "/hrPosition/detail")
    public ResponseData<HrPosition> detail(@Validated(HrPositionRequest.detail.class) HrPositionRequest hrPositionRequest) {
        return new SuccessResponseData<>(hrPositionService.detail(hrPositionRequest));
    }

    /**
     * 分页查询系统职位
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @GetResource(name = "分页查询系统职位", path = "/hrPosition/page")
    public ResponseData<PageResult<HrPosition>> page(HrPositionRequest hrPositionRequest) {
        return new SuccessResponseData<>(hrPositionService.findPage(hrPositionRequest));
    }

    /**
     * 获取全部系统职位
     *
     * @author liaoxiting
     * @date 2020/11/04 11:07
     */
    @GetResource(name = "获取全部系统职位", path = "/hrPosition/list")
    public ResponseData<List<HrPosition>> list(HrPositionRequest hrPositionRequest) {
        return new SuccessResponseData<>(hrPositionService.findList(hrPositionRequest));
    }

    /**
     * 获取岗位集合列表，通过岗位id集合
     *
     * @author liaoxiting
     * @date 2022/11/5 15:27
     */
    @PostResource(name = "获取岗位集合列表，通过岗位id集合", path = "/hrPosition/getPositionListByIds")
    public ResponseData<List<HrPositionDTO>> getPositionListByIds(@RequestBody @Validated(HrPositionRequest.batchQuery.class) HrPositionRequest hrPositionRequest) {
        return new SuccessResponseData<>(hrPositionService.getPositionDetailList(hrPositionRequest.getPositionIds()));
    }

}
