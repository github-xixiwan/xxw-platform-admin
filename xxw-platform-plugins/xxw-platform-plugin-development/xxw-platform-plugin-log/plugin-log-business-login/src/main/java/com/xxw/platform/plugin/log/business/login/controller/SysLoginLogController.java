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
package com.xxw.platform.plugin.log.business.login.controller;

import com.xxw.platform.frame.common.annotation.BusinessLog;
import com.xxw.platform.frame.common.enums.ResBizTypeEnum;
import com.xxw.platform.frame.common.pojo.response.ResponseData;
import com.xxw.platform.frame.common.pojo.response.SuccessResponseData;
import com.xxw.platform.plugin.db.api.pojo.page.PageResult;
import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogDto;
import com.xxw.platform.plugin.log.api.pojo.loginlog.SysLoginLogRequest;
import com.xxw.platform.plugin.log.business.login.entity.SysLoginLog;
import com.xxw.platform.plugin.log.business.login.service.SysLoginLogService;
import com.xxw.platform.plugin.scanner.api.annotation.ApiResource;
import com.xxw.platform.plugin.scanner.api.annotation.GetResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登陆日志控制器
 *
 * @author chenjinlong
 * @date 2021/1/13 17:51
 */
@RestController
@ApiResource(name = "登录日志", resBizType = ResBizTypeEnum.SYSTEM)
public class SysLoginLogController {

    @Resource
    private SysLoginLogService sysLoginLogService;

    /**
     * 清空登录日志
     *
     * @author chenjinlong
     * @date 2021/1/13 17:51
     */
    @GetResource(name = "清空登录日志", path = "/loginLog/deleteAll")
    @BusinessLog
    public ResponseData<?> deleteAll() {
        sysLoginLogService.delAll();
        return new SuccessResponseData<>();
    }

    /**
     * 查询登录日志详情
     *
     * @author chenjinlong
     * @date 2021/1/13 17:51
     */
    @GetResource(name = "查看详情登录日志", path = "/loginLog/detail")
    public ResponseData<SysLoginLog> detail(@Validated(SysLoginLogRequest.detail.class) SysLoginLogRequest sysLoginLogRequest) {
        return new SuccessResponseData<>(sysLoginLogService.detail(sysLoginLogRequest));
    }

    /**
     * 分页查询登录日志
     *
     * @author chenjinlong
     * @date 2021/1/13 17:51
     */
    @GetResource(name = "分页查询登录日志", path = "/loginLog/page")
    public ResponseData<PageResult<SysLoginLogDto>> page(SysLoginLogRequest sysLoginLogRequest) {
        return new SuccessResponseData<>(sysLoginLogService.findPage(sysLoginLogRequest));
    }

}
