package com.supcon.mes.module_wom_replenishmaterial.presenter;

import android.annotation.SuppressLint;
import android.util.ArrayMap;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialNotifyListContract;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialNotifyDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.network.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ReplenishMaterialNotifyListPresenter extends ReplenishMaterialNotifyListContract.Presenter {

    @Override
    public void listReplenishMaterialNotifies(int pageIndex, ArrayMap<String, Object> queryMap) {
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
        fastQueryCondEntity.modelAlias = "fmnNotice";
        fastQueryCondEntity.viewCode="WOM_1.0.0_fillMaterialNotice_fmnNoticeList";


        pageQueryParams.put("fastQueryCond", fastQueryCondEntity.toString());
        mCompositeSubscription.add(
                HttpClient.listReplenishMaterialNotifies(pageQueryParams)
                        .onErrorReturn(throwable -> {
                            CommonBAP5ListEntity<ReplenishMaterialNotifyEntity> commonBAP5ListEntity = new CommonBAP5ListEntity<>();
                            commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            commonBAP5ListEntity.success = false;
                            return commonBAP5ListEntity;
                        })
                        .subscribe(replenishMaterialNotifyEntityCommonBAP5ListEntity -> {
                            if (replenishMaterialNotifyEntityCommonBAP5ListEntity.success ){
                                getView().listReplenishMaterialNotifiesSuccess(replenishMaterialNotifyEntityCommonBAP5ListEntity);
                            }else {
                                getView().listReplenishMaterialNotifiesFailed(replenishMaterialNotifyEntityCommonBAP5ListEntity.msg);
                            }
                        })
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void submit(List<ReplenishMaterialNotifyDTO> dtoList) {
        ReplenishMaterialNotifyDTO dto[] = new ReplenishMaterialNotifyDTO[dtoList.size()];
        dtoList.toArray(dto);
        HttpClient.submit(dto)
                .onErrorReturn(throwable -> {
                    BAP5CommonEntity<BapResultEntity> bap5CommonEntity = new BAP5CommonEntity<>();
                    bap5CommonEntity.success = false;
                    bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return bap5CommonEntity;
                })
                .subscribe(bapResultEntityBAP5CommonEntity -> {
                    if (bapResultEntityBAP5CommonEntity.success){
                        getView().submitSuccess(bapResultEntityBAP5CommonEntity);
                    }else {
                        getView().submitFailed(bapResultEntityBAP5CommonEntity.msg);
                    }
                });
    }
}
