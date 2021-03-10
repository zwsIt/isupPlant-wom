package com.supcon.mes.module_wom_batchmaterial.presenter;


import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.ArrayList;
import java.util.Map;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class batchMaterialRecordsPresenter extends CommonListContract.Presenter {
    @Override
    public void list(int pageNo, Map<String, Object> customCondition, Map<String, Object> queryParams, String url, String modelAlias) {
        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        fastQueryCondEntity.modelAlias = modelAlias;
        fastQueryCondEntity.subconds = new ArrayList<>();
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(queryParams,"WOM_BATCH_MATERILS,ID,WOM_BAT_MATERIL_PARTS,HEAD_ID");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);

        Map<String, Object> requestParamsMap = ListRequestParamUtil.getQueryParam(pageNo,20,true,fastQueryCondEntity,customCondition);
        mCompositeSubscription.add(
                WomHttpClient.list(url, requestParamsMap)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success =false;
                            return bap5CommonEntity;
                        })
                        .subscribe(objectBAP5CommonEntity -> {
                            if (objectBAP5CommonEntity.success){
                                getView().listSuccess(objectBAP5CommonEntity);
                            }else {
                                getView().listFailed(objectBAP5CommonEntity.msg);
                            }
                        })
        );
    }
}
