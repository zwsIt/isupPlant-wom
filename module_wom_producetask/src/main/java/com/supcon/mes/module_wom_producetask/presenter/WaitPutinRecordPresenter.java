package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.PageParamUtil;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 待办记录获取Presenter
 */
public class WaitPutinRecordPresenter extends WaitPutinRecordsListContract.Presenter {

    @Override
    public void listWaitPutinRecords(int pageNo, int pageSize, Map<String, Object> queryParams, boolean like) {
        BAPQueryParamsHelper.setLike(like);
        Map<String, Object> requestParamsMap = PageParamUtil.pageParam(null, pageNo, pageSize, true);
        Map<String, Object> subconds = new HashMap<>();
        Map<String, Object> joinSubconds = new HashMap<>();

        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        fastQueryCondEntity.subconds = new ArrayList<>();
        fastQueryCondEntity.modelAlias = "waitPutRecord";
//        fastQueryCondEntity.viewCode = "WOM_1.0.0_waitPutinRecord_waitPutTaskList";

        for (String key : queryParams.keySet()) {
            joinSubconds.clear();
            if (Constant.BAPQuery.FORMULA_SET_PROCESS.equals(key)) { // 配方属性(普通/标准工单)
                joinSubconds.put(key, Objects.requireNonNull(queryParams.get(key)));
                fastQueryCondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(joinSubconds, "RM_FORMULAS,ID,WOM_WAIT_PUT_RECORDS,FORMULA_ID"));
            } else if (Constant.BAPQuery.IS_MORE_OTHER.equals(key)) { // 是否其他活动(配方/其他活动{机动、调整活动})
                joinSubconds.put(key, Objects.requireNonNull(queryParams.get(key)));
                fastQueryCondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(joinSubconds, "WOM_TASK_ACTIVES,ID,WOM_WAIT_PUT_RECORDS,TASK_ACTIVE_ID"));
            } else if (Constant.BAPQuery.TASK_PROCESS_ID.equals(key)) { // 工序ID
                joinSubconds.put(Constant.BAPQuery.ID, Objects.requireNonNull(queryParams.get(key)));
                fastQueryCondEntity.subconds.add(BAPQueryParamsHelper.crateJoinSubcondEntity(joinSubconds, "WOM_TASK_PROCESSES,ID,WOM_WAIT_PUT_RECORDS,TASK_PROCESS_ID"));
            } else {
                subconds.put(key, queryParams.get(key) == null ? "" : queryParams.get(key));
            }
        }
        fastQueryCondEntity.subconds.addAll(BAPQueryParamsHelper.createSingleFastQueryCond(subconds).subconds);
        requestParamsMap.put("fastQueryCond", GsonUtil.gsonString(fastQueryCondEntity));

        Flowable<BAP5CommonEntity<CommonBAPListEntity<WaitPutinRecordEntity>>> womFlowable;
        if (WomConstant.SystemCode.RECORD_TYPE_TASK.equals(queryParams.get(Constant.BAPQuery.RECORD_TYPE))) {
            womFlowable = WomHttpClient.waitPutTaskList(requestParamsMap);
        } else if (WomConstant.SystemCode.RECORD_TYPE_PROCESS.equals(queryParams.get(Constant.BAPQuery.RECORD_TYPE))) {
            womFlowable = WomHttpClient.waitPutProcessList(requestParamsMap);
        } else if (WomConstant.SystemCode.RECORD_TYPE_ACTIVE.equals(queryParams.get(Constant.BAPQuery.RECORD_TYPE))) {
            womFlowable = WomHttpClient.waitPutActiveList(requestParamsMap);
        } else {
            womFlowable = WomHttpClient.waitPutTaskList(requestParamsMap);
        }
        mCompositeSubscription.add(
                womFlowable.onErrorReturn(throwable -> {
                    BAP5CommonEntity<CommonBAPListEntity<WaitPutinRecordEntity>> bap5CommonEntity = new BAP5CommonEntity<>();
                    bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    bap5CommonEntity.success = false;
                    return bap5CommonEntity;
                })
                        .subscribe(commonBAPListEntityBAP5CommonEntity -> {
                            if (commonBAPListEntityBAP5CommonEntity.success && commonBAPListEntityBAP5CommonEntity.data != null) {
                                getView().listWaitPutinRecordsSuccess(commonBAPListEntityBAP5CommonEntity.data);
                            } else {
                                getView().listWaitPutinRecordsFailed(commonBAPListEntityBAP5CommonEntity.msg);
                            }
                        })
        );
    }
}
