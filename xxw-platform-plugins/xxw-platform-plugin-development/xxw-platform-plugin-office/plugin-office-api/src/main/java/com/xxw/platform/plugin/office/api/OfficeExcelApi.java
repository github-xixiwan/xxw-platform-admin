package com.xxw.platform.plugin.office.api;

import com.xxw.platform.plugin.office.api.report.ExcelExportParam;

import java.io.InputStream;
import java.util.List;

/**
 * Excel 常用操作接口
 *
 * @author liaoxiting
 * @date 2020/11/3 16:42
 */
public interface OfficeExcelApi {

    /**
     * 简单的导出Excel下载
     *
     * @param excelExportParam Excel导出参数
     * @author liaoxiting
     * @date 2020/11/4 10:11
     */
    void easyExportDownload(ExcelExportParam excelExportParam);

    /**
     * 简单的写入Excel文件到指定路径
     *
     * @param excelExportParam Excel导出参数
     * @author liaoxiting
     * @date 2020/11/4 11:31
     */
    void easyWriteToFile(ExcelExportParam excelExportParam);

    /**
     * 简单的读取Excel文件并返回实体类List集合
     *
     * @param inputStream 流输入Excel文件的流对象
     * @param clazz       每行数据转换成的对象类
     * @return 对象类List集合
     * @author liaoxiting
     * @date 2020/11/4 13:54
     */
    <T> List<T> easyReadToList(InputStream inputStream, Class<T> clazz);

    /**
     * 简单的读取Excel文件并返回实体类List集合-针对多行表头
     *
     * @param inputStream 流输入Excel文件的流对象
     * @param clazz       每行数据转换成的对象类
     * @param rowNum      表头所占行数
     * @return 对象类List集合
     * @author liaoxiting
     * @date 2022/8/11 10:37
     */
    <T> List<T> easyReadToList(InputStream inputStream, Integer rowNum, Class<T> clazz);

}
