package com.supcon.mes.module_wom_rejectmaterial.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_rejectmaterial.model.contract.RejectMaterialSubmitContract;
import com.supcon.mes.module_wom_rejectmaterial.model.dto.RejectMaterialDTO;
import com.supcon.mes.module_wom_rejectmaterial.model.network.RmHttpClient;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 退料提交Presenter
 */
public class RejectMaterialSubmitPresenter extends RejectMaterialSubmitContract.Presenter {

    @Override
    public void submit(Long id, String __pc__,String path, RejectMaterialDTO dto) {
        mCompositeSubscription.add(
                RmHttpClient.submit(path,id,__pc__,dto)
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
