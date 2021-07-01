package com.supcon.mes.module_wom_batchmaterial.presenter;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionPartEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialInstructionEditContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;
import com.supcon.mes.module_wom_batchmaterial.model.network.BmHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String,Object> queryMap = new HashMap<>(1);
        queryMap.put(Constant.BAPQuery.ID,id);
        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        fastQueryCondEntity.subconds = new ArrayList<>();
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(queryMap,"WOM_BM_SET_DETAILS,ID,WOM_BM_RECORDS,BM_SET_DETAIL");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);
        fastQueryCondEntity.modelAlias = "bmRecord";
        fastQueryCondEntity.viewCode = "WOM_1.0.0_batchMaterialSet_bmRecordList";

        mCompositeSubscription.add(
                BmHttpClient.listBatchParts(ListRequestParamUtil.getQueryParam(fastQueryCondEntity,new HashMap<>(1)))
                        .onErrorReturn(throwable -> {
                            CommonBAP5ListEntity<BatchInstructionPartEntity> commonBAP5ListEntity = new CommonBAP5ListEntity<>();
                            commonBAP5ListEntity.success = false;
                            commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return commonBAP5ListEntity;
                        })
                        .subscribe(batchInstructionPartEntityCommonBAP5ListEntity -> {
                            if (batchInstructionPartEntityCommonBAP5ListEntity.success){
                                getView().listBatchPartsSuccess(batchInstructionPartEntityCommonBAP5ListEntity.data);
                            }else {
                                getView().listBatchPartsFailed(batchInstructionPartEntityCommonBAP5ListEntity.msg);
                            }
                        })
        );
    }

    @Override
    public void submit(List<BatchInstructionPartEntity> batchInstructionPartEntityList) {
        BatchInstructionPartEntity[] batchInstructionPartEntities = new BatchInstructionPartEntity[batchInstructionPartEntityList.size()];
        batchInstructionPartEntityList.toArray(batchInstructionPartEntities);
        mCompositeSubscription.add(
                BmHttpClient.batchMaterialInstructionSubmit(batchInstructionPartEntities)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<Boolean> bap5CommonEntity = new BAP5CommonEntity<>();
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            bap5CommonEntity.success = false;
                            return bap5CommonEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<Boolean>>() {
                            @Override
                            public void accept(BAP5CommonEntity<Boolean> booleanBAP5CommonEntity) throws Exception {
                                if (booleanBAP5CommonEntity.success){
                                    getView().submitSuccess(booleanBAP5CommonEntity);
                                }else {
                                    getView().submitFailed(booleanBAP5CommonEntity.msg);
                                }
                            }
                        })
        );
    }
}
