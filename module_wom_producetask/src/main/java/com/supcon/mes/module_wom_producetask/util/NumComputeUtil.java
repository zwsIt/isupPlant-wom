package com.supcon.mes.module_wom_producetask.util;

import com.supcon.mes.mbap.view.CustomEditText;

import java.util.regex.Pattern;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/5/30
 * Email zhangwenshuai1@supcon.com
 * Desc 数值自动计算Util
 */
public class NumComputeUtil {
    /**
     * 两边包含：[10,99]
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean double2Eq(String dispValue, String standDispValue){
        String[] numArr = standDispValue.replace("[","").replace("]","").split(",");
        return Double.parseDouble(dispValue) >= Double.parseDouble(numArr[0]) && Double.parseDouble(dispValue) <= Double.parseDouble(numArr[1]);
    }

    /**
     * 两边非包含：(10,99)
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean double2Ueq(String dispValue, String standDispValue){
        String[] numArr = standDispValue.replace("(","").replace(")","").split(",");
        return Double.parseDouble(dispValue) > Double.parseDouble(numArr[0]) && Double.parseDouble(dispValue) < Double.parseDouble(numArr[1]);
    }

    /**
     * 左包含右非包含：[10,99)
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean leftEqRightUeq(String dispValue, String standDispValue){
        String[] numArr = standDispValue.replace("[","").replace(")","").split(",");
        return Double.parseDouble(dispValue) > Double.parseDouble(numArr[0]) && Double.parseDouble(dispValue) < Double.parseDouble(numArr[1]);
    }

    /**
     * 左非包含右包含：(10,99]
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean leftUeqRightEq(String dispValue, String standDispValue){
        String[] numArr = standDispValue.replace("(","").replace("]","").split(",");
        return Double.parseDouble(dispValue) > Double.parseDouble(numArr[0]) && Double.parseDouble(dispValue) <= Double.parseDouble(numArr[1]);
    }

    /**
     * 大于等于：≥10
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean GEQ(String dispValue, String standDispValue){
        String num = standDispValue.replace("≥","");
        return Double.parseDouble(dispValue) >= Double.parseDouble(num);
    }

    /**
     * 大于：>10
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean greater(String dispValue, String standDispValue){
        String num = standDispValue.replace(">","");
        return Double.parseDouble(dispValue) > Double.parseDouble(num);
    }

    /**
     * 小于等于：≤99
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean LEQ(String dispValue, String standDispValue){
        String num = standDispValue.replace("≤","");
        return Double.parseDouble(dispValue) <= Double.parseDouble(num);
    }

    /**
     * 小于：<99
     * @param dispValue  输入值
     * @param standDispValue  标准范围
     * @return
     */
    public static boolean lesser(String dispValue, String standDispValue){
        String num = standDispValue.replace("<","");
        return Double.parseDouble(dispValue) < Double.parseDouble(num);
    }
    /*
     * 是否为浮点数？double或float类型。
     * @param str 传入的字符串。
     * @return 是浮点数返回true,否则返回false。
     */
    public static boolean isDoubleOrFloat(String str) {
        Pattern pattern = Pattern.compile("^-?\\d+(\\.)?(\\d+)?$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断“.”开始的赋值为“0.”
     * @param charSequence
     * @param customEditText
     * @return
     */
    public static boolean eqPointStart(CharSequence charSequence, CustomEditText customEditText) {
        if (".".equals(charSequence.toString())) {
            customEditText.setContent("0.");
            customEditText.editText().setSelection(customEditText.editText().length());  // 光标置末尾
            return true;
        }
        return false;
    }
}
