package com.supcon.mes.module_wom_producetask.util;

import android.content.Context;
import android.text.TextUtils;

import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;

/**
 * Created by wanghaidong on 2020/7/31
 * Email:wanghaidong1@supcon.com
 */
public class MaterQRUtil {
    /**
     * @author zhangwenshuai1 2020/8/17
     * @param
     * @return
     * @description 一诺威项目二维码格式
     *
     */
    public static String[] yiNuoWeiMaterialQRCode(String code){
        if (!TextUtils.isEmpty(code) && code.contains("incode") && code.contains("batchno") && code.contains("batchno2") && code.contains("packqty") && code.contains("purcode") && code.contains("orderno")){
            return code.split(",");
        }
        return null;
    }

    /**
     * @author zhangwenshuai1 2020/8/17
     * @param
     * @return
     * @description 产品模式格式
     *
     */
    public static MaterialQRCodeEntity materialQRCode(Context context,String code){
        if (!TextUtils.isEmpty(code)){
            try {
                return GsonUtil.gsonToBean(code,MaterialQRCodeEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show(context,"二维码格式解析错误，请核对！");
                return null;
            }
        }
        ToastUtils.show(context,"二维码内容为空，请核对！");
        return null;
    }
    /**
     * @author zhangwenshuai1 2020/8/17
     * @param
     * @return
     * @description 天味家园
     *
     */
    public static QrCodeEntity getQRCode(Context context,String code){
        if (!TextUtils.isEmpty(code)){
            try {
                return GsonUtil.gsonToBean(code, QrCodeEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show(context,"二维码格式解析错误，请核对！");
                return null;
            }
        }
        ToastUtils.show(context,"二维码内容为空，请核对！");
        return null;
    }
}
