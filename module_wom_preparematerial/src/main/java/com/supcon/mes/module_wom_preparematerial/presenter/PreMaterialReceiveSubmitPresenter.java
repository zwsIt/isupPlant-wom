package com.supcon.mes.module_wom_preparematerial.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialSubmitEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialSubmitListEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreResultEntity;
import com.supcon.mes.module_wom_preparematerial.model.contract.PreMaterialReceiveSubmitContract;
import com.supcon.mes.module_wom_preparematerial.model.network.WomHttpClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2020/6/30
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialReceiveSubmitPresenter extends PreMaterialReceiveSubmitContract.Presenter {
    @Override
    public void doSubmitPreMaterial(List<PreMaterialSubmitEntity> preMaterialEntities) {

        mCompositeSubscription.add(
                WomHttpClient.prepRecodeSubmit(preMaterialEntities)
                        .onErrorReturn(new Function<Throwable, BAP5CommonEntity<PreResultEntity>>() {
                            @Override
                            public BAP5CommonEntity<PreResultEntity> apply(Throwable throwable) throws Exception {
                                BAP5CommonEntity bap5CommonEntity = new BAP5CommonEntity();
                                bap5CommonEntity.success = false;
                                bap5CommonEntity.msg = throwable.toString();
                                return bap5CommonEntity;
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<PreResultEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<PreResultEntity> preResultEntityBAP5CommonEntity) throws Exception {
                                if(preResultEntityBAP5CommonEntity.success){
                                    getView().doSubmitPreMaterialSuccess(preResultEntityBAP5CommonEntity.data);
                                }
                                else{
                                    getView().doSubmitPreMaterialFailed(preResultEntityBAP5CommonEntity.msg);
                                }
                            }
                        }));
    }
}
