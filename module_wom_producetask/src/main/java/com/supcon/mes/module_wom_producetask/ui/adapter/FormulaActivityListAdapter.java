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
            case 5:
                return new QualityItemViewHolder(context);
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
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
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
//            routineStartTv.setEnabled(true);
            routineStartTv.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()) {
                    routineStartTv.setVisibility(View.GONE);
//                    routineStartTv.setBackgroundResource(R.drawable.wom_sh_disable_bg);
//                    routineStartTv.setText(context.getResources().getString(R.string.wom_end));
//                    routineStartTv.setEnabled(false);
                } else {
                    routineStartTv.setText(context.getResources().getString(R.string.wom_start));
                    routineStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
                }
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
                            if (data.getProcReportId().getDetailsIsNull()) { // 是否存在检查明细
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
//            checkStartTv.setEnabled(true);
            checkStartTv.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()) {
                    checkStartTv.setVisibility(View.GONE);
//                    checkStartTv.setBackgroundResource(R.drawable.wom_sh_disable_bg);
//                    checkStartTv.setEnabled(false);
//                    checkStartTv.setText(context.getResources().getString(R.string.wom_end));
                } else {
                    checkStartTv.setText(context.getResources().getString(R.string.wom_start));
                    checkStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
                }
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
//            putInStartTv.setEnabled(true);
            putInStartTv.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()) {
                    putInStartTv.setVisibility(View.GONE);
//                    putInStartTv.setBackgroundResource(R.drawable.wom_sh_disable_bg);
//                    putInStartTv.setEnabled(false);
//                    putInStartTv.setText(context.getResources().getString(R.string.wom_end));
                } else {
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
                if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()) {
                    putInStartTv.setVisibility(View.GONE);
//                    putInStartTv.setBackgroundResource(R.drawable.wom_sh_disable_bg);
//                    putInStartTv.setEnabled(false);
//                    putInStartTv.setText(context.getResources().getString(R.string.wom_end));
                } else {
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
//            putInStartTv.setEnabled(true);
            putInStartTv.setVisibility(View.VISIBLE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()) {
                    putInStartTv.setVisibility(View.GONE);
//                    putInStartTv.setBackgroundResource(R.drawable.wom_sh_disable_bg);
//                    putInStartTv.setEnabled(false);
//                    putInStartTv.setText(context.getResources().getString(R.string.wom_end));
                } else {
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
     * QualityItemViewHolder
     * created by zhangwenshuai1 2020/4/7
     * desc 检验Item
     */
    class QualityItemViewHolder extends BaseRecyclerViewHolder<WaitPutinRecordEntity> {

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
        @BindByTag("qualityStartTv")
        TextView qualityStartTv;
        @BindByTag("itemStateTv")
        TextView itemStateTv;
        @BindByTag("adjustLl")
        LinearLayout adjustLl;
        @BindByTag("qualityTimes")
        CustomTextView qualityTimes;

        public QualityItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_quality;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(o -> WomConstant.SystemCode.BASE_DEAL_ADJUST.equals(getItem(getAdapterPosition()).getTaskActiveId().getActiveBatchState().getDealType().id))
                    .subscribe(o -> {
                        // 调整
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        IntentRouter.go(context, Constant.Router.WOM_ADJUST_ACTIVITY_LIST, bundle);
                    });
            RxView.clicks(qualityStartTv).throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        WaitPutinRecordEntity entity = getItem(getAdapterPosition());
                        onItemChildViewClick(qualityStartTv, getAdapterPosition(), entity);
                    });
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            activityNameTv.setText(data.getActiveName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            qualityTimes.setContent(String.valueOf(data.getTaskActiveId().getCheckTimes()));
//            routineStartTv.setEnabled(true);
            qualityStartTv.setVisibility(View.GONE);
            adjustLl.setVisibility(View.GONE);
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)) {
                itemStateTv.setVisibility(View.GONE);
                if (data.getTaskId().getBatchContral() != null && data.getTaskId().getBatchContral()) { // 批控
//                    routineStartTv.setBackgroundResource(R.drawable.wom_sh_disable_bg);
//                    routineStartTv.setText(context.getResources().getString(R.string.wom_end));
//                    routineStartTv.setEnabled(false);
                } else {
                    qualityStartTv.setVisibility(View.VISIBLE);
                    qualityStartTv.setText(context.getResources().getString(R.string.wom_start));
                    qualityStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
                }
            } else {
                itemStateTv.setVisibility(View.VISIBLE);
                itemStateTv.setText(TextUtils.isEmpty(data.getCheckState()) ? data./*getTaskActiveId().*/getCheckState() : data.getCheckState()); // 检验状态

                // 是否完工检验且允许提前放行
                if (data.getTaskActiveId().getFinalInspection() && data.getTaskActiveId().isRelease()){
                    qualityStartTv.setVisibility(View.VISIBLE);
                    qualityStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
                    qualityStartTv.setText(context.getResources().getString(R.string.wom_advance_release));
                }
                // 调整
                if (WomConstant.SystemCode.BASE_DEAL_ADJUST.equals(data.getActiveBatchState().getDealType().id)){
                    adjustLl.setVisibility(View.VISIBLE);
                }

            }
        }

    }
}
