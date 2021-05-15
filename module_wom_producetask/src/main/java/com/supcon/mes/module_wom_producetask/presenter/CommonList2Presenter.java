package com.supcon.mes.module_wom_producetask.presenter;


import android.text.TextUtils;

import com.supcon.mes.middleware.model.CommonSelectListEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.contract.CommonList2Contract;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 公用快速查询，适用subconds单层条件，eg：{"viewCode":"WOM_1.0.0_batchMaterial_batchMaterilList","modelAlias":"batchMateril","condName":"fastCond","remark":"fastCond","subconds":[{"type":"0","columnName":"PRODUCE_BATCH_NUM","dbColumnType":"TEXT","operator":"like","paramStr":"%?%","value":"8520136"}]}
 */
public class CommonList2Presenter extends CommonList2Contract.Presenter {
    @Override
    public void list(int pageNo, Map<String, Object> customCondition, Map<String, Object> queryParams, String url, String modelAlias) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParams);
        fastQueryCondEntity.modelAlias = modelAlias;

        Map<String, Object> requestParamsMap;
        if (TextUtils.isEmpty(modelAlias)){
            requestParamsMap = ListRequestParamUtil.getQueryParam(fastQueryCondEntity,customCondition);
        }else {
            requestParamsMap = ListRequestParamUtil.getQueryParam(pageNo,20,true,fastQueryCondEntity,customCondition);
        }
        mCompositeSubscription.add(
                WomHttpClient.listCommon(url, requestParamsMap)
                        .onErrorReturn(throwable -> {
                            CommonSelectListEntity commonSelectListEntity = new CommonSelectListEntity();
                            commonSelectListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            commonSelectListEntity.success = false;
                            return commonSelectListEntity;
                        })
                        .subscribe(commonSelectListEntity -> {
                            if (commonSelectListEntity.success){
                                getView().listSuccess(commonSelectListEntity);
                            }else {
                                getView().listFailed(commonSelectListEntity.msg);
                            }
                        })
        );
    }
}
