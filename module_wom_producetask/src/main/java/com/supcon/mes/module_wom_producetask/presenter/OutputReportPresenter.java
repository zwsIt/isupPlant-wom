package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.OutputReportContract;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.BatchPutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.OutputDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import io.reactivex.Flowable;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 投料活动报工
 */
public class OutputReportPresenter extends OutputReportContract.Presenter {

    @Override
    public void submit(Long id, String __pc__, OutputDetailDTO outputDetailDTO) {
        mCompositeSubscription.add(
                WomHttpClient.outputReportSubmit(id,__pc__,outputDetailDTO)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<BapResultEntity> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
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
