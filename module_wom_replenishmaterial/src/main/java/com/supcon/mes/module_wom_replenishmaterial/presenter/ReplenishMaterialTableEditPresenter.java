package com.supcon.mes.module_wom_replenishmaterial.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableEditContract;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialTableDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.network.HttpClient;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令编辑Presenter
 */
public class ReplenishMaterialTableEditPresenter extends ReplenishMaterialTableEditContract.Presenter {

    @Override
    public void submit(Long id, String __pc__, ReplenishMaterialTableDTO replenishMaterialTableDTO) {
        mCompositeSubscription.add(
                HttpClient.replenishMaterialEditSubmit(id,__pc__,replenishMaterialTableDTO)
                        .onErrorReturn(throwable -> {
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
