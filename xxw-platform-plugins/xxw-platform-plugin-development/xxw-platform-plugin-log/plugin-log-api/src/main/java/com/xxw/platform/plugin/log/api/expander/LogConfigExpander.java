package com.xxw.platform.plugin.log.api.expander;
import com.xxw.platform.plugin.config.api.context.ConfigContext;
import com.xxw.platform.plugin.log.api.constants.LogFileConstants;

/**
 * 日志记录相关的配置
 *
 * @author liaoxiting
 * @date 2020/10/28 16:11
 */
public class LogConfigExpander {

    /**
     * 获取日志记录的文件存储的位置（windows服务器）
     * <p>
     * 末尾不带斜杠
     *
     * @author liaoxiting
     * @date 2020/10/28 16:14
     */
    public static String getLogFileSavePathWindows() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOG_FILE_SAVE_PATH_WINDOWS", String.class, LogFileConstants.DEFAULT_FILE_SAVE_PATH_WINDOWS);
    }

    /**
     * 获取日志记录的文件存储的位置（linux和mac服务器）
     * <p>
     * 末尾不带斜杠
     *
     * @author liaoxiting
     * @date 2020/10/28 16:14
     */
    public static String getLogFileSavePathLinux() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOG_FILE_SAVE_PATH_LINUX", String.class, LogFileConstants.DEFAULT_FILE_SAVE_PATH_LINUX);
    }

    /**
     * 全局日志记录，如果开启则所有请求都将记录日志
     *
     * @author liaoxiting
     * @date 2022/1/12 20:32
     */
    public static Boolean getGlobalControllerOpenFlag() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOG_GLOBAL_FLAG", Boolean.class, LogFileConstants.DEFAULT_GLOBAL_LOG_FLAG);
    }

}
