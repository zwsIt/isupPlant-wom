package com.supcon.mes.module_wom_batchmaterial.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionPartEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialInstructionEditContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;
import com.supcon.mes.module_wom_batchmaterial.model.network.BmHttpClient;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令编辑Presenter
 */
public class BatchMaterialInstructionEditPresenter extends BatchMaterialInstructionEditContract.Presenter {

    @Override
    public void listBatchParts(Long id) {
        mCompositeSubscription.add(
                BmHttpClient.listBatchParts(id)
                        .onErrorReturn(new Function<Throwable, CommonBAP5ListEntity<BatchInstructionPartEntity>>() {
                            @Override
                            public CommonBAP5ListEntity<BatchInstructionPartEntity> apply(@NonNull Throwable throwable) throws Exception {
                                CommonBAP5ListEntity<BatchInstructionPartEntity> commonBAP5ListEntity = new CommonBAP5ListEntity<>();
                                commonBAP5ListEntity.success = false;
                                commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                                return commonBAP5ListEntity;
                            }
                        })
                        .subscribe(new Consumer<CommonBAP5ListEntity<BatchInstructionPartEntity>>() {
                            @Override
                            public void accept(CommonBAP5ListEntity<BatchInstructionPartEntity> batchInstructionPartEntityCommonBAP5ListEntity) throws Exception {
                                if (batchInstructionPartEntityCommonBAP5ListEntity.success){
                                    getView().listBatchPartsSuccess(batchInstructionPartEntityCommonBAP5ListEntity.data);
                                }else {
                                    getView().listBatchPartsFailed(batchInstructionPartEntityCommonBAP5ListEntity.msg);
                                }
                            }
                        })
        );
    }

    @Override
    public void submit(Long id, BatchMaterialInstructionDTO batchMaterialInstructionDTO) {
        mCompositeSubscription.add(
                BmHttpClient.batchMaterialInstructionSubmit(id,batchMaterialInstructionDTO)
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
