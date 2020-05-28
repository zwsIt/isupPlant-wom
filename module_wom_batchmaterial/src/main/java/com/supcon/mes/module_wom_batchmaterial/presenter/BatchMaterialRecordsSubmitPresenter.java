package com.supcon.mes.module_wom_batchmaterial.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialRecordsSubmitContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_batchmaterial.model.network.BmHttpClient;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录提交Presenter
 */
public class BatchMaterialRecordsSubmitPresenter extends BatchMaterialRecordsSubmitContract.Presenter {

    @Override
    public void submit(Map<String, Object> paramsMap, BatchMaterialRecordsSignSubmitDTO dto,boolean sign) {
        Flowable<BAP5CommonEntity<Object>> http;
        if (dto == null) {
            http = BmHttpClient.batchMaterialRecordsSubmit(paramsMap);
        } else {
            if (sign){
                http = BmHttpClient.batchMaterialRecordsSignSubmit(dto);
            }else {
                List<BatchMaterialPartEntity> list = GsonUtil.jsonToList(dto.getDetails(),BatchMaterialPartEntity.class);
                http = BmHttpClient.batchMaterialRecordsRetirement(list);
//                http = BmHttpClient.batchMaterialRecordsRetirement(dto);
            }

        }
        mCompositeSubscription.add(
                http.onErrorReturn(new Function<Throwable, BAP5CommonEntity<Object>>() {
                    @Override
                    public BAP5CommonEntity<Object> apply(Throwable throwable) throws Exception {
                        BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                        bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                        return bap5CommonEntity;
                    }
                })
                        .subscribe(new Consumer<BAP5CommonEntity<Object>>() {
                            @Override
                            public void accept(BAP5CommonEntity<Object> bapResultEntityBAP5CommonEntity) throws Exception {
                                if (bapResultEntityBAP5CommonEntity.success) {
                                    BatchMaterialRecordsSubmitPresenter.this.getView().submitSuccess(bapResultEntityBAP5CommonEntity);
                                } else {
                                    BatchMaterialRecordsSubmitPresenter.this.getView().submitFailed(bapResultEntityBAP5CommonEntity.msg);
                                }
                            }
                        })
        );
    }
}
