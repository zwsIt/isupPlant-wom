package com.supcon.mes.module_wom_rejectmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 配料退料Adapter
 */
public class RejectBatchMaterialListAdapter extends BaseListDataRecyclerViewAdapter<RejectMaterialEntity> {
    public RejectBatchMaterialListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RejectMaterialEntity> getViewHolder(int viewType) {
        return new ItemViewHolder(context);
    }

    /**
     * ItemViewHolder
     * created by zhangwenshuai1 2020/4/21
     * desc 配料退料Item
     */
    class ItemViewHolder extends BaseRecyclerViewHolder<RejectMaterialEntity> {

        @BindByTag("tableNoTv")
        TextView tableNoTv;
        @BindByTag("statusTv")
        TextView statusTv;
        @BindByTag("rejectType")
        CustomTextView rejectType;
        @BindByTag("applyStaff")
        CustomTextView applyStaff;
        @BindByTag("time")
        CustomTextView time;

        ItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_reject_material;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.REJECT_MATERIAL,getItem(getAdapterPosition()));
                        IntentRouter.go(context,Constant.Router.REJECT_MATERIAL_EDIT,bundle);
                    });

        }

        @Override
        protected void update(RejectMaterialEntity data) {
            tableNoTv.setText(data.getTableNo());
            statusTv.setText(data.getPending().taskDescription);
            rejectType.setContent(data.getRejectType().value);
            applyStaff.setContent(data.getRejectApplyStaff() == null ? "" : data.getRejectApplyStaff().getName());
            time.setContent(data.getRejectApplyDate() == null ? "" : DateUtil.dateTimeFormat(data.getRejectApplyDate()));
        }

    }

}
