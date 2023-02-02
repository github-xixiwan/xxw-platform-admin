/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package com.xxw.platform.plugin.timer.business.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import com.xxw.platform.plugin.scanner.api.annotation.PostResource;
import com.xxw.platform.plugin.timer.business.entity.SysTimers;
import com.xxw.platform.plugin.timer.business.param.SysTimersParam;
import com.xxw.platform.plugin.timer.business.service.SysTimersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 定时任务控制器
 *
 * @author fengshuonan
 * @date 2020/10/27 14:30
 */
@RestController
@ApiResource(name = "定时任务管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysTimersController {

    @Resource
    private SysTimersService sysTimersService;

    /**
     * 添加定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    @PostResource(name = "添加定时任务", path = "/sysTimers/add")
    @BusinessLog
    public ResponseData<?> add(@RequestBody @Valid @Validated(SysTimersParam.add.class) SysTimersParam sysTimersParam) {
        sysTimersService.add(sysTimersParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    @PostResource(name = "删除定时任务", path = "/sysTimers/delete")
    @BusinessLog
    public ResponseData<?> del(@RequestBody @Validated(SysTimersParam.delete.class) SysTimersParam sysTimersParam) {
        sysTimersService.del(sysTimersParam);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    @PostResource(name = "编辑定时任务", path = "/sysTimers/edit")
    @BusinessLog
    public ResponseData<?> edit(@RequestBody @Validated(SysTimersParam.edit.class) SysTimersParam sysTimersParam) {
        sysTimersService.edit(sysTimersParam);
        return new SuccessResponseData<>();
    }

    /**
     * 启动定时任务
     *
     * @author stylefeng
     * @date 2020/7/1 14:34
     */
    @PostResource(name = "启动定时任务", path = "/sysTimers/start")
    @BusinessLog
    public ResponseData<?> start(@RequestBody @Validated(SysTimersParam.startTimer.class) SysTimersParam sysTimersParam) {
        sysTimersService.start(sysTimersParam);
        return new SuccessResponseData<>();
    }

    /**
     * 停止定时任务
     *
     * @author stylefeng
     * @date 2020/7/1 14:34
     */
    @PostResource(name = "停止定时任务", path = "/sysTimers/stop")
    @BusinessLog
    public ResponseData<?> stop(@RequestBody @Validated(SysTimersParam.stopTimer.class) SysTimersParam sysTimersParam) {
        sysTimersService.stop(sysTimersParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    @GetResource(name = "查看详情定时任务", path = "/sysTimers/detail")
    public ResponseData<SysTimers> detail(@Validated(SysTimersParam.detail.class) SysTimersParam sysTimersParam) {
        return new SuccessResponseData<>(sysTimersService.detail(sysTimersParam));
    }

    /**
     * 分页查询定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    @GetResource(name = "分页查询定时任务", path = "/sysTimers/page")
    public ResponseData<PageResult<SysTimers>> page(SysTimersParam sysTimersParam) {
        return new SuccessResponseData<>(sysTimersService.findPage(sysTimersParam));
    }

    /**
     * 获取全部定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    @GetResource(name = "获取全部定时任务", path = "/sysTimers/list")
    public ResponseData<List<SysTimers>> list(SysTimersParam sysTimersParam) {
        return new SuccessResponseData<>(sysTimersService.findList(sysTimersParam));
    }

    /**
     * 获取系统的所有任务列表
     *
     * @author stylefeng
     * @date 2020/7/1 14:34
     */
    @PostResource(name = "获取系统的所有任务列表", path = "/sysTimers/getActionClasses")
    public ResponseData<List<String>> getActionClasses() {
        List<String> actionClasses = sysTimersService.getActionClasses();
        return new SuccessResponseData<>(actionClasses);
    }

}
