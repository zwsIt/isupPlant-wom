package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.CheckItemReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.ProCheckDetailDTO;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 活动操作
 */
public class CheckItemReportPresenter extends CheckItemReportContract.Presenter {

    @Override
    public void submit(Long id, String __pc__, ProCheckDetailDTO proCheckDetailDTO) {
        mCompositeSubscription.add(
                WomHttpClient.checkItemReportSubmit(id,__pc__,proCheckDetailDTO)
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
