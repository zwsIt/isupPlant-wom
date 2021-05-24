package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

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
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionPartEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/14
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录编辑Adapter
 */
public class BatchInstructionPartsAdapter extends BaseListDataRecyclerViewAdapter<BatchInstructionPartEntity> {
    public BatchInstructionPartsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BatchInstructionPartEntity> getViewHolder(int viewType) {
//        if (viewType == 1){
            return new RecordsEditViewHolder(context);
//        }else {
//            return new RecordsViewHolder(context);
//        }
    }

    @Override
    public int getItemViewType(int position, BatchInstructionPartEntity replenishMaterialTablePartEntity) {
//        if (batchMaterialPartEntity.getBatRecordState() != null && BmConstant.SystemCode.RECORD_STATE_BATCH.equals(batchMaterialPartEntity.getBatRecordState().id)){
//            return 1;
//        }else {
            return -1;
//        }
    }

    /**
     * RecordsEditViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 补料编辑Item
     */
    class RecordsEditViewHolder extends BaseRecyclerViewHolder<BatchInstructionPartEntity> {

        @BindByTag("batchNum")
        CustomEditText batchNum;
        @BindByTag("numEt")
        CustomEditText numEt;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public RecordsEditViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.batch_item_instruction_parts;
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
            itemViewDelBtn.setOnClickListener(v -> onItemChildViewClick(itemViewDelBtn, getAdapterPosition(), getItem(getAdapterPosition())));
            RxTextView.textChanges(batchNum.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setBatch(charSequence.toString()));

//            RxTextView.textChanges(numEt.editText())
//                    .skipInitialValue()
//                    .subscribe(new Consumer<CharSequence>() {
//                        @Override
//                        public void accept(CharSequence charSequence) throws Exception {
//                            getItem(getAdapterPosition()).setFmNumber(new BigDecimal(charSequence.toString()));
//                        }
//                    });
            RxTextView.textChanges(numEt.editText())
                    .skipInitialValue()
                    .filter(charSequence -> {
                        if (TextUtils.isEmpty(charSequence.toString())){
                            getItem(getAdapterPosition()).setBmNumber(null);
                            return false;
                        }
                        if(charSequence.toString().startsWith(".")){
                            numEt.editText().setText("0.");
                            return false;
                        }
                        return true;
                    })
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setBmNumber(new BigDecimal(charSequence.toString().trim())));

        }

        @Override
        protected void update(BatchInstructionPartEntity data) {
            batchNum.setContent(data.getBatch());
            numEt.setContent(data.getBmNumber() == null ? "" : String.valueOf(data.getBmNumber()));
        }
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 补料查看Item
     */
    class RecordsViewHolder extends BaseRecyclerViewHolder<BatchInstructionPartEntity> {

        @BindByTag("materialRecordStateTv")
        TextView materialRecordStateTv;
        @BindByTag("materialName")
        CustomTextView materialName;
        @BindByTag("batchNum")
        CustomEditText batchNum;
        @BindByTag("numEt")
        CustomEditText numEt;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;

        public RecordsViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.batch_item_instruction_parts;
        }

        @Override
        protected void initView() {
            super.initView();
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            materialRecordStateTv.setVisibility(View.VISIBLE);
            itemViewDelBtn.setVisibility(View.GONE);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
        }

        @Override
        protected void update(BatchInstructionPartEntity data) {
//            materialRecordStateTv.setText(data.getBatRecordState().value);
            batchNum.setContent(data.getBatch());
            numEt.setContent(data.getBmNumber() == null ? "" : String.valueOf(data.getBmNumber()));
        }
    }

}
