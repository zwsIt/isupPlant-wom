package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
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
        @BindByTag("activityName")
        TextView activityName;
        @BindByTag("activeType")
        TextView activeType;
        @BindByTag("materialName")
        CustomTextView materialName;
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("materialBatchNumTv")
        TextView materialBatchNumTv;
        @BindByTag("batchNum")
        CustomTextView batchNum;
        @BindByTag("checkResult")
        CustomTextView checkResult;
        @BindByTag("taskProcessName")
        CustomTextView taskProcessName;
        @BindByTag("time")
        CustomTextView time;
        @BindByTag("taskState")
        TextView taskState;
        @BindByTag("materialNameLl")
        LinearLayout materialNameLl;
        @BindByTag("materialCodeLl")
        LinearLayout materialCodeLl;
        @BindByTag("batchNumLl")
        LinearLayout batchNumLl;
        @BindByTag("checkResultLl")
        LinearLayout checkResultLl;
        @BindByTag("taskProcessNameLl")
        LinearLayout taskProcessNameLl;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_active_execute_record;
        }

        @Override
        protected void update(ActiExelogEntity data) {
            activeType.setText(data.activeType.value);
            activityName.setText(TextUtils.isEmpty(data.name) ? "--" : data.name);
            if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getRunState().id)){
                taskState.setText(data.getRunState().value);
                taskState.setTextColor(context.getResources().getColor(R.color.lubricateBg));
//                taskState.setBackgroundResource(R.drawable.sh_status);
                time.setKey(context.getResources().getString(R.string.middleware_start_time));
                time.setContent(DateUtil.dateTimeFormat(data.getActStartTime()));
            }else {
                taskState.setText(data.getRunState().value);
                taskState.setTextColor(context.getResources().getColor(R.color.listview_divider));
//                taskState.setBackgroundResource(R.drawable.sh_status);
                time.setKey(context.getResources().getString(R.string.middleware_end_time));
                time.setContent(DateUtil.dateTimeFormat(data.getActEndTime()));
            }
            if (data.getMaterialId() == null || data.getMaterialId().getId() == null /* && data.getMaterialBatchNum() != null*/) {
                materialNameLl.setVisibility(View.GONE);
                materialCodeLl.setVisibility(View.GONE);
                batchNumLl.setVisibility(View.GONE);
            } else {
                materialNameLl.setVisibility(View.VISIBLE);
                materialCodeLl.setVisibility(View.VISIBLE);
                batchNumLl.setVisibility(View.VISIBLE);

                materialName.setContent(data.getMaterialId().getName());
                materialCode.setContent(data.getMaterialId().getCode());
                batchNum.setContent(TextUtils.isEmpty(data.materialBatchNum) ? "--" : data.materialBatchNum);
            }

            if (WomConstant.SystemCode.RM_activeType_QUALITY.equals(data.activeType.id)){
                checkResultLl.setVisibility(View.VISIBLE);
                checkResult.setContent(data.getCheckResult());
            }else {
                checkResultLl.setVisibility(View.GONE);
            }

            if (WomConstant.SystemCode.RM_TYPE_COMMON.equals(data.getTaskId().getFormulaId().getSetProcess().id)){
                taskProcessNameLl.setVisibility(View.VISIBLE);
                taskProcessName.setContent(data.getTaskProcessId()== null ? "--" : data.getTaskProcessId().getName());
            }else {
                taskProcessNameLl.setVisibility(View.GONE);
            }

        }
    }


}
