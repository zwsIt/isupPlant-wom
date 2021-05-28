package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令集
 */
public class BatchMaterialSetListAdapter extends BaseListDataRecyclerViewAdapter<BatchMaterialSetEntity> {
    public BatchMaterialSetListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BatchMaterialSetEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<BatchMaterialSetEntity> {

        @BindByTag("activityNameTv")
        TextView activityNameTv;
        @BindByTag("statusTv")
        TextView statusTv;
        @BindByTag("autoBatchIv")
        ImageView autoBatchIv;
        @BindByTag("bucketCodeTv")
        TextView bucketCodeTv;
        @BindByTag("batchCurrentArea")
        CustomTextView batchCurrentArea;
        @BindByTag("batchCurrentWorkLine")
        CustomTextView batchCurrentWorkLine;
        @BindByTag("batchNextArea")
        CustomTextView batchNextArea;
        @BindByTag("time")
        CustomTextView time;
        @BindByTag("bindBucket")
        TextView bindBucket;

        public ViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.batch_item_material_set_list;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(300,TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(@NonNull Object o) throws Exception {
                            BatchMaterialSetEntity data = getItem(getAdapterPosition());
                            if (data.getVessel() == null || data.getVessel().getId() == null){
                                ToastUtils.show(context,context.getResources().getString(R.string.batch_please_bind_bucket_first));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            BatchMaterialSetEntity batchMaterialSetEntity = getItem(getAdapterPosition());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(BmConstant.IntentKey.BATCH_MATERIAL_SET,batchMaterialSetEntity);
                            if (BmConstant.SystemCode.TASK_BATCH.equals(batchMaterialSetEntity.getFmTask().id)){
                                IntentRouter.go(context,BmConstant.Router.BATCH_MATERIAL_INSTRUCTION_LIST,bundle);
                            }else if (BmConstant.SystemCode.TASK_TRANSPORT.equals(batchMaterialSetEntity.getFmTask().id)){
                                IntentRouter.go(context,BmConstant.Router.BATCH_TRUNK_AREA_SCAN,bundle);
                            }
                        }
                    });
            bucketCodeTv.setOnLongClickListener(v -> {
                CustomContentTextDialog.showContent(context,bucketCodeTv.getText().toString());
                return true;
            });
            RxView.clicks(bindBucket).throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onItemChildViewClick(bindBucket,0,getItem(getAdapterPosition()));
                        }
                    });
        }

        @Override
        protected void update(BatchMaterialSetEntity data) {
            activityNameTv.setText(data.getFormulaActiveId().getName());
            statusTv.setText(data.getFmState() == null ? "--" : data.getFmState().value);
            bucketCodeTv.setText(data.getVessel() == null || TextUtils.isEmpty(data.getVessel().getCode()) ? context.getResources().getString(R.string.batch_no_bind_bucket) : data.getVessel().getCode());
            batchCurrentArea.setContent(data.getCurrentBurendManage().getAreaId().getName() + "("+data.getCurrentBurendManage().getAreaId().getCode()+")");
            batchCurrentWorkLine.setContent(data.getCurrentBurendManage().getName() + "("+data.getCurrentBurendManage().getCode()+")");
            batchNextArea.setContent(data.getNextBurendManage() == null || data.getNextBurendManage().getAreaId() == null ? "--":data.getNextBurendManage().getAreaId().getName() + "("+data.getNextBurendManage().getAreaId().getCode()+")");
            time.setContent(data.getStartTime() == null ? "--" : DateUtil.dateTimeFormat(data.getStartTime()));
            if (data.getCurrentBurendManage().getAreaId().isAutoBurden()){
                autoBatchIv.setVisibility(View.VISIBLE);
            }else {
                autoBatchIv.setVisibility(View.GONE);
            }
            if (data.getCurrentBurendManage().getAreaId().isAutoBurden() || (data.getVessel() != null && data.getVessel().getId() != null)){
                bindBucket.setVisibility(View.GONE);
            }else {
                bindBucket.setVisibility(View.VISIBLE);
            }
        }
    }


}
