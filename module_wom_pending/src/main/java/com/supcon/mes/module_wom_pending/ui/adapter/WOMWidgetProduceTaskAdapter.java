package com.supcon.mes.module_wom_pending.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
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
 * Desc
 */
public class WOMWidgetProduceTaskAdapter extends BaseListDataRecyclerViewAdapter<WaitPutinRecordEntity> {
    public WOMWidgetProduceTaskAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitPutinRecordEntity> getViewHolder(int viewType) {
        return new TaskItemViewHolder(context);
    }


    /**
     * TaskProduceTaskItemViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 指令单Item
     */
    class TaskItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

        @BindByTag("produceBatchNumTv")
        TextView produceBatchNumTv;
        @BindByTag("statusTv")
        TextView statusTv;
        @BindByTag("productNameTv")
        TextView productNameTv;
        @BindByTag("executeSystemTv")
        TextView executeSystemTv;
        @BindByTag("lineNameCustomTv")
        CustomTextView lineNameCustomTv;
        @BindByTag("numTv")
        TextView numTv;
        @BindByTag("formulaCustomTv")
        CustomTextView formulaCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("startTv")
        TextView startTv;
        @BindByTag("pauseTv")
        TextView pauseTv;
        @BindByTag("resumeTv")
        TextView resumeTv;
        @BindByTag("stopTv")
        TextView stopTv;
        @BindByTag("tvOperateLl")
        LinearLayout tvOperateLl;

        public TaskItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_wom_wiget_produce_task;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            productNameTv.setOnLongClickListener(v -> {
                CustomContentTextDialog.showContent(context, getItem(getAdapterPosition()).getProductId().getName());
                return true;
            });
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
//                        if (WomConstant.SystemCode.RM_TYPE_SIMPLE.equals(getItem(getAdapterPosition()).getFormulaId().getSetProcess().id)){
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,getItem(getAdapterPosition()));
//                            IntentRouter.go(context,Constant.Router.WOM_SIMPLE_ACTIVITY_LIST,bundle);
//                        }else {
                            IntentRouter.go(context, Constant.AppCode.WOM_Production);
//                        }
                    });
            initOperateViewListener(startTv);
            initOperateViewListener(pauseTv);
            initOperateViewListener(resumeTv);
            initOperateViewListener(stopTv);
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            produceBatchNumTv.setText(data.getProduceBatchNum());
            statusTv.setText(data.getExeState().value);
            productNameTv.setText(data.getProductId().getName());
            executeSystemTv.setText(data.getExeSystem().value);
            lineNameCustomTv.setContent(data.getLineName());
            numTv.setText(String.valueOf(data.getTaskId().getPlanNum()));
            formulaCustomTv.setContent(data.getFormulaId().getFormualCode());

            tvOperateLl.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) { // 待执行
                statusTv.setTextColor(context.getResources().getColor(R.color.status_orange));
                startTv.setVisibility(View.VISIBLE);
                pauseTv.setVisibility(View.GONE);
                resumeTv.setVisibility(View.GONE);
                stopTv.setVisibility(View.GONE);
                timeCustomTv.setContent(data.getPlanStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getPlanStartTime()));
            } else if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getExeState().id)) { // 执行中

                statusTv.setTextColor(context.getResources().getColor(R.color.wom_tv_status_color));
                startTv.setVisibility(View.GONE);
                pauseTv.setVisibility(View.VISIBLE);
                resumeTv.setVisibility(View.GONE);
                stopTv.setVisibility(View.VISIBLE);
                timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            } else if (WomConstant.SystemCode.EXE_STATE_PAUSED.equals(data.getExeState().id)) { // 已暂停
                statusTv.setTextColor(context.getResources().getColor(R.color.status_blue));
                startTv.setVisibility(View.GONE);
                pauseTv.setVisibility(View.GONE);
                resumeTv.setVisibility(View.VISIBLE);
                stopTv.setVisibility(View.VISIBLE);
                timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            } else {
                statusTv.setTextColor(context.getResources().getColor(R.color.red));
                tvOperateLl.setVisibility(View.GONE);
                timeCustomTv.setContent(data.getTaskId().getActStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getTaskId().getActStartTime()));
            }
        }

        /**
         * @param
         * @return
         * @author zhangwenshuai1 2020/3/25
         * @description 操作按钮监听事件
         */
        @SuppressLint("CheckResult")
        private void initOperateViewListener(TextView view) {
            RxView.clicks(view)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(o -> onItemChildViewClick(view, getAdapterPosition(), getItem(getAdapterPosition())));
        }
    }

}
