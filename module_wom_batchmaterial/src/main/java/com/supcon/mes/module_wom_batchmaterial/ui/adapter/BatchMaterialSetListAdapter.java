package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
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
            bucketCodeTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CustomContentTextDialog.showContent(context,bucketCodeTv.getText().toString());
                    return true;
                }
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
            bucketCodeTv.setText(data.getVessel() == null ? context.getResources().getString(R.string.batch_no_bind_bucket) : data.getVessel().getCode());
            batchCurrentArea.setContent(data.getCurrentBurendManage().getAreaId().getName() + "("+data.getCurrentBurendManage().getAreaId().getCode()+")");
            batchCurrentWorkLine.setContent(data.getCurrentBurendManage().getName() + "("+data.getCurrentBurendManage().getCode()+")");
            batchNextArea.setContent(data.getCurrentBurendManage().getAreaId().getFactory().getName() + "("+data.getCurrentBurendManage().getAreaId().getFactory().getCode()+")");
            time.setContent(data.getStartTime() == null ? "--" : DateUtil.dateTimeFormat(data.getStartTime()));
        }
    }


}
