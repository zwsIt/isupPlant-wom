package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogEntity;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ActiExeLogAdapter extends BaseListDataRecyclerViewAdapter<ActiExelogEntity> {
    public ActiExeLogAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ActiExelogEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<ActiExelogEntity> {

        @BindByTag("activeType")
        TextView activeType;
        @BindByTag("dataSourceTv")
        TextView dataSourceTv;
        @BindByTag("activityName")
        CustomTextView activityName;
        @BindByTag("taskState")
        TextView taskState;
        @BindByTag("materialName")
        CustomTextView materialName;
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("materialBatchNum")
        CustomTextView materialBatchNum;
        @BindByTag("checkResult")
        CustomTextView checkResult;
        @BindByTag("taskProcessName")
        CustomTextView taskProcessName;
        @BindByTag("time")
        CustomTextView time;
        @BindByTag("materialRl")
        RelativeLayout materialRl;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_actiexelog;
        }

        @Override
        protected void update(ActiExelogEntity data) {
            activeType.setText(data.activeType.value);
            dataSourceTv.setText(data.exeSystem == null ? "--" : data.exeSystem.value);
            if (data.exeSystem != null && WomConstant.SystemCode.EXE_SYSTEM_BATCH.equals(data.exeSystem.id)){
                activeType.setBackgroundResource(R.drawable.sh_actilog_batch_bg);
                dataSourceTv.setBackgroundResource(R.drawable.sh_datasource_batch_bg);
                dataSourceTv.setTextColor(context.getResources().getColor(R.color.active_log_batch_start));
            }else {
                activeType.setBackgroundResource(R.drawable.sh_actilog_mes_bg);
                dataSourceTv.setBackgroundResource(R.drawable.sh_datasource_mes_bg);
                dataSourceTv.setTextColor(context.getResources().getColor(R.color.active_log_mes_start));
            }
            activityName.setContent(TextUtils.isEmpty(data.name) ? "--" : data.name);
            if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getRunState().id)){
                taskState.setText("执行中");
                taskState.setTextColor(context.getResources().getColor(R.color.lubricateBg));
                taskState.setBackgroundResource(R.drawable.sh_actilog_state_bg);
                time.setKey("开始时间");
                time.setContent(DateUtil.dateTimeFormat(data.getActStartTime()));
            }else {
                taskState.setText("已结束");
                taskState.setTextColor(context.getResources().getColor(R.color.listview_divider));
                taskState.setBackgroundResource(R.drawable.sh_co_product_bg);
                time.setKey("结束时间");
                time.setContent(DateUtil.dateTimeFormat(data.getActlongTime()));
            }
            if (data.getMaterialId() == null || data.getMaterialId().getId() == null /* && data.getMaterialBatchNum() != null*/) {
                materialRl.setVisibility(View.GONE);
            } else {
                materialRl.setVisibility(View.VISIBLE);
                materialName.setContent(data.getMaterialId().getName());
                materialCode.setContent(data.getMaterialId().getCode());
                materialBatchNum.setContent(TextUtils.isEmpty(data.materialBatchNum) ? "--" : data.materialBatchNum);
            }

            if (WomConstant.SystemCode.RM_activeType_QUALITY.equals(data.activeType.id)){
                checkResult.setVisibility(View.VISIBLE);
                checkResult.setContent(data.getCheckResult());
            }else {
                checkResult.setVisibility(View.GONE);
            }

            if (WomConstant.SystemCode.RM_TYPE_COMMON.equals(data.getTaskId().getFormulaId().getSetProcess().id)){
                taskProcessName.setVisibility(View.VISIBLE);
                taskProcessName.setContent(data.getTaskProcessId()== null ? "--" : data.getTaskProcessId().getName());
            }else {
                taskProcessName.setVisibility(View.GONE);
            }

        }
    }


}
