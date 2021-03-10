package com.supcon.mes.module_wom_batchmaterial.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialRecordsSubmitAPI;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialRecordsSubmitContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialRecordsSubmitPresenter;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/20
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@Presenter(value = {BatchMaterialRecordsSubmitPresenter.class})
public class BatchMaterialRecordsSubmitController extends BasePresenterController implements BatchMaterialRecordsSubmitContract.View {

    private OnAPIResultListener mOnAPIResultListener;

    public void setOnAPIResultListener(OnAPIResultListener listener) {
        mOnAPIResultListener = listener;
    }

    /**
     * @param paramsMap 操作：配料完成/撤回/派送/配放
     * @param dto
     * @param sign      签收/拒签 or 退废
     * @author zhangwenshuai1 2020/4/20
     * @description 配料记录提交
     */
    public void submit(Map<String, Object> paramsMap, BatchMaterialRecordsSignSubmitDTO dto, boolean sign) {
        presenterRouter.create(BatchMaterialRecordsSubmitAPI.class).submit(paramsMap, dto, sign);
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        if (mOnAPIResultListener != null) {
            mOnAPIResultListener.onSuccess(entity.data);
        }
    }

    @Override
    public void submitFailed(String errorMsg) {
        if (mOnAPIResultListener != null) {
            mOnAPIResultListener.onFail(errorMsg);
        }
    }
}
