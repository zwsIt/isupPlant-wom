package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityOperateContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 活动操作
 */
public class ActivityOperatePresenter extends ActivityOperateContract.Presenter {

    @Override
    public void operateActivity(Long waitPutRecordId, Long qualityActiveId, boolean isFinish) {
        mCompositeSubscription.add(
                WomHttpClient.activityOperate(waitPutRecordId, qualityActiveId, isFinish)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return bap5CommonEntity;
                        }).subscribe(objectBAP5CommonEntity -> {
                    if (objectBAP5CommonEntity.success) {
                        getView().operateActivitySuccess(objectBAP5CommonEntity);
                    } else {
                        getView().operateActivityFailed(objectBAP5CommonEntity.msg);
                    }
                })
        );
    }
}
