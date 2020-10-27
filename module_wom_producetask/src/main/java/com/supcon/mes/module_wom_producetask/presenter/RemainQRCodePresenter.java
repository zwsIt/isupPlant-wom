package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.contract.RemainQRCodeContract;
import com.supcon.mes.module_wom_producetask.model.dto.BatchPutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 尾料扫描
 */
public class RemainQRCodePresenter extends RemainQRCodeContract.Presenter {

    @Override
    public void getMaterialByQR(Long id) {
        mCompositeSubscription.add(
                WomHttpClient.getRemainMaterialJSON(id)
                .onErrorReturn(throwable -> {
                    BAP5CommonEntity bap5CommonEntity = new BAP5CommonEntity();
                    bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                    return bap5CommonEntity;
                })
                .subscribe(bap5CommonEntity -> {
                    if (bap5CommonEntity.success){
                        getView().getMaterialByQRSuccess(bap5CommonEntity);
                    }else {
                        getView().getMaterialByQRFailed(bap5CommonEntity.msg);
                    }
                })
        );
    }
}
