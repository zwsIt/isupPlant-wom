package com.supcon.mes.module_wom_rejectmaterial.controller;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.module_wom_rejectmaterial.model.api.RejectMaterialSubmitAPI;
import com.supcon.mes.module_wom_rejectmaterial.model.contract.RejectMaterialSubmitContract;
import com.supcon.mes.module_wom_rejectmaterial.model.dto.RejectMaterialDTO;
import com.supcon.mes.module_wom_rejectmaterial.presenter.RejectMaterialSubmitPresenter;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/20
 * Email zhangwenshuai1@supcon.com
 * Desc 退料提交Controller
 */
@Presenter(value = {RejectMaterialSubmitPresenter.class})
public class RejectMaterialSubmitController extends BasePresenterController implements RejectMaterialSubmitContract.View {

    private OnAPIResultListener mOnAPIResultListener;

    public void setOnAPIResultListener(OnAPIResultListener listener) {
        mOnAPIResultListener = listener;
    }

    /**
     * @param
     * @param dto
     * @param sign
     * @author zhangwenshuai1 2020/4/20
     * @description 退料提交
     */
    public void submit(Long id, String __pc__, String path, RejectMaterialDTO dto, boolean sign) {
        presenterRouter.create(RejectMaterialSubmitAPI.class).submit(id, __pc__, path, dto);
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
