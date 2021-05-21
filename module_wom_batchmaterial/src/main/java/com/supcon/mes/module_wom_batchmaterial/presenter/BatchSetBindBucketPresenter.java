package com.supcon.mes.module_wom_batchmaterial.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialRecordsSubmitContract;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchSetBindBucketContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_batchmaterial.model.network.BmHttpClient;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录提交Presenter
 */
public class BatchSetBindBucketPresenter extends BatchSetBindBucketContract.Presenter {

    @Override
    public void submit(Map<String, Object> paramsMap) {
        mCompositeSubscription.add(
                BmHttpClient.bindBucketSubmit(paramsMap)
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
