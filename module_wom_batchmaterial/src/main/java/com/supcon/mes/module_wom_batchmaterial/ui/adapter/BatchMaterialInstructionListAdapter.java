package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterilEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令Adapter
 */
public class BatchMaterialInstructionListAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterilEntity> {
    public BatchMaterialInstructionListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BatchMaterilEntity> getViewHolder(int viewType) {
        return new ItemViewHolder(context);
    }

    /**
     * TaskProduceTaskItemViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 配料指令指令Item
     */
    class ItemViewHolder extends BaseRecyclerViewHolder<BatchMaterilEntity> {
        @BindByTag("tableNoTv")
        TextView tableNoTv;
        @BindByTag("statusTv")
        TextView statusTv;
        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("produceBatchNum")
        CustomTextView produceBatchNum;
        @BindByTag("batchNumber")
        CustomTextView batchNumber;
        @BindByTag("createStaff")
        CustomTextView createStaff;
        @BindByTag("batchMode")
        TextView batchMode;

        public ItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_material_instruction;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            materialNameTv.setOnLongClickListener(v -> {
                CustomContentTextDialog.showContent(context, getItem(getAdapterPosition()).getProductId().getName());
                return true;
            });
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> {

                        BatchMaterilEntity data = getItem(getAdapterPosition());
                        if (BmConstant.ViewName.BATCH_MATERIAL_EDIT.equals(data.getPending().openUrl)){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.IntentKey.BATCH_MATERIAL_INSTRUCTION,data);
                            IntentRouter.go(context,Constant.Router.BATCH_MATERIAL_INSTRUCTION_EDIT,bundle);
                        }else {
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_support_view));
                        }

                    });

        }

        @Override
        protected void update(BatchMaterilEntity data) {
            tableNoTv.setText(data.getTableNo());
            statusTv.setText(data.getBatchSite().value);
            materialNameTv.setText(data.getProductId().getName());
            materialCode.setContent(data.getProductId().getCode());
            produceBatchNum.setContent(data.getProduceBatchNum());
            batchNumber.setContent(data.getOfferNum() + "/" +data.getNeedNum());
            createStaff.setContent(data.getMakeStaff().name);
//            batchMode.setText(String.format("%s       %s", data.getBatchType().value, data.getBatchSite().value));
        }

    }

}
