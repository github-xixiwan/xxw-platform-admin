package com.xxw.platform.plugin.message.sdk.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.message.api.pojo.request.MessageRequest;
import com.xxw.platform.plugin.message.sdk.db.entity.SysMessage;

import java.util.List;

/**
 * 系统消息 service接口
 *
 * @author liaoxiting
 * @date 2020/12/31 20:09
 */
public interface SysMessageService extends IService<SysMessage> {

    /**
     * 新增
     *
     * @param messageRequest 参数对象
     * @author liaoxiting
     * @date 2021/2/2 20:48
     */
    void add(MessageRequest messageRequest);

    /**
     * 删除
     *
     * @param messageRequest 参数对象
     * @author liaoxiting
     * @date 2021/1/26 12:52
     */
    void del(MessageRequest messageRequest);

    /**
     * 修改
     *
     * @param messageRequest 参数对象
     * @author liaoxiting
     * @date 2021/2/2 20:48
     */
    void edit(MessageRequest messageRequest);

    /**
     * 查询-详情-根据主键id
     *
     * @param messageRequest 参数对象
     * @author liaoxiting
     * @date 2021/2/2 20:48
     */
    SysMessage detail(MessageRequest messageRequest);

    /**
     * 分页查询
     *
     * @param messageRequest 参数
     * @author liaoxiting
     * @date 2021/2/2 20:48
     */
    PageResult<SysMessage> findPage(MessageRequest messageRequest);

    /**
     * 列表查询
     *
     * @param messageRequest 参数
     * @author liaoxiting
     * @date 2021/1/8 15:21
     */
    List<SysMessage> findList(MessageRequest messageRequest);

    /**
     * 数量查询
     *
     * @param messageRequest 参数
     * @author liaoxiting
     * @date 2021/1/11 19:21
     */
    Integer findCount(MessageRequest messageRequest);

}
