package com.xxw.platform.plugin.log.sdk.db;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.LogManagerApi;
import com.xxw.platform.plugin.log.api.pojo.manage.LogManagerRequest;
import com.xxw.platform.plugin.log.api.pojo.record.LogRecordDTO;
import com.xxw.platform.plugin.log.sdk.db.entity.SysLog;
import com.xxw.platform.plugin.log.sdk.db.service.SysLogService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志管理，数据库实现
 *
 * @author liaoxiting
 * @date 2020/11/2 17:40
 */
@Slf4j
public class DbLogManagerServiceImpl implements LogManagerApi {

    @Resource
    private SysLogService sysLogService;

    @Override
    public List<LogRecordDTO> findList(LogManagerRequest logManagerRequest) {
        List<SysLog> sysLogList = this.sysLogService.findList(logManagerRequest);
        List<LogRecordDTO> logRecordDTOList = CollUtil.newArrayList();
        BeanUtil.copyProperties(sysLogList, logRecordDTOList);
        return logRecordDTOList;
    }

    @Override
    public PageResult<LogRecordDTO> findPage(LogManagerRequest logManagerRequest) {
        PageResult<SysLog> sysLogPageResult = this.sysLogService.findPage(logManagerRequest);

        // 分页类型转换
        PageResult<LogRecordDTO> logRecordDTOPageResult = new PageResult<>();
        BeanUtil.copyProperties(sysLogPageResult, logRecordDTOPageResult);

        // 转化数组
        List<SysLog> rows = sysLogPageResult.getRows();
        ArrayList<LogRecordDTO> newRows = new ArrayList<>();
        for (SysLog row : rows) {
            LogRecordDTO logRecordDTO = new LogRecordDTO();
            BeanUtil.copyProperties(row, logRecordDTO);

            // 设置请求和响应为空，减少带宽开销
            logRecordDTO.setRequestResult(null);
            logRecordDTO.setRequestResult(null);
            newRows.add(logRecordDTO);
        }
        logRecordDTOPageResult.setRows(newRows);

        return logRecordDTOPageResult;
    }

    @Override
    public void del(LogManagerRequest logManagerRequest) {
        this.sysLogService.del(logManagerRequest);
    }

    @Override
    public LogRecordDTO detail(LogManagerRequest logManagerRequest) {
        SysLog detail = this.sysLogService.detail(logManagerRequest);
        LogRecordDTO logRecordDTO = new LogRecordDTO();
        BeanUtil.copyProperties(detail, logRecordDTO);
        return logRecordDTO;
    }

}
