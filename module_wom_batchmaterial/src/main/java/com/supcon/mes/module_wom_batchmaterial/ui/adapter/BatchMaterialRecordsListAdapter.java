package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialListActivity;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialRecordsRecallListActivity;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录ListAdapter
 */
public class BatchMaterialRecordsListAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterialPartEntity> {
    public BatchMaterialRecordsListAdapter(Context context) {
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
        @BindByTag("batchMode")
        TextView batchMode;
        @BindByTag("checkBox")
        CheckBox checkBox;

        public RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_records;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> checkBox.performClick());
            materialNameTv.setOnLongClickListener(v -> {
                CustomContentTextDialog.showContent(context, getItem(getAdapterPosition()).getMaterialId().getName());
                return true;
            });
            checkBox.setOnClickListener(v -> onItemChildViewClick(checkBox,getAdapterPosition(),getItem(getAdapterPosition())));

//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    getItem(getAdapterPosition()).setChecked(isChecked);
//
//                }
//            });
        }

        @Override
        protected void update(BatchMaterialPartEntity data) {
            materialNameTv.setText(data.getMaterialId().getName());
            produceBatchNum.setContent(data.getHeadId().getProduceBatchNum());
            materialCode.setContent(data.getMaterialId().getCode());
            materialBatchNum.setContent(data.getMaterialBatchNum());
            batchNumber.setContent(data.getOfferNum() == null ? "" : data.getOfferNum().toString());
            batchMode.setText(String.format("%s       %s", data.getHeadId().getBatchType().value, data.getHeadId().getBatchSite().value));
            if (context instanceof BatchMaterialListActivity){
                if (!data.isChecked() && ((BatchMaterialListActivity)context).getBatchMaterialRecordsFragment().getAllChosen()){
                    data.setChecked(true);
                }
            }else if (context instanceof BatchMaterialRecordsRecallListActivity){ // 撤回list
                if (!data.isChecked() && ((BatchMaterialRecordsRecallListActivity)context).getAllChosen()){
                    data.setChecked(true);
                }
            }
            checkBox.setChecked(data.isChecked());
        }
    }

}
