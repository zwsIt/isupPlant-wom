package com.supcon.mes.module_wom_rejectmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialPartEntity;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 退料记录编辑Adapter
 */
public class RejectMaterialRecordsEditAdapter extends BaseListDataRecyclerViewAdapter<RejectMaterialPartEntity> {

    public RejectMaterialRecordsEditAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RejectMaterialPartEntity> getViewHolder(int viewType) {
        return new RecordsEditViewHolder(context);
    }

    /**
     * RecordsEditViewHolder
     * created by zhangwenshuai1 2020/4/21
     * desc 退料编辑Item
     */
    class RecordsEditViewHolder extends BaseRecyclerViewHolder<RejectMaterialPartEntity> {


        @BindByTag("material")
        CustomTextView material;
        @BindByTag("batchNum")
        CustomTextView batchNum;
        @BindByTag("warehouseTv")
        CustomTextView warehouseTv;  // 入库仓
        @BindByTag("storeSetTv")
        CustomTextView storeSetTv; // 入库货位
        @BindByTag("numEt")
        CustomTextView numEt;
        @BindByTag("rejectReason")
        CustomTextView rejectReason;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public RecordsEditViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_reject_material_records_edit;
        }

        @Override
        protected void initView() {
            super.initView();
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            itemViewDelBtn.setOnClickListener(v -> {
                onItemChildViewClick(itemViewDelBtn, getAdapterPosition(), getItem(getAdapterPosition()));
            });
//            RxTextView.textChanges(numEt.editText())
//                    .skipInitialValue()
//                    .filter(new Predicate<CharSequence>() {
//                        @Override
//                        public boolean test(CharSequence charSequence) throws Exception {
//                            if (TextUtils.isEmpty(charSequence.toString())){
//                                getItem(getAdapterPosition()).setRejectNum(null);
//                                return false;
//                            }
//                            return true;
//                        }
//                    })
//                    .subscribe(charSequence -> {
//                        getItem(getAdapterPosition()).setRejectNum(new BigDecimal(charSequence.toString().trim()));
//                    });
            warehouseTv.setOnChildViewClickListener((childView, action, obj) -> {
                if (action == -1){
                    getItem(getAdapterPosition()).setWareId(null);
                    getItem(getAdapterPosition()).setStoreId(null);
                    notifyItemRangeChanged(getAdapterPosition(),1);
                }else {
                    onItemChildViewClick(childView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
            storeSetTv.setOnChildViewClickListener((childView, action, obj) -> {
                if (action == -1){
                    getItem(getAdapterPosition()).setStoreId(null);
                }else {
                    onItemChildViewClick(childView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
            rejectReason.setOnChildViewClickListener((childView, action, obj) -> {
                if (action == -1){
                    getItem(getAdapterPosition()).setRejectReason(null);
                }else {
                    onItemChildViewClick(rejectReason,getAdapterPosition(),getItem(getAdapterPosition()));
                }

            });
        }

        @Override
        protected void update(RejectMaterialPartEntity data) {
            material.setContent(data.getMaterialId() == null ? "" : String.format("%s(%s)", data.getMaterialId().getName(), data.getMaterialId().getCode()));
            batchNum.setContent(data.getMaterialBatchNum());
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
            numEt.setContent(data.getRejectNum() == null ? "" : String.valueOf(data.getRejectNum()));
            rejectReason.setContent(data.getRejectReason() == null ? "" : data.getRejectReason().value);
        }
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 退料查看Item
     *//*
    class RecordsViewHolder extends BaseRecyclerViewHolder<RejectMaterialPartEntity> {

//        @BindByTag("materialRecordStateLl")
//        LinearLayout materialRecordStateLl;
//        @BindByTag("materialRecordState")
//        TextView materialRecordState;
        @BindByTag("materialName")
        CustomTextView materialName;
        @BindByTag("batchNum")
        CustomEditText batchNum;
        @BindByTag("warehouseTv")
        CustomTextView warehouseTv;
        @BindByTag("storeSetTv")
        CustomTextView storeSetTv;
        @BindByTag("numEt")
        CustomEditText numEt;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_records_edit;
        }

        @Override
        protected void initView() {
            super.initView();
            batchNum.setEditable(false);
            warehouseTv.setEditable(false);
            storeSetTv.setEditable(false);
            numEt.setEditable(false);
            itemViewDelBtn.setVisibility(View.GONE);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
        }

        @Override
        protected void update(RejectMaterialPartEntity data) {
            batchNum.setContent(data.getMaterialBatchNum());
            batchNum.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            numEt.setContent(data.getOfferNum() == null ? "" : String.valueOf(data.getOfferNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
        }
    }*/

}
