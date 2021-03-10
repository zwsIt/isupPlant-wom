package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录拒签ListAdapter
 */
public class BatchMaterialRecordsRejectListAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterialPartEntity> {
    public BatchMaterialRecordsRejectListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BatchMaterialPartEntity> getViewHolder(int viewType) {
        return new RecordsViewHolder(context);
    }

    /**
     * RecordsViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 配料记录Item
     */
    class RecordsViewHolder extends BaseRecyclerViewHolder<BatchMaterialPartEntity> {

        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("produceBatchNum")
        CustomTextView produceBatchNum;
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("materialBatchNum")
        CustomTextView materialBatchNum;
        @BindByTag("batchNumber")
        CustomTextView batchNumber;
        @BindByTag("rejectReason")
        CustomTextView rejectReason;
        @BindByTag("batchMode")
        TextView batchMode;

        public RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_records_reject;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            rejectReason.setOnChildViewClickListener((childView, action, obj) -> {
                if (action == -1){
                    getItem(getAdapterPosition()).setRejectReason(null);
                }else {
                    onItemChildViewClick(rejectReason,getAdapterPosition(),getItem(getAdapterPosition()));
                }

            });
        }

        @Override
        protected void update(BatchMaterialPartEntity data) {
            materialNameTv.setText(data.getMaterialId().getName());
            produceBatchNum.setContent(data.getHeadId().getProduceBatchNum());
            materialCode.setContent(data.getMaterialId().getCode());
            materialBatchNum.setContent(data.getMaterialBatchNum());
            batchNumber.setContent(data.getOfferNum() == null ? "" : data.getOfferNum().toString());
            batchMode.setText(String.format("%s       %s", data.getHeadId().getBatchType().value, data.getHeadId().getBatchSite().value));

            rejectReason.setContent(data.getRejectReason() == null ? "" : data.getRejectReason().value);
        }
    }

}
