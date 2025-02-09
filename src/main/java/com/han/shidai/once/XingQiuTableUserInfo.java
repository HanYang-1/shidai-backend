package com.han.shidai.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 星球表格用户信息
 */
@Data
public class XingQiuTableUserInfo {
    /**
     * 成员编号
     */
    @ExcelProperty("成员编号")
    private String planetCode;

    /**
     * 成员昵称
     */
    @ExcelProperty("成员昵称")
    private String username;
}
