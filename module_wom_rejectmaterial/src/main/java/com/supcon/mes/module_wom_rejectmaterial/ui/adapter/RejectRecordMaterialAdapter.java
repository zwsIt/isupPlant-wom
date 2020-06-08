package com.supcon.mes.module_wom_rejectmaterial.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectRecordMaterialEntity;

/**
 * Author by fengjun1,
 * Date on 2020/6/4.
 */
public class RejectRecordMaterialAdapter extends BaseListDataRecyclerViewAdapter<RejectRecordMaterialEntity> {
    public RejectRecordMaterialAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RejectRecordMaterialEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<RejectRecordMaterialEntity> {

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
        protected void update(RejectRecordMaterialEntity data) {
            tableNoTv.setText(data.headId != null ? (TextUtils.isEmpty(data.headId.getTableNo()) ? "--" : data.headId.getTableNo()) : "--");
            materialNameTv.setText(data.materialId != null ? (!TextUtils.isEmpty(data.materialId.name) ? data.materialId.name : "--") : "--");
            rejectMaterialCode.setContent(data.materialId != null ? (!TextUtils.isEmpty(data.materialId.code) ? data.materialId.code : "--") : "--");
            materialBatch.setContent(data.materialBatchNum != null ? data.materialBatchNum : "--");
            rejectNum.setContent(data.rejectNum != null ? String.valueOf(data.rejectNum) : "--");
            rejectStaff.setContent(data.headId != null ? (data.headId.getRejectApplyStaff() != null ? data.headId.getRejectApplyStaff().name : "--") : "--");
        }
    }
}
