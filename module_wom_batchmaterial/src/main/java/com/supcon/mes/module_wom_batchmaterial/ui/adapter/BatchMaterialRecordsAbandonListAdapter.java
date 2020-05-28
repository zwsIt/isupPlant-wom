package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialListActivity;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialRecordsRecallAbandonListActivity;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录退废ListAdapter
 */
public class BatchMaterialRecordsAbandonListAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterialPartEntity> {
    public BatchMaterialRecordsAbandonListAdapter(Context context) {
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
        @BindByTag("checkBox")
        CheckBox checkBox;

        public RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_records_abandon;
        }

        @Override
        protected void initView() {
            super.initView();
            checkBox.setVisibility(View.VISIBLE);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            rejectReason.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    if (action == -1){
                        getItem(getAdapterPosition()).setRetirementState(null);
                    }else {
                        onItemChildViewClick(rejectReason,getAdapterPosition(),getItem(getAdapterPosition()));
                    }

                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChildViewClick(checkBox,getAdapterPosition(),getItem(getAdapterPosition()));
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

            rejectReason.setContent(data.getRetirementState() == null ? "" : data.getRetirementState().value);
            if (!data.isChecked() && ((BatchMaterialRecordsRecallAbandonListActivity)context).getAllChosen()){
                data.setChecked(true);
            }
            checkBox.setChecked(data.isChecked());
        }
    }

}
