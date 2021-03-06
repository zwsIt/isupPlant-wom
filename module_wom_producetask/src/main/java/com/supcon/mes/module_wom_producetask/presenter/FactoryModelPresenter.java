package com.supcon.mes.module_wom_producetask.presenter;


import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.contract.FactoryModelContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class FactoryModelPresenter extends FactoryModelContract.Presenter {
    @Override
    public void listFactoryModelUnit(int pageNo, Map<String, Object> customCondition, Map<String, Object> queryParams) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParams);
        fastQueryCondEntity.modelAlias = "factoryModel";
        String productLineId= customCondition.get("lineId")+"";
        String processId= customCondition.get("processId")+"";
        Map<String,Object> fastQueryMap=new HashMap<>();
//        fastQueryMap.put("fastQueryCond", GsonUtil.gsonString(fastQueryCondEntity));
        Map<String, Object> paramsMap = ListRequestParamUtil.getQueryParam(pageNo,500,true,fastQueryCondEntity,null);
        mCompositeSubscription.add(
                WomHttpClient.factoryUnitRef2Query(productLineId,processId,fastQueryMap)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<CommonBAPListEntity<FactoryModelEntity>> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        })
                        .subscribe(commonBAPListEntityBAP5CommonEntity -> {
                            if (commonBAPListEntityBAP5CommonEntity.success) {
                                FactoryModelPresenter.this.getView().listFactoryModelUnitSuccess(commonBAPListEntityBAP5CommonEntity);
                            } else {
                                FactoryModelPresenter.this.getView().listFactoryModelUnitFailed(commonBAPListEntityBAP5CommonEntity.msg);
                            }
                        })
        );
    }
}
