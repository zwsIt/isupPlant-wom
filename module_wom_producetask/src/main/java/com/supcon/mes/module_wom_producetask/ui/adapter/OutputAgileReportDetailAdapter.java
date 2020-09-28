package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;

import java.math.BigDecimal;

import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 灵活产出活动报工
 */
public class OutputAgileReportDetailAdapter extends BaseListDataRecyclerViewAdapter<OutputDetailEntity> {

    public OutputAgileReportDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OutputDetailEntity> getViewHolder(int viewType) {
        return new ReportViewHolder(context);
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 报工明细Item
     */
    class ReportViewHolder extends BaseRecyclerViewHolder<OutputDetailEntity> {

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
        @BindByTag("preBatchNumTv")
        TextView preBatchNumTv;
        @BindByTag("preNumTv")
        TextView preNumTv;

        public ReportViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_output_agile_activity_report;
        }

        @Override
        protected void initView() {
            super.initView();
            preBatchNumTv.setText(context.getResources().getString(R.string.wom_material_batch_num));
            preNumTv.setText(context.getResources().getString(R.string.wom_output_num));
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            itemViewDelBtn.setOnClickListener(v -> {
                onItemChildViewClick(itemViewDelBtn, getAdapterPosition(), getItem(getAdapterPosition()));
            });
            materialName.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {
                    if(action == -1){
                        getItem(getAdapterPosition()).setProduct(null);
                    }else {
                        onItemChildViewClick(materialName, getAdapterPosition(), getItem(getAdapterPosition()));
                    }
                }
            });
            RxTextView.textChanges(batchNum.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setMaterialBatchNum(charSequence.toString().trim()));
            RxTextView.textChanges(numEt.editText())
                    .skipInitialValue()
                    .filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(CharSequence charSequence) throws Exception {
                            if (TextUtils.isEmpty(charSequence.toString())){
                                getItem(getAdapterPosition()).setOutputNum(null);
                                getItem(getAdapterPosition()).setReportNum(getItem(getAdapterPosition()).getOutputNum());
                                return false;
                            }
                            if(charSequence.toString().startsWith(".")){
                                numEt.editText().setText("0.");
                                numEt.editText().setSelection(numEt.getContent().length());
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(charSequence -> {
                        getItem(getAdapterPosition()).setOutputNum(new BigDecimal(charSequence.toString().trim()));
                        getItem(getAdapterPosition()).setReportNum(getItem(getAdapterPosition()).getOutputNum());
                    });
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
        protected void update(OutputDetailEntity data) {
            if (WomConstant.SystemCode.MATERIAL_BATCH_02.equals(data.getProduct().getIsBatch().id)){
                preBatchNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_batch_number,0,R.drawable.ic_necessary,0);
            }else {
                preBatchNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_batch_number,0,0,0);
            }
            materialName.setContent(data.getProduct() == null ? "" : String.format("%s(%s)",data.getProduct().getName(),data.getProduct().getCode()));
            batchNum.setContent(data.getMaterialBatchNum());
            numEt.setContent(data.getOutputNum() == null ? "" : String.valueOf(data.getOutputNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
        }
    }

}
