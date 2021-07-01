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
import java.util.HashMap;
import java.util.Map;

import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_JOIN;

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
        HashMap<String,Object> taskStateMap = new HashMap<>(1);
        taskStateMap.put(Constant.BAPQuery.FM_TASK,queryMap.get(Constant.BAPQuery.FM_TASK));
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(taskStateMap);

        // 自动配料
        if (pending){
            HashMap<String,Object> autoBatchMap = new HashMap<>(1);
            autoBatchMap.put(Constant.BAPQuery.AUTO_BURDEN,queryMap.get(Constant.BAPQuery.AUTO_BURDEN));

            JoinSubcondEntity midJoinSubcondEntity = new JoinSubcondEntity();
            midJoinSubcondEntity.joinInfo = "HM_BUREND_MENAGE,ID,WOM_BM_SETS,CURRENT_BUREND_MANAGE";
            midJoinSubcondEntity.type = TYPE_JOIN;
            midJoinSubcondEntity.subconds = new ArrayList<>();
            midJoinSubcondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(autoBatchMap, "HM_AREA_MENGE,ID,HM_BUREND_MENAGE,AREA_ID"));
            fastQueryCondEntity.subconds.add(midJoinSubcondEntity);
        }

        // 桶编码
        queryMap.remove(Constant.BAPQuery.FM_TASK);
        queryMap.remove(Constant.BAPQuery.AUTO_BURDEN);
        fastQueryCondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(queryMap, "WOM_VESSELS,ID,WOM_BM_SETS,VESSEL"));

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
