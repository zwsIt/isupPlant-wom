package com.supcon.mes.module_wom_producetask.presenter;


import android.text.TextUtils;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 公用快速查询，适用subconds单层条件，eg：{"viewCode":"WOM_1.0.0_batchMaterial_batchMaterilList","modelAlias":"batchMateril","condName":"fastCond","remark":"fastCond","subconds":[{"type":"0","columnName":"PRODUCE_BATCH_NUM","dbColumnType":"TEXT","operator":"like","paramStr":"%?%","value":"8520136"}]}
 */
public class CommonListPresenter extends CommonListContract.Presenter {
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
                WomHttpClient.list(url, requestParamsMap)
                        .onErrorReturn(new Function<Throwable, BAP5CommonEntity<Object>>() {
                            @Override
                            public BAP5CommonEntity<Object> apply(Throwable throwable) throws Exception {
                                BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                                bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                                return bap5CommonEntity;
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<Object>>() {
                            @Override
                            public void accept(BAP5CommonEntity<Object> objectBAP5CommonEntity) throws Exception {
                                if (objectBAP5CommonEntity.success){
                                    getView().listSuccess(objectBAP5CommonEntity);
                                }else {
                                    getView().listFailed(objectBAP5CommonEntity.msg);
                                }
                            }
                        })
        );
    }
}
