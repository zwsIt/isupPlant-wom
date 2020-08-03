package com.supcon.mes.module_wom_producetask.util;

import android.text.TextUtils;

/**
 * Created by wanghaidong on 2020/7/31
 * Email:wanghaidong1@supcon.com
 */
public class MaterQRUtil {
    public static String[] materialQRCode(String code){
        if (!TextUtils.isEmpty(code) && code.contains("incode") && code.contains("batchno") && code.contains("batchno2") && code.contains("packqty") && code.contains("purcode") && code.contains("orderno")){
            return code.split(",");
        }
        return null;
    }
}
