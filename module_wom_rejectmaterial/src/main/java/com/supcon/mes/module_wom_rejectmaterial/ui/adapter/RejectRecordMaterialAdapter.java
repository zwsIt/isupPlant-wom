package com.supcon.mes.module_wom_rejectmaterial.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialPartEntity;

/**
 * Author by fengjun1,
 * Date on 2020/6/4.
 */
public class RejectRecordMaterialAdapter extends BaseListDataRecyclerViewAdapter<RejectMaterialPartEntity> {
    public RejectRecordMaterialAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RejectMaterialPartEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<RejectMaterialPartEntity> {

        @BindByTag("tableNoTv")
        TextView tableNoTv;
        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("rejectMaterialCode")
        CustomTextView rejectMaterialCode;
        @BindByTag("materialBatch")
        CustomTextView materialBatch;
        @BindByTag("rejectNum")
        CustomTextView rejectNum;
        @BindByTag("rejectStaff")
        CustomTextView rejectStaff;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_reject_record_material;
        }

        @Override
        protected void update(RejectMaterialPartEntity data) {
            tableNoTv.setText(data.getHeadId() != null ? (TextUtils.isEmpty(data.getHeadId().getTableNo()) ? "--" : data.getHeadId().getTableNo()) : "--");
            materialNameTv.setText(data.getMaterialId() != null ? (!TextUtils.isEmpty(data.getMaterialId().getName()) ? data.getMaterialId().getName() : "--") : "--");
            rejectMaterialCode.setContent(data.getMaterialId() != null ? (!TextUtils.isEmpty(data.getMaterialId().getCode()) ? data.getMaterialId().getCode() : "--") : "--");
            materialBatch.setContent(data.getMaterialBatchNum() != null ? data.getMaterialBatchNum() : "--");
            rejectNum.setContent(data.getRejectNum() != null ? String.valueOf(data.getRejectNum()) : "--");
            rejectStaff.setContent(data.getHeadId() != null ? (data.getHeadId().getRejectApplyStaff() != null ? data.getHeadId().getRejectApplyStaff().name : "--") : "--");
        }
    }
}
