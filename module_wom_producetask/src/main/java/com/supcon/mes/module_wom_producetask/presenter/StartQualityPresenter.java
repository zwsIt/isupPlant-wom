package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.StartQualityContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 发起请检
 */
public class StartQualityPresenter extends StartQualityContract.Presenter {

    @Override
    public void startQuality(Long activeId) {
        mCompositeSubscription.add(
                WomHttpClient.adjustFinish(activeId)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        }).subscribe(objectBAP5CommonEntity -> {
                    if (objectBAP5CommonEntity.success){
                        getView().startQualitySuccess(objectBAP5CommonEntity);
                    }else {
                        getView().startQualityFailed(objectBAP5CommonEntity.msg);
                    }
                })
        );
    }
}
