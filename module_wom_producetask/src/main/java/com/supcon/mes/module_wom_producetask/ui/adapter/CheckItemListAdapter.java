package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.ProCheckDetailEntity;
import com.supcon.mes.module_wom_producetask.ui.activity.CheckItemListActivity;
import com.supcon.mes.module_wom_producetask.util.NumComputeUtil;

import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class CheckItemListAdapter extends BaseListDataRecyclerViewAdapter<ProCheckDetailEntity> {
    public CheckItemListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ProCheckDetailEntity> getViewHolder(int viewType) {
        return new CheckViewHolder(context);
    }

    /**
     * CheckViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 检查项Item
     */
    class CheckViewHolder extends BaseRecyclerViewHolder<ProCheckDetailEntity> {

        @BindByTag("checkDetailName")
        CustomTextView checkDetailName;
        @BindByTag("standardValTv")
        CustomTextView standardValTv;
        @BindByTag("reportValEv")
        CustomEditText reportValEv;
        @BindByTag("reportValTv")
        TextView reportValTv;
        @BindByTag("passTv")
        TextView passTv;
        @BindByTag("passSwitch")
        Switch passSwitch;

        public CheckViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_check_activity_report;
        }

        @Override
        protected void initView() {
            super.initView();
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxTextView.textChanges(reportValEv.editText())
                    .skipInitialValue()
                    .filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(CharSequence charSequence) throws Exception {
                            getItem(getAdapterPosition()).setReportValue(charSequence.toString());
                            if ("-".equals(charSequence.toString())){
                                return false;
                            }
                            if (".".equals(charSequence.toString())){
                                reportValEv.setContent("0.");
                                reportValEv.editText().setSelection(reportValEv.editText().length());  // 光标置末尾
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(charSequence -> {
                        getItem(getAdapterPosition()).setReportValue(charSequence.toString());
                        if (((CheckItemListActivity)context).contentView.isComputingLayout()){
                            ((CheckItemListActivity)context).contentView.post(() -> autoResult(passSwitch,getItem(getAdapterPosition())));
                        }else {
                            autoResult(passSwitch,getItem(getAdapterPosition()));
                        }
                    });
            passSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getItem(getAdapterPosition()).setIsPass(isChecked);
                }
            });
        }

        @Override
        protected void update(ProCheckDetailEntity data) {
            checkDetailName.setContent(data.getCheckItems());
            standardValTv.setContent(data.getStandard());
            reportValEv.setContent(data.getReportValue());
            if (TextUtils.isEmpty(data.getStandard())){
                reportValTv.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }else {
                reportValTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_necessary,0);
            }
            if (TextUtils.isEmpty(data.getStandard())){
                passSwitch.setClickable(true);
                reportValEv.setInputType(InputType.TYPE_CLASS_TEXT);
            }else {
                passSwitch.setClickable(false);
                reportValEv.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
            }
            passSwitch.setChecked(data.isIsPass());
        }
    }

    /**
     * 自动判断结果(是否通过)
     * @param passSwitch
     * @param entity
     */
    private void autoResult(Switch passSwitch, ProCheckDetailEntity entity) {
        if (!TextUtils.isEmpty(entity.getStandard())){

            if (TextUtils.isEmpty(entity.getReportValue())){
                entity.setIsPass(false);
            }else {
                String standard = entity.getStandard();
                if (standard.contains("[") && standard.contains("]")){
                    entity.setIsPass(NumComputeUtil.double2Eq(entity.getReportValue(),standard));
                }else if (standard.contains("[") && standard.contains(")")){
                    entity.setIsPass(NumComputeUtil.leftEqRightUeq(entity.getReportValue(),standard));
                }else if (standard.contains("(") && standard.contains(")")){
                    entity.setIsPass(NumComputeUtil.double2Ueq(entity.getReportValue(),standard));
                }else if (standard.contains("(") && standard.contains("]")){
                    entity.setIsPass(NumComputeUtil.leftUeqRightEq(entity.getReportValue(),standard));
                }
            }
            passSwitch.setChecked(entity.isIsPass());
//            notifyItemRangeChanged(adapterPosition,1);

        }
    }

}
