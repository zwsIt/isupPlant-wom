package com.supcon.mes.module_wom_producetask.ui.adapter;

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
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;

import java.math.BigDecimal;

import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/14
 * Email zhangwenshuai1@supcon.com
 * Desc 灵活投料报工
 */
public class PutInAgileReportDetailAdapter extends BaseListDataRecyclerViewAdapter<PutInDetailEntity> {

    public PutInAgileReportDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<PutInDetailEntity> getViewHolder(int viewType) {
        return new ReportViewHolder(context);
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/14
     * desc 报工明细Item
     */
    class ReportViewHolder extends BaseRecyclerViewHolder<PutInDetailEntity> {

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
        @BindByTag("remainderNumEt")
        CustomEditText remainderNumEt;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("materialBatchNumTv")
        TextView materialBatchNumTv;
        @BindByTag("remainderNumEtLl")
        LinearLayout remainderNumEtLl;

        public ReportViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_put_in_agile_activity_report;
        }

        @Override
        protected void initView() {
            super.initView();
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            remainderNumEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
                        getItem(getAdapterPosition()).setMaterialId(null);
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
                    .skip(1)
                    .filter(charSequence -> {
                        PutInDetailEntity data = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence.toString())) {
                            data.setPutinNum(null);
                            data.setUseNum(null);
                            return false;
                        }

                        if (data.getPutinNum() != null && data.getPutinNum().toString().equals(charSequence.toString())) {
                            return false;
                        }

                        if (charSequence.toString().startsWith(".")) {
                            numEt.editText().setText("0.");
                            numEt.editText().setSelection(numEt.getContent().length());
                            return false;
                        }
                        BigDecimal current = new BigDecimal(charSequence.toString().trim());
                        // 尾料参照、扫描时 自动计算
                        if (data.getSpecificationNum() != null) {
                            if (current.compareTo(data.getSpecificationNum()) > 0) {
                                ToastUtils.show(context, context.getString(R.string.wom_putin_num_compare_scan) + data.getSpecificationNum());
                                int index = numEt.editText().getSelectionStart();
                                if (index == 0){
                                    numEt.setContent(data.getSpecificationNum().toString());
                                }else {
                                    numEt.editText().getText().delete(index-1, index);
                                }
                                return false;
                            }
                            data.setRemainNum(data.getSpecificationNum().subtract(current));
                        }
                        if (data.getRemainId() != null) {
                            if (current.compareTo(data.getRemainId().getRemainNum()) > 0) {
                                ToastUtils.show(context, context.getString(R.string.wom_putin_num_compare_remain) + data.getRemainId().getRemainNum());
                                int index = numEt.editText().getSelectionStart();
                                if (index == 0){
                                    numEt.setContent(data.getRemainId().getRemainNum().toString());
                                }else {
                                    numEt.editText().getText().delete(index-1, index);
                                }
                                return false;
                            }
                            data.setRemainNum(data.getRemainId().getRemainNum().subtract(current));
                        }
                        return true;
                    })
                    .subscribe(charSequence -> {
                        PutInDetailEntity data = getItem(getAdapterPosition());
                        data.setPutinNum(new BigDecimal(charSequence.toString().trim()));
                        data.setUseNum(getItem(getAdapterPosition()).getPutinNum());
                        if (data.getRemainNum() != null){
                            remainderNumEt.setContent(data.getRemainNum().toString());
                        }

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
            RxTextView.textChanges(remainderNumEt.editText())
                    .skipInitialValue()
                    .skip(1)
                    .filter(charSequence -> {
                        PutInDetailEntity data = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence.toString())) {
                            data.setRemainNum(null);
                            return false;
                        }

                        if (data.getRemainNum() != null && data.getRemainNum().toString().equals(charSequence.toString())) {
                            return false;
                        }

                        if (charSequence.toString().startsWith(".")) {
                            remainderNumEt.editText().setText("0.");
                            remainderNumEt.editText().setSelection(remainderNumEt.getContent().length());
                            return false;
                        }
                        BigDecimal current = new BigDecimal(charSequence.toString().trim());

                        // 尾料参照、扫描时 自动计算
                        if (data.getSpecificationNum() != null) {
                            if (current.compareTo(data.getSpecificationNum()) > 0) {
                                ToastUtils.show(context, context.getString(R.string.wom_remain_num_compare) + data.getSpecificationNum());
                                int index = remainderNumEt.editText().getSelectionStart();
                                if (index == 0){
                                    remainderNumEt.setContent(data.getSpecificationNum().toString());
                                }else {
                                    remainderNumEt.editText().getText().delete(index-1, index);
                                }
                                return false;
                            }
                            data.setPutinNum(data.getSpecificationNum().subtract(current));
                        }
                        if (data.getRemainId() != null) {
                            if (current.compareTo(data.getRemainId().getRemainNum()) > 0) {
                                ToastUtils.show(context, context.getString(R.string.wom_remain_compare_less) + data.getRemainId().getRemainNum());
                                int index = remainderNumEt.editText().getSelectionStart();
                                if (index == 0){
                                    remainderNumEt.setContent(data.getRemainId().getRemainNum().toString());
                                }else {
                                    remainderNumEt.editText().getText().delete(index-1, index);
                                }
                                return false;
                            }
                            data.setPutinNum(data.getRemainId().getRemainNum().subtract(current));
                        }
                        return true;
                    })
                    .subscribe(charSequence -> {
                        PutInDetailEntity data = getItem(getAdapterPosition());
                        data.setRemainNum(new BigDecimal(charSequence.toString().trim()));
                        if (data.getPutinNum() != null){
                            numEt.setContent(data.getPutinNum().toString());
                        }
                    });


        }

        @Override
        protected void update(PutInDetailEntity data) {
            if (data.getRemainId() != null){
                materialName.setEditable(false);
                batchNum.setEditable(false);
//                warehouseTv.setEditable(false);
//                storeSetTv.setEditable(false);
                remainderNumEtLl.setVisibility(View.GONE);
            }else {
                materialName.setEditable(true);
                batchNum.setEditable(true);
//                warehouseTv.setEditable(true);
//                storeSetTv.setEditable(true);
                remainderNumEtLl.setVisibility(View.VISIBLE);
            }
            if (WomConstant.SystemCode.MATERIAL_BATCH_02.equals(data.getMaterialId().getIsBatch().id)){
                materialBatchNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_batch_number,0,R.drawable.ic_necessary,0);
            }else {
                materialBatchNumTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_batch_number,0,0,0);
            }
            materialName.setContent(data.getMaterialId().getId()==null ? "" : String.format("%s(%s)", data.getMaterialId().getName(), data.getMaterialId().getCode()));
            batchNum.setContent(data.getMaterialBatchNum());
            numEt.setContent(data.getPutinNum() == null ? "" : String.valueOf(data.getPutinNum()));
            remainderNumEt.setContent(data.getRemainNum() == null ? "" : String.valueOf(data.getRemainNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
        }
    }

}
