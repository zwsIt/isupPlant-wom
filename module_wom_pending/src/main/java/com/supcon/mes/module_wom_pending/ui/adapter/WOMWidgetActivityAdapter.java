package com.supcon.mes.module_wom_pending.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_pending.R;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 机动活动Adapter
 */
public class WOMWidgetActivityAdapter extends BaseListDataRecyclerViewAdapter<WaitPutinRecordEntity> {
    public WOMWidgetActivityAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitPutinRecordEntity> getViewHolder(int viewType) {
        return new ActivityItemViewHolder(context);
    }



    /**
     * CheckItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 检查Item
     */
    class ActivityItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

        @BindByTag("activityNameTv")
        TextView activityNameTv;
        @BindByTag("activityTypeTv")
        TextView activityTypeTv;
        @BindByTag("sequenceCustomTv")
        CustomTextView sequenceCustomTv;
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("agileIv")
        ImageView agileIv;

        public ActivityItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_wom_widget_activity;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,getItem(getAdapterPosition()));
                        if (WomConstant.SystemCode.RM_TYPE_SIMPLE.equals(getItem(getAdapterPosition()).getFormulaId().getSetProcess().id)){
                            IntentRouter.go(context,Constant.Router.WOM_SIMPLE_ACTIVITY_LIST,bundle);
                        }
                        else{
                            IntentRouter.go(context,Constant.Router.WOM_ACTIVITY_LIST,bundle);
                        }
                    });
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            if(WomConstant.SystemCode.RM_activeType_PUTIN.equals(data.getTaskActiveId().getActiveType().id)){

                ((ViewGroup)numCustomTv.getParent()).setVisibility(View.VISIBLE);
                numCustomTv.setContent(data.getTaskActiveId().getPlanQuantity() == null ? "" : data.getTaskActiveId().getPlanQuantity().toString());
                if(data.getAgile()){
                    agileIv.setVisibility(View.VISIBLE);
                    activityNameTv.setText(data.getActiveName());
                }
                else{
                    agileIv.setVisibility(View.GONE);
                    activityNameTv.setText(data.getTaskActiveId().getMaterialId().getName());
                }
            }
            else{
                ((ViewGroup)numCustomTv.getParent()).setVisibility(View.GONE);
                activityNameTv.setText(data.getActiveName());
            }

            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            if(TextUtils.isEmpty(data.getTaskActiveId().getExecSort())){
                ((ViewGroup)sequenceCustomTv.getParent()).setVisibility(View.GONE);
            }
            else {
                ((ViewGroup)sequenceCustomTv.getParent()).setVisibility(View.VISIBLE);
                sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            }

            if(TextUtils.isEmpty(data.getTaskProcessId().getEquipmentId().getName())){
                ((ViewGroup)factoryModelUnitCustomTv.getParent()).setVisibility(View.GONE);
            }
            else{
                ((ViewGroup)factoryModelUnitCustomTv.getParent()).setVisibility(View.VISIBLE);
                factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            }

            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));

        }
    }

}
