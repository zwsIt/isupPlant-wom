package com.supcon.mes.module_wom_batchmaterial.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchTrunkScanAPI;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchSetBindBucketContract;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchTrunkScanContract;
import com.supcon.mes.module_wom_batchmaterial.model.network.BmHttpClient;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录提交Presenter
 */
public class BatchTrunkScanPresenter extends BatchTrunkScanContract.Presenter {

    @Override
    public void submit(Long bmSetId, String trunkCode) {
        mCompositeSubscription.add(
                BmHttpClient.loadMaterial(bmSetId,trunkCode)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        })
                        .subscribe(bapResultEntityBAP5CommonEntity -> {
                            if (bapResultEntityBAP5CommonEntity.success) {
                                getView().submitSuccess(bapResultEntityBAP5CommonEntity);
                            } else {
                                getView().submitFailed(bapResultEntityBAP5CommonEntity.msg);
                            }
                        })
        );
    }
}
