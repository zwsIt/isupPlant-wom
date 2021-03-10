package com.supcon.mes.module_wom_preparematerial.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreResultEntity;
import com.supcon.mes.module_wom_preparematerial.model.contract.PreMaterialReceiveSubmitContract;
import com.supcon.mes.module_wom_preparematerial.model.network.WomHttpClient;

import java.util.List;


/**
 * Created by wangshizhan on 2020/6/30
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialReceiveSubmitPresenter extends PreMaterialReceiveSubmitContract.Presenter {
    @Override
    public void doSubmitPreMaterial(List<PreMaterialEntity> preMaterialEntities) {

        mCompositeSubscription.add(
                WomHttpClient.prepRecodeSubmit(preMaterialEntities)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<PreResultEntity> bap5CommonEntity = new BAP5CommonEntity();
                            bap5CommonEntity.success = false;
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return bap5CommonEntity;
                        })
                        .subscribe(preResultEntityBAP5CommonEntity -> {
                            if (preResultEntityBAP5CommonEntity.success) {
                                PreMaterialReceiveSubmitPresenter.this.getView().doSubmitPreMaterialSuccess(preResultEntityBAP5CommonEntity.data);
                            } else {
                                PreMaterialReceiveSubmitPresenter.this.getView().doSubmitPreMaterialFailed(preResultEntityBAP5CommonEntity.msg);
                            }
                        }));
    }
}
