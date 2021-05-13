package com.supcon.mes.module_wom_replenishmaterial.presenter;


import android.text.TextUtils;
import android.util.ArrayMap;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableListContract;
import com.supcon.mes.module_wom_replenishmaterial.model.network.HttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/12
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ReplenishMaterialTableListPresenter extends ReplenishMaterialTableListContract.Presenter {

    @Override
    public void listReplenishMaterialTables(int pageIndex, String url, ArrayMap<String, Object> queryMap) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("pageNo",pageIndex);
        pageQueryParams.put("pageSize",20);
        pageQueryParams.put("maxPageSize",500);
        pageQueryParams.put("paging",true);

        ArrayMap<String,Object> arrayMap = new ArrayMap<>();
        arrayMap.put(Constant.BAPQuery.NOTICE_STATE,queryMap.get(Constant.BAPQuery.NOTICE_STATE));
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(arrayMap);

        queryMap.remove(Constant.BAPQuery.NOTICE_STATE);
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(queryMap,"HM_FTY_EQUIPMENTS,ID,WOM_FMN_NOTICES,EQUIPMENT");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);
        fastQueryCondEntity.modelAlias = "fmBill";
        fastQueryCondEntity.viewCode="WOM_1.0.0_fillMaterial_fmBillList";


        pageQueryParams.put("fastQueryCond", fastQueryCondEntity.toString());
        mCompositeSubscription.add(
                HttpClient.listReplenishMaterialTables(url,pageQueryParams)
                        .onErrorReturn(throwable -> {
                            CommonBAP5ListEntity<ReplenishMaterialTableEntity> commonBAP5ListEntity = new CommonBAP5ListEntity<>();
                            commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            commonBAP5ListEntity.success = false;
                            return commonBAP5ListEntity;
                        })
                        .subscribe(replenishMaterialNotifyEntityCommonBAP5ListEntity -> {
                            if (replenishMaterialNotifyEntityCommonBAP5ListEntity.success ){
                                getView().listReplenishMaterialTablesSuccess(replenishMaterialNotifyEntityCommonBAP5ListEntity);
                            }else {
                                getView().listReplenishMaterialTablesFailed(replenishMaterialNotifyEntityCommonBAP5ListEntity.msg);
                            }
                        })
        );
    }

}
