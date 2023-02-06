package com.xxw.platform.plugin.message.business;

import cn.hutool.core.util.StrUtil;
import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.message.api.MessageApi;
import com.xxw.platform.plugin.message.api.enums.MessageReadFlagEnum;
import com.xxw.platform.plugin.message.api.pojo.request.MessageRequest;
import com.xxw.platform.plugin.message.api.pojo.request.MessageSendRequest;
import com.xxw.platform.plugin.message.api.pojo.response.MessageResponse;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 系统消息控制器
 *
 * @author liaoxiting
 * @date 2021/1/1 22:30
 */
@RestController
@ApiResource(name = "系统消息控制器", resBizType = ResBizTypeEnum.SYSTEM)
public class SysMessageController {

    /**
     * 系统消息api
     */
    @Resource
    private MessageApi messageApi;

    /**
     * 发送系统消息
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @PostResource(name = "发送系统消息", path = "/sysMessage/sendMessage")
    @BusinessLog
    public ResponseData<?> sendMessage(@RequestBody @Validated(MessageSendRequest.add.class) MessageSendRequest messageSendRequest) {
        messageSendRequest.setMessageSendTime(new Date());
        messageApi.sendMessage(messageSendRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量更新系统消息状态
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @PostResource(name = "批量更新系统消息状态", path = "/sysMessage/batchUpdateReadFlag")
    @BusinessLog
    public ResponseData<?> batchUpdateReadFlag(@RequestBody @Validated(MessageRequest.updateReadFlag.class) MessageRequest messageRequest) {
        List<Long> messageIdList = messageRequest.getMessageIdList();
        messageApi.batchReadFlagByMessageIds(StrUtil.join(",", messageIdList), MessageReadFlagEnum.READ);
        return new SuccessResponseData<>();
    }

    /**
     * 系统消息全部修改已读
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @GetResource(name = "系统消息全部修改已读", path = "/sysMessage/allMessageReadFlag")
    public ResponseData<?> allMessageReadFlag() {
        messageApi.allMessageReadFlag();
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统消息
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @PostResource(name = "删除系统消息", path = "/sysMessage/delete")
    @BusinessLog
    public ResponseData<?> delete(@RequestBody @Validated(MessageRequest.delete.class) MessageRequest messageRequest) {
        messageApi.deleteByMessageId(messageRequest.getMessageId());
        return new SuccessResponseData<>();
    }

    /**
     * 查看系统消息
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @GetResource(name = "查看系统消息", path = "/sysMessage/detail")
    public ResponseData<MessageResponse> detail(@Validated(MessageRequest.detail.class) MessageRequest messageRequest) {
        return new SuccessResponseData<>(messageApi.messageDetail(messageRequest));
    }

    /**
     * 分页查询系统消息列表
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @GetResource(name = "分页查询系统消息列表", path = "/sysMessage/page")
    public ResponseData<PageResult<MessageResponse>> page(MessageRequest messageRequest) {
        return new SuccessResponseData<>(messageApi.queryPageCurrentUser(messageRequest));
    }

    /**
     * 系统消息列表
     *
     * @author liaoxiting
     * @date 2021/1/8 13:50
     */
    @GetResource(name = "系统消息列表", path = "/sysMessage/list")
    public ResponseData<List<MessageResponse>> list(MessageRequest messageRequest) {
        return new SuccessResponseData<>(messageApi.queryListCurrentUser(messageRequest));
    }

    /**
     * 查询所有未读系统消息列表
     *
     * @author liaoxiting
     * @date 2021/6/12 17:42
     */
    @GetResource(name = "查询所有未读系统消息列表", path = "/sysMessage/unReadMessageList", requiredPermission = false)
    public ResponseData<List<MessageResponse>> unReadMessageList(MessageRequest messageRequest) {
        messageRequest.setReadFlag(MessageReadFlagEnum.UNREAD.getCode());
        List<MessageResponse> messageResponses = messageApi.queryListCurrentUser(messageRequest);
        return new SuccessResponseData<>(messageResponses);
    }

}
