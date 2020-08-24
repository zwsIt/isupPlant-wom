package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;
import com.supcon.mes.module_wom_producetask.util.NumComputeUtil;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 投料报工
 */
public class PutInReportDetailAdapter extends BaseListDataRecyclerViewAdapter<PutInDetailEntity> {

    private boolean batchPutInActivity; // 是否投配料活动

    public PutInReportDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<PutInDetailEntity> getViewHolder(int viewType) {
        return new ReportViewHolder(context);
    }

    public void setBatchPutInActivity(boolean b) {
        batchPutInActivity = b;
    }

    public boolean edit = true;

    /**
     * ReportViewHolder
     * created by zhangwenshuai1 2020/4/10
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
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("index")
        TextView index;
        @BindByTag("beforeWeightTv")
        CustomEditText beforeWeightTv;
        @BindByTag("afterWeightTv")
        CustomEditText afterWeightTv;
        @BindByTag("radioGroup")
        RadioGroup radioGroup;

        public ReportViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_put_in_activity_report;
        }

        @Override
        protected void initView() {
            super.initView();
            if (batchPutInActivity) {
                materialNameLl.setVisibility(View.VISIBLE);
            }

            batchNum.setEditable(edit);
            warehouseTv.setEditable(edit);
            storeSetTv.setEditable(edit);
            numEt.setEditable(edit);
            beforeWeightTv.setEditable(edit);
            afterWeightTv.setEditable(edit);

            numEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            beforeWeightTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            afterWeightTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            for (int i = 0; i < 2; i++) {
                radioGroup.getChildAt(i).setClickable(edit);
            }
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            itemViewDelBtn.setOnClickListener(v -> {
                onItemChildViewClick(itemViewDelBtn, getAdapterPosition(), getItem(getAdapterPosition()));
            });

            RxTextView.textChanges(batchNum.editText())
                    .skipInitialValue()
                    .subscribe(charSequence -> getItem(getAdapterPosition()).setMaterialBatchNum(!TextUtils.isEmpty(charSequence) ? charSequence.toString().trim() : ""));
            RxTextView.textChanges(numEt.editText())
                    .skipInitialValue()
                    .filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(CharSequence charSequence) throws Exception {
                            PutInDetailEntity data = getItem(getAdapterPosition());
                            if (TextUtils.isEmpty(charSequence.toString())) {
                                data.setPutinNum(null);
                                data.setUseNum(null);
                                return false;
                            }
                            if (charSequence.toString().startsWith(".")) {
                                numEt.editText().setText("0.");
                                numEt.editText().setSelection(numEt.getContent().length());
                                return false;
                            }
                            // 用料量大于可用量判断
                            if (data.getAvailableNum() != null && new BigDecimal(charSequence.toString().trim()).compareTo(data.getAvailableNum()) > 0) {
                                ToastUtils.show(context, context.getString(R.string.wom_putin_num_compare) + data.getAvailableNum());
                                int index = numEt.editText().getSelectionStart();
                                numEt.editText().getText().delete(index - 1, index);
                                return false;
                            }

                            return true;
                        }
                    })
                    .subscribe(charSequence -> {
                        getItem(getAdapterPosition()).setPutinNum(new BigDecimal(charSequence.toString().trim()));
                        getItem(getAdapterPosition()).setUseNum(getItem(getAdapterPosition()).getPutinNum());
                    });
            warehouseTv.setOnChildViewClickListener((childView, action, obj) -> {
                if (action == -1) {
                    getItem(getAdapterPosition()).setWareId(null);
                    getItem(getAdapterPosition()).setStoreId(null);
                    notifyItemRangeChanged(getAdapterPosition(), 1);
                } else {
                    onItemChildViewClick(childView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
            if (edit) {
                RxTextView.textChanges(beforeWeightTv.editText())
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MICROSECONDS)
                        .map(CharSequence::toString)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(charSequence -> {
                            PutInDetailEntity putInDetailEntity = getItem(getAdapterPosition());
                            putInDetailEntity.setBeforeNum(!TextUtils.isEmpty(charSequence)?new BigDecimal(charSequence):null);
                            if (TextUtils.isEmpty(charSequence)) {
                                return;
                            }
                            float beforeWeight = !TextUtils.isEmpty(beforeWeightTv.getInput()) ? Float.valueOf(beforeWeightTv.getInput()) : 0;
                            float afterWeight = !TextUtils.isEmpty(afterWeightTv.getInput()) ? Float.valueOf(afterWeightTv.getInput()) : 0;
                            float diff=beforeWeight-afterWeight;
                            putInDetailEntity.setPutinNum(diff==0?null:new BigDecimal(diff));
                            numEt.setContent(putInDetailEntity.getPutinNum() == null ? "" : String.valueOf(putInDetailEntity.getPutinNum()));
                        });

                RxTextView.textChanges(afterWeightTv.editText())
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MICROSECONDS)
                        .map(CharSequence::toString)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(charSequence -> {
                            PutInDetailEntity putInDetailEntity = getItem(getAdapterPosition());
                            putInDetailEntity.setAfterWeight(charSequence);
                            putInDetailEntity.setAfterNum(!TextUtils.isEmpty(charSequence)?new BigDecimal(charSequence):null);
                            if (TextUtils.isEmpty(charSequence)) {
                                return;
                            }
                            float beforeWeight = !TextUtils.isEmpty(beforeWeightTv.getInput()) ? Float.valueOf(beforeWeightTv.getInput()) : 0;
                            float afterWeight = !TextUtils.isEmpty(afterWeightTv.getInput()) ? Float.valueOf(afterWeightTv.getInput()) : 0;
                            float diff=beforeWeight-afterWeight;
                            putInDetailEntity.setPutinNum(diff==0?null:new BigDecimal(diff));
                            numEt.setContent(putInDetailEntity.getPutinNum() == null ? "" : String.valueOf(putInDetailEntity.getPutinNum()));
                        });
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    PutInDetailEntity putInDetailEntity = getItem(getAdapterPosition());
                    if (checkedId == R.id.radioBtn_yes) {
                        putInDetailEntity.setAllPutInto(true);
                    }
                    if (checkedId == R.id.radioBtn_no) {
                        putInDetailEntity.setAllPutInto(false);
                    }
                });
            }
            storeSetTv.setOnChildViewClickListener((childView, action, obj) -> {
                if (action == -1) {
                    getItem(getAdapterPosition()).setStoreId(null);
                } else {
                    onItemChildViewClick(childView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }

        @Override
        protected void update(PutInDetailEntity data) {
            materialName.setContent(String.format("%s(%s)", data.getMaterialId().getName(), data.getMaterialId().getCode()));
            batchNum.setContent(data.getMaterialBatchNum());
            numEt.setContent(data.getPutinNum() == null ? "" : String.valueOf(data.getPutinNum()));
            warehouseTv.setContent(data.getWareId() == null ? "" : data.getWareId().getName());
            storeSetTv.setContent(data.getStoreId() == null ? "" : data.getStoreId().getName());
            beforeWeightTv.setContent(data.getBeforeNum()==null?"":String.valueOf(data.getBeforeNum()));
            afterWeightTv.setContent(data.getAfterNum()==null?"":String.valueOf(data.getAfterNum()));
            if (data.isAllPutInto()) {
                radioGroup.check(R.id.radioBtn_yes);
            } else {
                radioGroup.check(R.id.radioBtn_no);
            }
            index.setText((getAdapterPosition() + 1) + "");
        }
    }

}
