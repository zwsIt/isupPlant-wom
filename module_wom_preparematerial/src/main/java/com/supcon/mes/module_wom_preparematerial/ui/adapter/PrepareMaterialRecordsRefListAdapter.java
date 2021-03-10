package com.supcon.mes.module_wom_preparematerial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.model.bean.PrepareMaterialPartEntity;
import com.supcon.mes.module_wom_preparematerial.ui.activity.PrepareMaterialRejectRefListActivity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 备料记录参照ListAdapter
 */
public class PrepareMaterialRecordsRefListAdapter extends BaseListDataRecyclerViewAdapter<PrepareMaterialPartEntity> {
    public PrepareMaterialRecordsRefListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<PrepareMaterialPartEntity> getViewHolder(int viewType) {
        return new RecordsViewHolder(context);
    }

    /**
     * RecordsViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 备料记录Item
     */
    class RecordsViewHolder extends BaseRecyclerViewHolder<PrepareMaterialPartEntity> {


        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("materialBatchNum")
        CustomTextView materialBatchNum;
        @BindByTag("prepareNumber")
        CustomTextView prepareNumber;
        @BindByTag("status")
        TextView status;
        @BindByTag("checkBox")
        CheckBox checkBox;

        RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_prepare_material_records_ref;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        checkBox.performClick();
                    });
            checkBox.setOnClickListener(v -> onItemChildViewClick(checkBox,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(PrepareMaterialPartEntity data) {
            materialNameTv.setText(data.getMaterialId().getName());
            materialCode.setContent(data.getMaterialId().getCode());
            materialBatchNum.setContent(data.getMaterialBatchNum());
            prepareNumber.setContent(data.getDoneNum() == null ? "" : data.getDoneNum().toString());
            status.setText(String.format("%s       %s", data.getPartState().value, data.getRejectReason() == null ? "" : data.getRejectReason().value));

            if (context instanceof PrepareMaterialRejectRefListActivity){
                if (!data.isChecked() && ((PrepareMaterialRejectRefListActivity)context).getAllChosen()){
                    data.setChecked(true);
                }
            }/*else if (context instanceof PrepareMaterialRejectRefListActivity){
                if (!data.isChecked() && ((BatchMaterialRejectRefListActivity)context).getAllChosen()){
                    data.setChecked(true);
                }
            }*/
            checkBox.setChecked(data.isChecked());
        }
    }

}
