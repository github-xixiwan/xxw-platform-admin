package com.xxw.platform.plugin.db.api.factory;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import com.xxw.platform.frame.common.util.HttpServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页参数快速获取
 *
 * @author liaoxiting
 * @date 2020/10/17 17:33
 */
public class PageFactory {

    /**
     * 每页大小（默认20）
     */
    private static final String PAGE_SIZE_PARAM_NAME = "pageSize";

    /**
     * 第几页（从1开始）
     */
    private static final String PAGE_NO_PARAM_NAME = "pageNo";

    /**
     * 默认分页，在使用时PageFactory.defaultPage会自动获取pageSize和pageNo参数
     *
     * @author liaoxiting
     * @date 2020/3/30 16:42
     */
    public static <T> Page<T> defaultPage() {

        int pageSize = 20;
        int pageNo = 1;

        HttpServletRequest request = HttpServletUtil.getRequest();

        //每页条数
        String pageSizeString = request.getParameter(PAGE_SIZE_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageSizeString)) {
            pageSize = Integer.parseInt(pageSizeString);
        }

        //第几页
        String pageNoString = request.getParameter(PAGE_NO_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageNoString)) {
            pageNo = Integer.parseInt(pageNoString);
        }

        return new Page<>(pageNo, pageSize);
    }

    /**
     * 从baseRequest中获取分页参数
     *
     * @author liaoxiting
     * @date 2021/10/19 16:05
     */
    public static <T> Page<T> defaultPage(BaseRequest baseRequest) {
        int pageSize = 20;
        int pageNo = 1;

        if (ObjectUtil.isNotEmpty(baseRequest)) {
            pageNo = baseRequest.getPageNo() == null ? pageNo : baseRequest.getPageNo();
            pageSize = baseRequest.getPageSize() == null ? pageNo : baseRequest.getPageSize();
        }
        return new Page<>(pageNo, pageSize);
    }

}
