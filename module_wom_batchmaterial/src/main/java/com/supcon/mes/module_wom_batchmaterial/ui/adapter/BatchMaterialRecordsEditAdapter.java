package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import java.math.BigDecimal;

import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录编辑Adapter
 */
public class BatchMaterialRecordsEditAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterialPartEntity> {
    private boolean materialBatchNo; // 物料是否未启用批次
    public BatchMaterialRecordsEditAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BatchMaterialPartEntity> getViewHolder(int viewType) {
        if (viewType == 1){
            return new RecordsEditViewHolder(context);
        }else {
            return new RecordsViewHolder(context);
        }
    }

    @Override
    public int getItemViewType(int position, BatchMaterialPartEntity batchMaterialPartEntity) {
        if (batchMaterialPartEntity.getBatRecordState() != null && BmConstant.SystemCode.RECORD_STATE_BATCH.equals(batchMaterialPartEntity.getBatRecordState().id)){
            return 1;
        }else {
            return -1;
        }
    }

    public void setMaterialBatchNo(boolean b) {
        materialBatchNo = b;
    }

    /**
     * RecordsEditViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 配料编辑Item
     */
    class RecordsEditViewHolder extends BaseRecyclerViewHolder<BatchMaterialPartEntity> {

        @BindByTag("materialNameLl")
        LinearLayout materialNameLl;
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
        @BindByTag("materialBatchNumTv")
        TextView materialBatchNumTv;

        public RecordsEditViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_records_edit;
        }

        @Override
        protected void initView() {
            super.initView();
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (materialBatchNo){
                materialBatchNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_batch_number,0,0,0);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            itemViewDelBtn.setOnClickListener(v -> onItemChildViewClick(itemViewDelBtn, getAdapterPosition(), getItem(getAdapterPosition())));
            RxTextView.textChanges(batchNum.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setMaterialBatchNum(charSequence.toString().trim()));
            RxTextView.textChanges(numEt.editText())
                    .skipInitialValue()
                    .filter(charSequence -> {
                        if (TextUtils.isEmpty(charSequence.toString())){
                            getItem(getAdapterPosition()).setOfferNum(null);
                            return false;
                        }
                        if(charSequence.toString().startsWith(".")){
                            numEt.editText().setText("0.");
                            return false;
                        }
                        return true;
                    })
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setOfferNum(new BigDecimal(charSequence.toString().trim())));
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
        }

        @Override
        protected void update(BatchMaterialPartEntity data) {
            batchNum.setContent(data.getMaterialBatchNum());
            numEt.setContent(data.getOfferNum() == null ? "" : String.valueOf(data.getOfferNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
        }
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 配料查看Item
     */
    class RecordsViewHolder extends BaseRecyclerViewHolder<BatchMaterialPartEntity> {

//        @BindByTag("materialRecordStateLl")
//        LinearLayout materialRecordStateLl;
        @BindByTag("materialRecordStateTv")
        TextView materialRecordStateTv;
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
        @BindByTag("materialBatchNumTv")
        TextView materialBatchNumTv;

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
            materialRecordStateTv.setVisibility(View.VISIBLE);
            batchNum.setEditable(false);
            warehouseTv.setEditable(false);
            storeSetTv.setEditable(false);
            numEt.setEditable(false);
            itemViewDelBtn.setVisibility(View.GONE);
            if (materialBatchNo){
                materialBatchNumTv.setCompoundDrawablesWithIntrinsicBounds(com.supcon.mes.module_wom_producetask.R.drawable.ic_batch_number,0,0,0);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
        }

        @Override
        protected void update(BatchMaterialPartEntity data) {
            materialRecordStateTv.setText(data.getBatRecordState().value);
            batchNum.setContent(data.getMaterialBatchNum());
            batchNum.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            numEt.setContent(data.getOfferNum() == null ? "" : String.valueOf(data.getOfferNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
        }
    }

}
