package com.supcon.mes.module_wom_preparematerial.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_preparematerial.model.contract.PreMaterialReceiveListContract;
import com.supcon.mes.module_wom_preparematerial.model.network.WomHttpClient;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialReceiveListPresenter extends PreMaterialReceiveListContract.Presenter {
    @Override
    public void getPreMaterialReceiveList(int pageNum, Map<String, Object> queryParams) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParams);
        fastQueryCondEntity.modelAlias = "prepMatralReco";
        fastQueryCondEntity.viewCode = "WOM_1.0.0_prePraOrder_recordingReceiveList";
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("pageSize", 50);
        pageQueryParam.put("paging", true);
        pageQueryParam.put("pageNo", pageNum);
        pageQueryParam.put("fastQueryCond", GsonUtil.gsonString(fastQueryCondEntity));
        mCompositeSubscription.add(
                WomHttpClient.getPreMaterialReceiveList(pageQueryParam)
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<CommonBAPListEntity<PreMaterialEntity>> bap5CommonEntity = new BAP5CommonEntity();
                            bap5CommonEntity.success = false;
                            bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return bap5CommonEntity;
                        })
                        .subscribe((BAP5CommonEntity<CommonBAPListEntity<PreMaterialEntity>> commonBAPListEntityBAP5CommonEntity) -> {
                            if (commonBAPListEntityBAP5CommonEntity.success) {

                                for (PreMaterialEntity preMaterialEntity : commonBAPListEntityBAP5CommonEntity.data.result) {
                                    preMaterialEntity.receiveState = SystemCodeManager.getInstance().getSystemCodeEntity("WOM_receiveState/receive");
                                }


                                PreMaterialReceiveListPresenter.this.getView().getPreMaterialReceiveListSuccess(commonBAPListEntityBAP5CommonEntity.data);
                            } else {
                                PreMaterialReceiveListPresenter.this.getView().getPreMaterialReceiveListFailed(commonBAPListEntityBAP5CommonEntity.msg);
                            }
                        }));
    }
}
