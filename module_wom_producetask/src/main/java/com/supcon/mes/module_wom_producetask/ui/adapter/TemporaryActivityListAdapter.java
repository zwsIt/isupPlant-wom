package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
 * Desc 机动活动Adapter
 */
public class TemporaryActivityListAdapter extends BaseListDataRecyclerViewAdapter<WaitPutinRecordEntity> {
    public TemporaryActivityListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<WaitPutinRecordEntity> getViewHolder(int viewType) {
        if (viewType == 1) {
            return new RoutineItemViewHolder(context);
        } else if (viewType == 2) {
            return new CheckItemViewHolder(context);
        } else if (viewType == 3) {
            return new PutInItemViewHolder(context);
        } else if (viewType == 5) {
            return new QualityItemViewHolder(context);
        } else if (viewType == 6) {
            return new OutputItemViewHolder(context);
        } else {
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
            return R.layout.wom_item_temporary_routine;
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
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)){
                routineStartTv.setText(context.getResources().getString(R.string.wom_start));
                routineStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            }else {
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
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(getItem(getAdapterPosition()).getExeState().id)){
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
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
                    .subscribe(o -> onItemChildViewClick(checkStartTv,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            activityNameTv.setText(data.getActiveName());
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            sequenceCustomTv.setContent(data.getTaskActiveId().getExecSort());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)){
                checkStartTv.setText(context.getResources().getString(R.string.wom_start));
                checkStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            }else {
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
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("putInStartTv")
        TextView putInStartTv;
        @BindByTag("agileIv")
        ImageView agileIv;

        public PutInItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_temporary_put_in;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(getItem(getAdapterPosition()).getExeState().id)){
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        if (getItem(getAdapterPosition()).getAgile()){
                            IntentRouter.go(context, Constant.Router.WOM_PUT_IN_AGILE_REPORT, bundle);
                        }else {
                            IntentRouter.go(context, Constant.Router.WOM_PUT_IN_REPORT, bundle);
                        }
                    });
            RxView.clicks(putInStartTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(putInStartTv,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            if (data.getAgile()){ // 灵活投料
                materialNameTv.setText(data.getActiveName());
                agileIv.setVisibility(View.VISIBLE);
            }else {
                materialNameTv.setText(data.getTaskActiveId().getMaterialId().getName());
                agileIv.setVisibility(View.GONE);
            }
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            numCustomTv.setContent(data.getTaskActiveId().getPlanQuantity() == null ? "" : data.getTaskActiveId().getPlanQuantity().toString());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)){
                putInStartTv.setText(context.getResources().getString(R.string.wom_start));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            }else {
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
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("putInStartTv")
        TextView putInStartTv;
        @BindByTag("agileIv")
        ImageView agileIv;

        public OutputItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_temporary_put_in;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(getItem(getAdapterPosition()).getExeState().id)){
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_first_start_activity));
                                return false;
                            }
                            return true;
                        }
                    })
                    .subscribe(o -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, getItem(getAdapterPosition()));
                        if (getItem(getAdapterPosition()).getAgile()){
                            IntentRouter.go(context, Constant.Router.WOM_OUTPUT_AGILE_REPORT, bundle);
                        }else {
                            IntentRouter.go(context, Constant.Router.WOM_OUTPUT_REPORT, bundle);
                        }
                    });
            RxView.clicks(putInStartTv).throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(putInStartTv,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(WaitPutinRecordEntity data) {
            if (data.getAgile()){ // 灵活
                materialNameTv.setText(data.getActiveName());
                agileIv.setVisibility(View.VISIBLE);
            }else {
                materialNameTv.setText(data.getTaskActiveId().getMaterialId().getName());
                agileIv.setVisibility(View.GONE);
            }
            activityTypeTv.setText(data.getTaskActiveId().getActiveType().value);
            numCustomTv.setContent(data.getTaskActiveId().getPlanQuantity() == null ? "" : data.getTaskActiveId().getPlanQuantity().toString());
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)){
                putInStartTv.setText(context.getResources().getString(R.string.wom_start));
                putInStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            }else {
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
        @BindByTag("factoryModelUnitCustomTv")
        CustomTextView factoryModelUnitCustomTv;
        @BindByTag("timeCustomTv")
        CustomTextView timeCustomTv;
        @BindByTag("routineStartTv")
        TextView routineStartTv;

        public QualityItemViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_temporary_routine;
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
            factoryModelUnitCustomTv.setContent(data.getTaskProcessId().getEquipmentId().getName());
            timeCustomTv.setContent(data.getActualStartTime() == null ? "" : DateUtil.dateTimeFormat(data.getActualStartTime()));
            if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(data.getExeState().id)){
                routineStartTv.setVisibility(View.VISIBLE);
                routineStartTv.setText(context.getResources().getString(R.string.wom_start));
                routineStartTv.setBackgroundResource(R.drawable.wom_sh_start_bg);
            }else {
                routineStartTv.setVisibility(View.GONE);
                routineStartTv.setText(context.getResources().getString(R.string.wom_end));
                routineStartTv.setBackgroundResource(R.drawable.wom_sh_end_bg);
            }
        }

    }

}
