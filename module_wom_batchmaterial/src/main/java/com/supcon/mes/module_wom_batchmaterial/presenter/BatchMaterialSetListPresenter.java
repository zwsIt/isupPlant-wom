package com.supcon.mes.module_wom_batchmaterial.presenter;


import android.util.ArrayMap;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialSetListContract;
import com.supcon.mes.module_wom_batchmaterial.model.network.BmHttpClient;

import java.util.ArrayList;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/12
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class BatchMaterialSetListPresenter extends BatchMaterialSetListContract.Presenter {

    @Override
    public void listBatchMaterialSets(int pageIndex, String url,boolean pending, ArrayMap<String, Object> queryMap) {
        Map<String, Object> pageQueryParams = PageParamUtil.pageParam(pageIndex, 20, true);

//        if (!pending){
//            ArrayMap<String,Object> customCondition = new ArrayMap<>();
//            customCondition.put("needFM",true);
//            pageQueryParams.put("customCondition",customCondition);
//        }

        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        fastQueryCondEntity.subconds = new ArrayList<>();

        if (queryMap.containsKey(Constant.BAPQuery.CODE)){
            // 桶编码
            BAPQueryParamsHelper.setLike(false);
            JoinSubcondEntity joinSubcondEntityBucket = BAPQueryParamsHelper.crateJoinSubcondEntity(queryMap, "WOM_VESSELS,ID,WOM_FM_BILLS,VESSEL");
            fastQueryCondEntity.subconds.add(joinSubcondEntityBucket);
            queryMap.remove(Constant.BAPQuery.CODE);
        }

        // 设备
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(queryMap, "HM_FTY_EQUIPMENTS,ID,WOM_FMN_NOTICES,EQUIPMENT");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);


        fastQueryCondEntity.modelAlias = "bmSet";
        fastQueryCondEntity.viewCode = "WOM_1.0.0_batchMaterialSet_bmSetList";
        pageQueryParams.put("fastQueryCond", fastQueryCondEntity.toString());

        mCompositeSubscription.add(
                BmHttpClient.listBatchMaterialSets(pageQueryParams)
                        .onErrorReturn(throwable -> {
                            CommonBAP5ListEntity<BatchMaterialSetEntity> commonBAP5ListEntity = new CommonBAP5ListEntity<>();
                            commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            commonBAP5ListEntity.success = false;
                            return commonBAP5ListEntity;
                        })
                        .subscribe(replenishMaterialNotifyEntityCommonBAP5ListEntity -> {
                            if (replenishMaterialNotifyEntityCommonBAP5ListEntity.success) {
                                getView().listBatchMaterialSetsSuccess(replenishMaterialNotifyEntityCommonBAP5ListEntity);
                            } else {
                                getView().listBatchMaterialSetsFailed(replenishMaterialNotifyEntityCommonBAP5ListEntity.msg);
                            }
                        })
        );
    }
}
