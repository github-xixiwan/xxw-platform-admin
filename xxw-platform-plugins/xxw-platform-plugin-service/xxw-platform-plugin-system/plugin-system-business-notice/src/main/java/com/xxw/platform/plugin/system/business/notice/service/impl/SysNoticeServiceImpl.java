package com.xxw.platform.plugin.system.business.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxw.platform.frame.common.enums.YesOrNotEnum;
import com.xxw.platform.plugin.db.api.factory.PageFactory;
import com.xxw.platform.plugin.db.api.factory.PageResultFactory;
import com.xxw.platform.plugin.db.api.pojo.entity.BaseEntity;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.message.api.MessageApi;
import com.xxw.platform.plugin.message.api.enums.MessageBusinessTypeEnum;
import com.xxw.platform.plugin.message.api.pojo.request.MessageSendRequest;
import com.xxw.platform.plugin.system.api.exception.SystemModularException;
import com.xxw.platform.plugin.system.api.exception.enums.notice.NoticeExceptionEnum;
import com.xxw.platform.plugin.system.api.pojo.notice.SysNoticeRequest;
import com.xxw.platform.plugin.system.business.notice.entity.SysNotice;
import com.xxw.platform.plugin.system.business.notice.mapper.SysNoticeMapper;
import com.xxw.platform.plugin.system.business.notice.service.SysNoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 通知表 服务实现类
 *
 * @author liaoxiting
 * @date 2021/1/8 22:45
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    private static final String NOTICE_SCOPE_ALL = "all";

    @Resource
    private MessageApi messageApi;

    @Override
    public void add(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = new SysNotice();

        // 拷贝属性
        BeanUtil.copyProperties(sysNoticeRequest, sysNotice);

        // 没传递通知范围，则默认发给所有人
        if (StrUtil.isBlank(sysNotice.getNoticeScope())) {
            sysNotice.setNoticeScope(NOTICE_SCOPE_ALL);
        }

        // 如果保存成功调用发送消息
        if (this.save(sysNotice)) {
            sendMessage(sysNotice);
        }
    }

    @Override
    public void del(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.querySysNoticeById(sysNoticeRequest);
        // 逻辑删除
        sysNotice.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(sysNotice);
    }

    @Override
    public void edit(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.querySysNoticeById(sysNoticeRequest);

        // 通知范围不允许修改， 如果通知范围不同抛出异常
        if (!sysNoticeRequest.getNoticeScope().equals(sysNotice.getNoticeScope())) {
            throw new SystemModularException(NoticeExceptionEnum.NOTICE_SCOPE_NOT_EDIT);
        }

        // 获取通知范围，如果为空则设置为all
        String noticeScope = sysNotice.getNoticeScope();
        if (StrUtil.isBlank(noticeScope)) {
            sysNoticeRequest.setNoticeScope(NOTICE_SCOPE_ALL);
        }

        // 更新属性
        BeanUtil.copyProperties(sysNoticeRequest, sysNotice);

        // 修改成功后发送信息
        if (this.updateById(sysNotice)) {
            sendMessage(sysNotice);
        }
    }

    @Override
    public SysNotice detail(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> queryWrapper = this.createWrapper(sysNoticeRequest);
        return this.getOne(queryWrapper, false);
    }

    @Override
    public PageResult<SysNotice> findPage(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> wrapper = createWrapper(sysNoticeRequest);
        Page<SysNotice> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public List<SysNotice> findList(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> wrapper = createWrapper(sysNoticeRequest);
        return this.list(wrapper);
    }

    /**
     * 获取通知管理
     *
     * @author liaoxiting
     * @date 2021/1/9 16:56
     */
    private SysNotice querySysNoticeById(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.getById(sysNoticeRequest.getNoticeId());
        if (ObjectUtil.isNull(sysNotice)) {
            throw new SystemModularException(NoticeExceptionEnum.NOTICE_NOT_EXIST, sysNoticeRequest.getNoticeId());
        }
        return sysNotice;
    }

    /**
     * 创建wrapper
     *
     * @author liaoxiting
     * @date 2020/1/9 16:16
     */
    private LambdaQueryWrapper<SysNotice> createWrapper(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();

        // 按时间倒序排列
        queryWrapper.orderByDesc(BaseEntity::getCreateTime);

        // 查询未删除状态的
        queryWrapper.eq(SysNotice::getDelFlag, YesOrNotEnum.N.getCode());

        if (ObjectUtil.isEmpty(sysNoticeRequest)) {
            return queryWrapper;
        }

        // 通知id
        Long noticeId = sysNoticeRequest.getNoticeId();

        // 通知标题
        String noticeTitle = sysNoticeRequest.getNoticeTitle();

        // 拼接sql 条件
        queryWrapper.like(ObjectUtil.isNotEmpty(noticeTitle), SysNotice::getNoticeTitle, noticeTitle);
        queryWrapper.eq(ObjectUtil.isNotEmpty(noticeId), SysNotice::getNoticeId, noticeId);

        return queryWrapper;
    }

    /**
     * 发送消息
     *
     * @author liaoxiting
     * @date 2021/2/8 19:30
     */
    private void sendMessage(SysNotice sysNotice) {
        MessageSendRequest message = new MessageSendRequest();

        // 消息标题
        message.setMessageTitle(sysNotice.getNoticeTitle());

        // 消息内容
        message.setMessageContent(sysNotice.getNoticeContent());

        // 消息优先级
        message.setPriorityLevel(sysNotice.getPriorityLevel());

        // 消息发送范围
        message.setReceiveUserIds(sysNotice.getNoticeScope());

        // 消息业务类型
        message.setBusinessType(MessageBusinessTypeEnum.SYS_NOTICE.getCode());
        message.setBusinessTypeValue(MessageBusinessTypeEnum.SYS_NOTICE.getName());

        message.setBusinessId(sysNotice.getNoticeId());
        message.setMessageSendTime(new Date());

        try {
            messageApi.sendMessage(message);
        } catch (Exception exception) {
            // 发送失败打印异常
            log.error("发送消息失败:", exception);
        }
    }

}
