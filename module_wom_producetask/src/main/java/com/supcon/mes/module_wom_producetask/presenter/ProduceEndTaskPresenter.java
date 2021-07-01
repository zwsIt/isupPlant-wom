package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.contract.OutputReportContract;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceEndTaskContract;
import com.supcon.mes.module_wom_producetask.model.dto.OutputDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.ProduceEndTaskDTO;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 工单结束保存报工
 */
public class ProduceEndTaskPresenter extends ProduceEndTaskContract.Presenter {
    @Override
    public void save(Long id, ProduceEndTaskDTO produceEndTaskDTO) {
        mCompositeSubscription.add(
                WomHttpClient.produceEndReportSave(id,produceEndTaskDTO)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<BapResultEntity> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        })
                        .subscribe(bapResultEntityBAP5CommonEntity -> {
                            if (bapResultEntityBAP5CommonEntity.success){
                                getView().saveSuccess(bapResultEntityBAP5CommonEntity);
                            }else {
                                getView().saveFailed(bapResultEntityBAP5CommonEntity.msg);
                            }
                        })
        );
    }
}
