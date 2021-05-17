package com.supcon.mes.module_wom_replenishmaterial.ui.adapter;

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
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTablePartEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/5/14
 * Email zhangwenshuai1@supcon.com
 * Desc 补料记录扫描Adapter
 */
public class ReplenishMaterialRecordsScanAdapter extends BaseListDataRecyclerViewAdapter<ReplenishMaterialTablePartEntity> {
    public ReplenishMaterialRecordsScanAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ReplenishMaterialTablePartEntity> getViewHolder(int viewType) {
//        if (viewType == 1){
            return new RecordsEditViewHolder(context);
//        }else {
//            return new RecordsViewHolder(context);
//        }
    }

    @Override
    public int getItemViewType(int position, ReplenishMaterialTablePartEntity replenishMaterialTablePartEntity) {
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
    class RecordsEditViewHolder extends BaseRecyclerViewHolder<ReplenishMaterialTablePartEntity> {

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
            return R.layout.replenish_item_material_records_edit;
        }

        @Override
        protected void initView() {
            super.initView();
            batchNum.setEditable(false);
            numEt.setEditable(false);
            numEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            itemViewDelBtn.setVisibility(View.GONE);
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
                            getItem(getAdapterPosition()).setFmNumber(null);
                            return false;
                        }
                        if(charSequence.toString().startsWith(".")){
                            numEt.editText().setText("0.");
                            return false;
                        }
                        return true;
                    })
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setFmNumber(new BigDecimal(charSequence.toString().trim())));

        }

        @Override
        protected void update(ReplenishMaterialTablePartEntity data) {
            batchNum.setContent(data.getBatch());
            numEt.setContent(data.getFmNumber() == null ? "" : String.valueOf(data.getFmNumber()));
        }
    }

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 补料查看Item
     */
    class RecordsViewHolder extends BaseRecyclerViewHolder<ReplenishMaterialTablePartEntity> {

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
            return R.layout.replenish_item_material_records_edit;
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
        protected void update(ReplenishMaterialTablePartEntity data) {
//            materialRecordStateTv.setText(data.getBatRecordState().value);
            batchNum.setContent(data.getBatch());
            numEt.setContent(data.getFmNumber() == null ? "" : String.valueOf(data.getFmNumber()));
        }
    }

}
