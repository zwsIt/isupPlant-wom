package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ProduceTaskListAdapter extends BaseListDataRecyclerViewAdapter<WaitPutinRecordEntity> {
    public ProduceTaskListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitPutinRecordEntity> getViewHolder(int viewType) {
        if (viewType == 1) {
            return new TaskProduceTaskItemViewHolder(context);
        } else if (viewType == 2) {
            return new ProcessProduceTaskItemViewHolder(context);
        } else {
            return new TaskProduceTaskItemViewHolder(context);
        }
    }

    @Override
    public int getItemViewType(int position, WaitPutinRecordEntity waitPutinRecordEntity) {
        if (WomConstant.SystemCode.RECORD_TYPE_TASK.equals(waitPutinRecordEntity.getRecordType().id)) {
            return 1;
        } else if (WomConstant.SystemCode.RECORD_TYPE_PROCESS.equals(waitPutinRecordEntity.getRecordType().id)) {
            return 2;
        } else if (WomConstant.SystemCode.RECORD_TYPE_ACTIVE.equals(waitPutinRecordEntity.getRecordType().id)) {
            return 3;
        } else {
            return -1;
        }
    }

    /**
     * TaskProduceTaskItemViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 指令单Item
     */
    class TaskProduceTaskItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

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
        @BindByTag("holdTv")
        TextView holdTv;
        @BindByTag("restartTv")
        TextView restartTv;
        @BindByTag("stopTv")
        TextView stopTv;
        @BindByTag("tvOperateLl")
        LinearLayout tvOperateLl;
        @BindByTag("expandIv")
        ImageView expandIv;
        @BindByTag("batchIv")
        ImageView batchIv;
        @BindByTag("dischargeTv")
        TextView dischargeTv;

        public TaskProduceTaskItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_produce_task_list;
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
                        if (WomConstant.SystemCode.RM_TYPE_SIMPLE.equals(getItem(getAdapterPosition()).getFormulaId().getSetProcess().id)){

                            if (WomConstant.SystemCode.EXE_STATE_PAUSED.equals(getItem(getAdapterPosition()).getExeState().id)){
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_task_paused));
                                return;
                            }
                            if (WomConstant.SystemCode.EXE_STATE_HOLD.equals(getItem(getAdapterPosition()).getExeState().id)){
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_task_held));
                                return;
                            }

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,getItem(getAdapterPosition()));
                            IntentRouter.go(context,Constant.Router.WOM_SIMPLE_ACTIVITY_LIST,bundle);
                        }else {
                            expandIv.performClick();
                        }
                    });
            initOperateViewListener(startTv);
            initOperateViewListener(holdTv);
            initOperateViewListener(restartTv);
            initOperateViewListener(stopTv);
            initOperateViewListener(dischargeTv);
            RxView.clicks(expandIv).throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        WaitPutinRecordEntity entity = getItem(getAdapterPosition());

                        if (WomConstant.SystemCode.EXE_STATE_HOLD.equals(entity.getExeState().id) || WomConstant.SystemCode.EXE_STATE_STOPPED.equals(entity.getExeState().id)
                         || WomConstant.SystemCode.EXE_STATE_ABANDONED.equals(entity.getExeState().id)){
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_process_view));
                            return;
                        }

                        List<WaitPutinRecordEntity> processEntityList = entity.getProcessWaitPutinRecordEntityList();
                        if (entity.isExpand()) {
                            getList().removeAll(processEntityList);
                            notifyItemRangeRemoved(getAdapterPosition() + 1, processEntityList.size());
                            notifyItemRangeChanged(getAdapterPosition(), processEntityList.size());
                        } else {
                            onItemChildViewClick(expandIv, getAdapterPosition(), entity);
                        }
                        entity.setExpand(!entity.isExpand());
                    });
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

            if (WomConstant.SystemCode.RM_TYPE_COMMON.equals(data.getFormulaId().getSetProcess().id)){ // 标准配方
                expandIv.setVisibility(View.VISIBLE);
                if (data.isExpand()) {
                    expandIv.setImageResource(R.drawable.ic_drop_up);
                } else {
                    expandIv.setImageResource(R.drawable.ic_drop_down);
                }
            }else { // 普通配方
                expandIv.setVisibility(View.GONE);
            }

            if (data.getTaskId().getBatchContral()){
                batchIv.setVisibility(View.VISIBLE);
            }else {
                batchIv.setVisibility(View.GONE);
            }

            tvOperateLl.setVisibility(View.VISIBLE);
            dischargeTv.setVisibility(View.GONE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) { // 待执行
                statusTv.setTextColor(context.getResources().getColor(R.color.status_orange));
//                workNum.setVisibility(View.GONE);
                startTv.setVisibility(View.VISIBLE);
                holdTv.setVisibility(View.GONE);
                restartTv.setVisibility(View.GONE);
                stopTv.setVisibility(View.GONE);
                timeCustomTv.setContent(data.getPlanStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getPlanStartTime()));
            } else if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getExeState().id)) { // 执行中
//                if (data.getPendingNum() == 0){
//                    mPendingController.queryPendingNum(data,workNum); // 待办查询
//                }else {
//                    workNum.setVisibility(View.VISIBLE);
//                }
                statusTv.setTextColor(context.getResources().getColor(R.color.wom_tv_status_color));
                startTv.setVisibility(View.GONE);
                holdTv.setVisibility(View.VISIBLE);
                restartTv.setVisibility(View.GONE);
                stopTv.setVisibility(View.VISIBLE);
                timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));

                if (data.getTaskId().getAdvanceCharge() && !data.getTaskId().getAdvanced()){
                    dischargeTv.setVisibility(View.VISIBLE);
                }else {
                    dischargeTv.setVisibility(View.GONE);
                }

            } else if (WomConstant.SystemCode.EXE_STATE_HOLD.equals(data.getExeState().id)) { // 已保持
                statusTv.setTextColor(context.getResources().getColor(R.color.status_blue));
                startTv.setVisibility(View.GONE);
                holdTv.setVisibility(View.GONE);
                restartTv.setVisibility(View.VISIBLE);
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

    /**
     * ProcessProduceTaskItemViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 工序Item
     */
    class ProcessProduceTaskItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

        @BindByTag("processNameCustomTv")
        CustomTextView processNameCustomTv;
        @BindByTag("exeSequenceTv")
        TextView exeSequenceTv;
        @BindByTag("equipmentCustomTv")
        CustomTextView equipmentCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("setEquipmentTv")
        TextView setEquipmentTv;
        @BindByTag("processStartTv")
        TextView processStartTv;
        @BindByTag("tvOperateLl")
        LinearLayout tvOperateLl;

        public ProcessProduceTaskItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_process_list;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,getItem(getAdapterPosition()));
                            IntentRouter.go(context,Constant.Router.WOM_ACTIVITY_LIST,bundle);
                        }
                    });
            RxView.clicks(processStartTv).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(processStartTv,getAdapterPosition(),getItem(getAdapterPosition())));
            RxView.clicks(setEquipmentTv).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(setEquipmentTv,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            processNameCustomTv.setContent(data.getProcessName());
            if(data.getTaskProcessId().getProcSort() == null){
                exeSequenceTv.setText("--");
            }
            else {
                exeSequenceTv.setText(String.valueOf(data.getTaskProcessId().getProcSort()));
            }
            equipmentCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getTaskProcessId().getActStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getTaskProcessId().getActStartTime()));
            if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()){
                tvOperateLl.setVisibility(View.GONE);
            }else {
                tvOperateLl.setVisibility(View.VISIBLE);
                if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)){
                    setEquipmentTv.setVisibility(View.GONE);
                    processStartTv.setText(context.getString(R.string.wom_start));
                    processStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
                }else if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getExeState().id)){
                    setEquipmentTv.setVisibility(View.VISIBLE);
                    processStartTv.setText(context.getString(R.string.wom_end));
                    processStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
                }
            }
        }
    }

}
