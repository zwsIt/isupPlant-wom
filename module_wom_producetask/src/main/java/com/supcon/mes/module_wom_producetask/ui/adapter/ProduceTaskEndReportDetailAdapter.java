package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ProduceTaskEndReportDetailAdapter extends BaseListDataRecyclerViewAdapter<OutputDetailEntity> {
    public ProduceTaskEndReportDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<OutputDetailEntity> getViewHolder(int viewType) {
        return new ReportViewHolder(context);
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/1
     * desc 报工明细Item
     */
    class ReportViewHolder extends BaseRecyclerViewHolder<OutputDetailEntity> {

        @BindByTag("batchNum")
        CustomEditText batchNum;
        @BindByTag("numEt")
        CustomEditText numEt;
        @BindByTag("remainderNumEt")
        CustomEditText remainderNumEt;
        @BindByTag("warehouseTv")
        CustomTextView warehouseTv;
        @BindByTag("storeSetTv")
        CustomTextView storeSetTv;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("preNumTv")
        TextView preNumTv;
        @BindByTag("vessel")
        CustomEditText vessel;

        public ReportViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_output_report;
        }

        @Override
        protected void initView() {
            super.initView();
            preNumTv.setText(context.getResources().getString(R.string.wom_output_num));
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            remainderNumEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            itemViewDelBtn.setOnClickListener(v -> {
                getList().remove(getItem(getAdapterPosition()));
                notifyItemRangeRemoved(getAdapterPosition(),1);
                notifyItemRangeChanged(getAdapterPosition(),getItemCount()-getAdapterPosition());
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
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence charSequence) throws Exception {
                            getItem(getAdapterPosition()).setOutputNum(new BigDecimal(charSequence.toString().trim()));
                            getItem(getAdapterPosition()).setReportNum(getItem(getAdapterPosition()).getOutputNum());
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
                        OutputDetailEntity data = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence.toString())) {
                            data.setRemainNum(null);
                            return false;
                        }

                        if (charSequence.toString().startsWith(".")) {
                            remainderNumEt.editText().setText("0.");
                            remainderNumEt.editText().setSelection(remainderNumEt.getContent().length());
                            return false;
                        }

                        return true;
                    })
                    .subscribe(charSequence -> {
                        OutputDetailEntity data = getItem(getAdapterPosition());
                        data.setRemainNum(new BigDecimal(charSequence.toString().trim()));
                    });
        }

        @Override
        protected void update(OutputDetailEntity data) {
            batchNum.setContent(data.getMaterialBatchNum());
            numEt.setContent(data.getOutputNum() == null ? "" : String.valueOf(data.getOutputNum()));
            remainderNumEt.setContent(data.getRemainNum() == null ? "" : String.valueOf(data.getRemainNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
            vessel.setContent(data.getVessel() == null ? "" : data.getVessel().getCode());
        }
    }

}
