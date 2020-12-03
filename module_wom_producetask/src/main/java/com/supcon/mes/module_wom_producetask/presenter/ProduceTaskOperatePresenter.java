package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 指令单操作
 */
public class ProduceTaskOperatePresenter extends ProduceTaskOperateContract.Presenter {
    @Override
    public void operateProduceTask(Long waitPutRecordId, String operateType, List<OutputDetailEntity> outputDetailEntityList) {
        Map<String, Object> map = new HashMap<>();
        map.put("waitPutRecordId", waitPutRecordId);
        map.put("operateType", operateType);
        map.put("outputDetail", outputDetailEntityList);
        mCompositeSubscription.add(
                WomHttpClient.taskOperate(map)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        }).subscribe(objectBAP5CommonEntity -> {
                    if (objectBAP5CommonEntity.success) {
                        getView().operateProduceTaskSuccess(objectBAP5CommonEntity);
                    } else {
                        getView().operateProduceTaskFailed(objectBAP5CommonEntity.msg);
                    }
                })
        );
    }

    @Override
    public void operateDischarge(Long taskId) {
        mCompositeSubscription.add(
                WomHttpClient.operateDischarge(taskId)
                .onErrorReturn(throwable -> {
                    BAP5CommonEntity bap5CommonEntity = new BAP5CommonEntity();
                    bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    bap5CommonEntity.success =false;
                    return null;
                })
                .subscribe(new Consumer<BAP5CommonEntity>() {
                    @Override
                    public void accept(BAP5CommonEntity bap5CommonEntity) throws Exception {
                        if (bap5CommonEntity.success){
                            getView().operateDischargeSuccess(bap5CommonEntity);
                        }else {
                            getView().operateDischargeFailed(bap5CommonEntity.msg);
                        }
                    }
                })
        );
    }

}
