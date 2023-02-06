package com.xxw.platform.plugin.system.business.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.StatusEnum;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.system.api.UserOrgServiceApi;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.organization.PositionExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.organization.HrPositionDTO;
import com.xxw.platform.plugin.system.api.pojo.organization.HrPositionRequest;
import com.xxw.platform.plugin.system.business.organization.entity.HrPosition;
import com.xxw.platform.plugin.system.business.organization.mapper.HrPositionMapper;
import com.xxw.platform.plugin.system.business.organization.service.HrPositionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统职位表 服务实现类
 *
 * @author liaoxiting
 * @date 2020/11/04 11:07
 */
@Service
public class HrPositionServiceImpl extends ServiceImpl<HrPositionMapper, HrPosition> implements HrPositionService {

    @Resource
    private UserOrgServiceApi userOrgServiceApi;

    @Override
    public void add(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = new HrPosition();
        BeanUtil.copyProperties(hrPositionRequest, sysPosition);

        // 设置状态为启用
        sysPosition.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(sysPosition);
    }

    @Override
    public void del(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = this.querySysPositionById(hrPositionRequest);

        // 该职位下是否有员工，如果有将不能删除
        Boolean userOrgFlag = userOrgServiceApi.getUserOrgFlag(null, sysPosition.getPositionId());
        if (userOrgFlag) {
            throw new SystemModularException(PositionExceptionEnum.CANT_DELETE_POSITION);
        }

        // 逻辑删除
        sysPosition.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(sysPosition);
    }

    @Override
    public void edit(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = this.querySysPositionById(hrPositionRequest);
        BeanUtil.copyProperties(hrPositionRequest, sysPosition);
        this.updateById(sysPosition);
    }

    @Override
    public void changeStatus(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = this.querySysPositionById(hrPositionRequest);
        sysPosition.setStatusFlag(hrPositionRequest.getStatusFlag());
        this.updateById(sysPosition);
    }

    @Override
    public HrPosition detail(HrPositionRequest hrPositionRequest) {
        return this.querySysPositionById(hrPositionRequest);
    }

    @Override
    public List<HrPosition> findList(HrPositionRequest hrPositionRequest) {
        return this.list(this.createWrapper(hrPositionRequest));
    }

    @Override
    public PageResult<HrPosition> findPage(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> wrapper = this.createWrapper(hrPositionRequest);

        Page<HrPosition> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public void batchDel(HrPositionRequest hrPositionRequest) {
        List<Long> positionIds = hrPositionRequest.getPositionIds();
        for (Long userId : positionIds) {
            HrPositionRequest tempRequest = new HrPositionRequest();
            tempRequest.setPositionId(userId);
            this.del(tempRequest);
        }
    }

    @Override
    public Integer positionNum() {
        return Convert.toInt(this.count());
    }

    @Override
    public HrPositionDTO getPositionDetail(Long positionId) {
        HrPositionDTO hrPositionDTO = new HrPositionDTO();
        HrPositionRequest request = new HrPositionRequest();
        request.setPositionId(positionId);
        HrPosition detail = this.detail(request);
        if (ObjectUtil.isNotNull(detail)) {
            BeanUtil.copyProperties(detail, hrPositionDTO, CopyOptions.create().ignoreError());
        }
        return hrPositionDTO;
    }

    @Override
    public List<HrPositionDTO> getPositionDetailList(List<Long> positionIdList) {
        ArrayList<HrPositionDTO> hrPositionDTOS = new ArrayList<>();
        for (Long positionId : positionIdList) {
            HrPositionDTO positionDetail = this.getPositionDetail(positionId);
            hrPositionDTOS.add(positionDetail);
        }
        return hrPositionDTOS;
    }

    /**
     * 根据主键id获取对象信息
     *
     * @return 实体对象
     * @author liaoxiting
     * @date 2021/2/2 10:16
     */
    private HrPosition querySysPositionById(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.getById(hrPositionRequest.getPositionId());
        if (ObjectUtil.isEmpty(hrPosition) || YesOrNotEnum.Y.getCode().equals(hrPosition.getDelFlag())) {
            throw new SystemModularException(PositionExceptionEnum.CANT_FIND_POSITION, hrPositionRequest.getPositionId());
        }
        return hrPosition;
    }
    /**
     * 实体构建 QueryWrapper
     *
     * @author liaoxiting
     * @date 2021/2/2 10:17
     */
    private LambdaQueryWrapper<HrPosition> createWrapper(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> queryWrapper = new LambdaQueryWrapper<>();

        Long positionId = hrPositionRequest.getPositionId();
        String positionName = hrPositionRequest.getPositionName();
        String positionCode = hrPositionRequest.getPositionCode();
        Integer statusFlag = hrPositionRequest.getStatusFlag();

        // SQL条件拼接
        queryWrapper.eq(ObjectUtil.isNotNull(positionId), HrPosition::getPositionId, positionId);
        queryWrapper.like(StrUtil.isNotEmpty(positionName), HrPosition::getPositionName, positionName);
        queryWrapper.eq(StrUtil.isNotEmpty(positionCode), HrPosition::getPositionCode, positionCode);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), HrPosition::getStatusFlag, statusFlag);

        // 查询未删除状态的
        queryWrapper.eq(HrPosition::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列
        queryWrapper.orderByAsc(HrPosition::getPositionSort);

        return queryWrapper;
    }

}
