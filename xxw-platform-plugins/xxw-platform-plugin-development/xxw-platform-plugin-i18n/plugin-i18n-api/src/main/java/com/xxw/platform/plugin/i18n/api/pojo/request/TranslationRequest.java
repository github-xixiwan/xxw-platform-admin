package com.xxw.platform.plugin.i18n.api.pojo.request;

import com.xxw.platform.frame.common.annotation.ChineseDescription;
import com.xxw.platform.frame.common.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 多语言请求信息
 *
 * @author liaoxiting
 * @since 2019-10-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TranslationRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @NotNull(message = "tranId不能为空", groups = {edit.class, detail.class, delete.class})
    @ChineseDescription("主键id")
    private Long tranId;

    /**
     * 编码
     */
    @NotBlank(message = "tranCode不能为空", groups = {add.class, edit.class})
    @ChineseDescription("编码")
    private String tranCode;

    /**
     * 多语言条例名称
     */
    @NotBlank(message = "tranName不能为空", groups = {add.class, edit.class})
    @ChineseDescription("多语言条例名称")
    private String tranName;

    /**
     * 语种字典
     */
    @NotBlank(message = "tranLanguageCode不能为空", groups = {add.class, edit.class, changeUserLanguage.class, deleteTranLanguage.class})
    @ChineseDescription("语种字典")
    private String tranLanguageCode;

    /**
     * 翻译的值
     */
    @NotBlank(message = "tranValue不能为空", groups = {add.class, edit.class})
    @ChineseDescription("翻译的值")
    private String tranValue;

    /**
     * 字典id，用在删除语种
     */
    @NotNull(message = "字典id", groups = {deleteTranLanguage.class})
    @ChineseDescription("字典id，用在删除语种")
    private Long dictId;

    /**
     * 改变当前用户多语言
     */
    public @interface changeUserLanguage {
    }

    /**
     * 删除语种
     */
    public @interface deleteTranLanguage {
    }

}
