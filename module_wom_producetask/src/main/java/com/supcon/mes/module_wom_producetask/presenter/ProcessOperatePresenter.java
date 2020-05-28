package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ProcessOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 工序操作
 */
public class ProcessOperatePresenter extends ProcessOperateContract.Presenter {

    @Override
    public void updateProcessFactoryModelUnit(Long taskProcessId, String __pc__, Map<String, Object> submitMap) {
        mCompositeSubscription.add(
                WomHttpClient.submit(taskProcessId,__pc__,submitMap)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<BapResultEntity> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return bap5CommonEntity;
                        })
                        .subscribe(bapResultEntityBAP5CommonEntity -> {
                            if (bapResultEntityBAP5CommonEntity.success) {
                                getView().updateProcessFactoryModelUnitSuccess(bapResultEntityBAP5CommonEntity);
                            } else {
                                getView().updateProcessFactoryModelUnitFailed(bapResultEntityBAP5CommonEntity.msg);
                            }
                        })
        );
    }

    @Override
    public void operateProcess(Long waitPutRecordId, boolean isFinish) {
        mCompositeSubscription.add(
                WomHttpClient.taskProcessOperate(waitPutRecordId,isFinish)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = throwable.toString();
                            return bap5CommonEntity;
                        }).subscribe(objectBAP5CommonEntity -> {
                    if (objectBAP5CommonEntity.success){
                        getView().operateProcessSuccess(objectBAP5CommonEntity);
                    }else {
                        getView().operateProcessFailed(objectBAP5CommonEntity.msg);
                    }
                })
        );
    }
}
