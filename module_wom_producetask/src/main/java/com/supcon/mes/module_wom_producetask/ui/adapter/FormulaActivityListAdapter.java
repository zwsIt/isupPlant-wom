package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Predicate;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配方活动Adapter
 */
public class FormulaActivityListAdapter extends BaseListDataRecyclerViewAdapter<WaitPutinRecordEntity> {
    public FormulaActivityListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitPutinRecordEntity> getViewHolder(int viewType) {

        switch (viewType) {
            case 2:
                return new CheckItemViewHolder(context);
            case 3:
            case 7:
                return new PutInItemViewHolder(context);
            case 4:
            case 8:
                return new BatchPutInItemViewHolder(context);
            /*case 5:
                return new QualityItemViewHolder(context);*/
            case 6:
            case 9:
                return new OutputItemViewHolder(context);
            default:
                return new RoutineItemViewHolder(context);
        }
    }

    @Override
    public int getItemViewType(int position, WaitPutinRecordEntity waitPutinRecordEntity) {
        if (WomConstant.SystemCode.RM_activeType_COMMON.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 1;
        } else if (WomConstant.SystemCode.RM_activeType_CHECK.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 2;
        } else if (WomConstant.SystemCode.RM_activeType_PUTIN.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 3;
        } else if (WomConstant.SystemCode.RM_activeType_BATCH_PUTIN.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 4;
        } else if (WomConstant.SystemCode.RM_activeType_QUALITY.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 5;
        } else if (WomConstant.SystemCode.RM_activeType_OUTPUT.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 6;
        } else if (WomConstant.SystemCode.RM_activeType_PIPE_PUTIN.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 7;
        } else if (WomConstant.SystemCode.RM_activeType_PIPE_BATCH_PUTIN.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 8;
        } else if (WomConstant.SystemCode.RM_activeType_PIPE_OUTPUT.equals(waitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
            return 9;
        } else {
            return -1;
        }
    }

    /**
     * RoutineItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 常规Item
     */
    class RoutineItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

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
        @BindByTag("routineStartTv")
        TextView routineStartTv;

        public RoutineItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_routine;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(o -> WomConstant.SystemCode.RM_TYPE_SIMPLE.equals(getItem(getAdapterPosition()).getFormulaId().getSetProcess().id))
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
//                        IntentRouter.go(context, Constant.Router.WOM_ACTIVITY_LIST, bundle);
                    });
            RxView.clicks(routineStartTv).throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        WaitPutinRecordEntity entity = getItem(getAdapterPosition());
                        onItemChildViewClick(routineStartTv, getAdapterPosition(), entity);
                    });
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            activityNameTv.setText(data.getActiveName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                routineStartTv.setText(context.getResources().getString(R.string.wom_start));
                routineStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            } else {
                routineStartTv.setText(context.getResources().getString(R.string.wom_end));
                routineStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
            }
        }

    }

    /**
     * CheckItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 检查Item
     */
    class CheckItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

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
        @BindByTag("checkStartTv")
        TextView checkStartTv;

        public CheckItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_check;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            WaitPutinRecordEntity data = getItem(getAdapterPosition());
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
                                return false;
                            }
                            if (data.getProcReportId().getDetailsIsNull()){ // 是否存在检查明细
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.WOM_CHECK_LIST, bundle);
                    });
            RxView.clicks(checkStartTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(checkStartTv, getAdapterPosition(), getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            activityNameTv.setText(data.getActiveName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                checkStartTv.setText(context.getResources().getString(R.string.wom_start));
                checkStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            } else {
                checkStartTv.setText(context.getResources().getString(R.string.wom_end));
                checkStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
            }
        }
    }

    /**
     * PutInItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 人工投料Item
     */
    class PutInItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("activityTypeTv")
        TextView activityTypeTv;
        @BindByTag("sequenceCustomTv")
        CustomTextView sequenceCustomTv;
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("putInStartTv")
        TextView putInStartTv;

        public PutInItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_put_in;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(getItem(getAdapterPosition()).getExeState().id)) {
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.WOM_PUT_IN_REPORT, bundle);
                    });
            RxView.clicks(putInStartTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(putInStartTv, getAdapterPosition(), getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            materialNameTv.setText(data.getTaskActiveId().getMaterialId().getName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            numCustomTv.setContent(data.getTaskActiveId().getPlanQuantity() == null ? "" : data.getTaskActiveId().getPlanQuantity().toPlainString());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                putInStartTv.setText(context.getResources().getString(R.string.wom_start));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            } else {
                putInStartTv.setText(context.getResources().getString(R.string.wom_end));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
            }

        }
    }

    /**
     * BatchPutInItemViewHolder
     * created by zhangwenshuai1 2020/4/10
     * desc 人工投配料Item
     */
    class BatchPutInItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("activityTypeTv")
        TextView activityTypeTv;
        @BindByTag("sequenceCustomTv")
        CustomTextView sequenceCustomTv;
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("putInStartTv")
        TextView putInStartTv;

        public BatchPutInItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_put_in;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(getItem(getAdapterPosition()).getExeState().id)) {
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.WOM_BATCH_PUT_IN_REPORT, bundle);
                    });
            RxView.clicks(putInStartTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(putInStartTv, getAdapterPosition(), getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            materialNameTv.setText(data.getActiveName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            numCustomTv.setContent(data.getTaskActiveId().getPlanQuantity() == null ? "" : data.getTaskActiveId().getPlanQuantity().toPlainString());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            putInStartTv.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                if(data.getTaskId().getBatchContral()!=null && data.getTaskId().getBatchContral()){
                    putInStartTv.setVisibility(View.GONE);
                }
                else {
                    putInStartTv.setText(context.getResources().getString(R.string.wom_start));
                    putInStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
                }
            } else {
                putInStartTv.setText(context.getResources().getString(R.string.wom_end));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
            }

        }
    }

    /**
     * OutputItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 人工产出Item
     */
    class OutputItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

        @BindByTag("materialNameTv")
        TextView materialNameTv;
        @BindByTag("activityTypeTv")
        TextView activityTypeTv;
        @BindByTag("sequenceCustomTv")
        CustomTextView sequenceCustomTv;
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("putInStartTv")
        TextView putInStartTv;

        public OutputItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_put_in;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(getItem(getAdapterPosition()).getExeState().id)) {
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.WOM_OUTPUT_REPORT, bundle);
                    });
            RxView.clicks(putInStartTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(putInStartTv, getAdapterPosition(), getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            materialNameTv.setText(data.getTaskActiveId().getMaterialId().getName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            numCustomTv.setContent(data.getTaskActiveId().getPlanQuantity() == null ? "" : data.getTaskActiveId().getPlanQuantity().toPlainString());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                putInStartTv.setText(context.getResources().getString(R.string.wom_start));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            } else {
                putInStartTv.setText(context.getResources().getString(R.string.wom_end));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
            }

        }
    }

    /* *//**
     * QualityItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 检验Item
     *//*
    class QualityItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

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
        @BindByTag("expandIv")
        ImageView expandIv;

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
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(o -> WomConstant.SystemCode.RM_TYPE_SIMPLE.equals(getItem(getAdapterPosition()).getFormulaId().getSetProcess().id))
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.WOM_ACTIVITY_LIST, bundle);
                    });
            initOperateViewListener(startTv);
            initOperateViewListener(pauseTv);
            initOperateViewListener(resumeTv);
            initOperateViewListener(stopTv);
            RxView.clicks(expandIv).throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        WaitPutinRecordEntity entity = getItem(getAdapterPosition());

                        if (WomConstant.SystemCode.EXE_STATE_PAUSED.equals(entity.getExeState().id) || WomConstant.SystemCode.EXE_STATE_STOPPED.equals(entity.getExeState().id)
                                || WomConstant.SystemCode.EXE_STATE_ABANDONED.equals(entity.getExeState().id)) {
                            ToastUtils.show(context, "当前工单状态下，无工序查看");
                            return;
                        }

                        List<WaitPutinRecordEntity> processEntityList = entity.getProcessWaitPutinRecordEntityList();
                        if (entity.isExpand()) {
                            getList().removeAll(processEntityList);
                            notifyItemRangeRemoved(getAdapterPosition() + 1, processEntityList.size());
                            notifyItemRangeChanged(getAdapterPosition(), processEntityList.size());
//                            processEntityList.clear();
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

            if (WomConstant.SystemCode.RM_TYPE_COMMON.equals(data.getFormulaId().getSetProcess().id)) { // 标准配方
                expandIv.setVisibility(View.VISIBLE);
                if (data.isExpand()) {
                    expandIv.setImageResource(R.drawable.ic_drop_up);
                } else {
                    expandIv.setImageResource(R.drawable.ic_drop_down);
                }
            } else { // 普通配方
                expandIv.setVisibility(View.GONE);
            }

            tvOperateLl.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) { // 待执行
                statusTv.setTextColor(context.getResources().getColor(R.color.status_orange));
//                workNum.setVisibility(View.GONE);
                startTv.setVisibility(View.VISIBLE);
                pauseTv.setVisibility(View.GONE);
                resumeTv.setVisibility(View.GONE);
                stopTv.setVisibility(View.GONE);
                timeCustomTv.setContent(data.getTaskId().getPlanStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getTaskId().getPlanStartTime()));
            } else if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getExeState().id)) { // 执行中
//                if (data.getPendingNum() == 0){
//                    mPendingController.queryPendingNum(data,workNum); // 待办查询
//                }else {
//                    workNum.setVisibility(View.VISIBLE);
//                }
                statusTv.setTextColor(context.getResources().getColor(R.color.wom_tv_status_color));
                startTv.setVisibility(View.GONE);
                pauseTv.setVisibility(View.VISIBLE);
                resumeTv.setVisibility(View.GONE);
                stopTv.setVisibility(View.VISIBLE);
                timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
                timeCustomTv.setContent(data.getTaskId().getActStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getTaskId().getActStartTime()));
            } else if (WomConstant.SystemCode.EXE_STATE_PAUSED.equals(data.getExeState().id)) { // 已暂停
                statusTv.setTextColor(context.getResources().getColor(R.color.status_blue));
                startTv.setVisibility(View.GONE);
                pauseTv.setVisibility(View.GONE);
                resumeTv.setVisibility(View.VISIBLE);
                stopTv.setVisibility(View.VISIBLE);
                timeCustomTv.setContent(data.getTaskId().getActStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getTaskId().getActStartTime()));
            } else {
                statusTv.setTextColor(context.getResources().getColor(R.color.red));
                tvOperateLl.setVisibility(View.GONE);
                timeCustomTv.setContent(data.getTaskId().getActStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getTaskId().getActStartTime()));
            }
        }

        *//**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/25
     * @description 操作按钮监听事件
     *//*
        @SuppressLint("CheckResult")
        private void initOperateViewListener(TextView view) {
            RxView.clicks(view)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(o -> onItemChildViewClick(view, getAdapterPosition(), getItem(getAdapterPosition())));
        }
    }*/

}
