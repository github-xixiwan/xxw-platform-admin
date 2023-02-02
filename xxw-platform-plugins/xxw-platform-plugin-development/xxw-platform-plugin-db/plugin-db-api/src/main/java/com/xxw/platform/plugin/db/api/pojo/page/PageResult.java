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
package com.xxw.platform.plugin.db.api.pojo.page;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 *
 * @author fengshuonan
 * @date 2020/10/15 15:53
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 第几页
     */
    @ChineseDescription("第几页,从1开始")
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    @ChineseDescription("每页条数")
    private Integer pageSize = 20;

    /**
     * 总页数
     */
    @ChineseDescription("总页数")
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    @ChineseDescription("总记录数")
    private Integer totalRows = 0;

    /**
     * 结果集
     */
    @ChineseDescription("结果集")
    private List<T> rows;

}