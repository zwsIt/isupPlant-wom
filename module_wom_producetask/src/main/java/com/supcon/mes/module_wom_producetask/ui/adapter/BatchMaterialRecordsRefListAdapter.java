package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.ui.activity.BatchMaterialRefListActivity;
import com.supcon.mes.module_wom_producetask.ui.activity.BatchMaterialRejectRefListActivity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录参照ListAdapter
 */
public class BatchMaterialRecordsRefListAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterialPartEntity> {
    public BatchMaterialRecordsRefListAdapter(Context context) {
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
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("materialBatchNum")
        CustomTextView materialBatchNum;
        @BindByTag("batchNumber")
        CustomTextView batchNumber;
        @BindByTag("batchMode")
        TextView batchMode;
        @BindByTag("checkBox")
        CheckBox checkBox;

        public RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_records_ref;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        checkBox.performClick();
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
            materialCode.setContent(data.getMaterialId().getCode());
            materialBatchNum.setContent(data.getMaterialBatchNum());
            batchNumber.setContent(data.getOfferNum() == null ? "" : data.getOfferNum().toString());
            batchMode.setText(String.format("%s       %s", data.getHeadId().getBatchType().value, data.getHeadId().getBatchSite().value));

            if (context instanceof BatchMaterialRefListActivity){
                if (!data.isChecked() && ((BatchMaterialRefListActivity)context).getAllChosen()){
                    data.setChecked(true);
                }
            }else if (context instanceof BatchMaterialRejectRefListActivity){
                if (!data.isChecked() && ((BatchMaterialRejectRefListActivity)context).getAllChosen()){
                    data.setChecked(true);
                }
            }
            checkBox.setChecked(data.isChecked());
        }
    }

}
