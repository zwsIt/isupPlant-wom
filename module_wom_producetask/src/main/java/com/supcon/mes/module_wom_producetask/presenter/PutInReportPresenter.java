package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.BatchPutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import io.reactivex.Flowable;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 投料活动报工
 */
public class PutInReportPresenter extends PutInReportContract.Presenter {

    @Override
    public void submit(boolean batchPutIn, Long id, String __pc__, PutinDetailDTO putinDetailDTO, BatchPutinDetailDTO batchPutinDetailDTO) {
        Flowable<BAP5CommonEntity<BapResultEntity>> flowable;
        if (batchPutIn){
            flowable = WomHttpClient.batchPutInReportSubmit(id,__pc__,batchPutinDetailDTO);
        }else {
            flowable = WomHttpClient.putInReportSubmit(id,__pc__,putinDetailDTO);
        }
        mCompositeSubscription.add(
                flowable.onErrorReturn(throwable -> {
                            BAP5CommonEntity<BapResultEntity> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        })
                        .subscribe(bapResultEntityBAP5CommonEntity -> {
                            if (bapResultEntityBAP5CommonEntity.success){
                                getView().submitSuccess(bapResultEntityBAP5CommonEntity);
                            }else {
                                getView().submitFailed(bapResultEntityBAP5CommonEntity.msg);
                            }
                        })
        );
    }
}
