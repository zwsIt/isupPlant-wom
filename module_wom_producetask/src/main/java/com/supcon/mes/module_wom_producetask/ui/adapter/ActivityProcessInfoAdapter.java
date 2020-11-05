package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.TaskActiveEntity;

/**
 *
 */
public class ActivityProcessInfoAdapter extends BaseListDataRecyclerViewAdapter<TaskActiveEntity> {

    public ActivityProcessInfoAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<TaskActiveEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<TaskActiveEntity> {


        @BindByTag("index")
        TextView index;
        @BindByTag("itemAreaDot")
        ImageView itemAreaDot;
        @BindByTag("itemAreaLineBottom")
        View itemAreaLineBottom;
        @BindByTag("itemAreaDotLayout")
        LinearLayout itemAreaDotLayout;
        @BindByTag("activityName")
        TextView activityName;
        @BindByTag("material")
        TextView material;
        @BindByTag("dealTime")
        TextView dealTime;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_activity_process_info;
        }

        @Override
        protected void initListener() {
            super.initListener();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void update(TaskActiveEntity data) {
//            if (getAdapterPosition() == getListSize() - 1) {
//                itemAreaLineBottom.setVisibility(View.INVISIBLE);
//            }
            index.setText(data.getExecSort());
            activityName.setText(String.format("%s：%s", data.getActiveType().value, data.getName()));
            if (WomConstant.SystemCode.EXE_STATE_END.equals(data.getRunState().id)){
                index.setTextColor(context.getResources().getColor(R.color.gray_text));
                activityName.setTextColor(context.getResources().getColor(R.color.gray_text));
                itemAreaDot.setImageResource(R.drawable.ic_point_end);
                dealTime.setText(DateUtil.dateFormat(data.getActEndTime(), "MM-dd HH:mm"));
            }else if (WomConstant.SystemCode.EXE_STATE_ING.equals(data.getRunState().id)){
                index.setTextColor(context.getResources().getColor(R.color.wom_tv_status_color));
                activityName.setTextColor(context.getResources().getColor(R.color.wom_tv_status_color));
                itemAreaDot.setImageResource(R.drawable.ic_point_ing);
                dealTime.setText(DateUtil.dateFormat(data.getActStartTime(), "MM-dd HH:mm"));
            }else {
                index.setTextColor(context.getResources().getColor(R.color.black));
                activityName.setTextColor(context.getResources().getColor(R.color.black));
                itemAreaDot.setImageResource(R.drawable.ic_point);
                dealTime.setText("--");
            }

            if (data.getMaterialId() != null && !TextUtils.isEmpty(data.getMaterialId().getCode())){
                material.setText(String.format("%s(%s)", data.getMaterialId().getName(), data.getMaterialId().getCode()));
            }else {
                material.setText("--");
            }

//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("进行了"+data.dealStaff +"操作，处理结果" + data.operateDes);
//            String html = "进行了<font color='#366CBC'>"+data.activityName+"</font>操作，处理结果<font color='#366CBC'>" + data.operateDes + "</font>";
//            activityName.setText(Html.fromHtml(html));
//            if (TextUtils.isEmpty(data.dealAdvice)){
//                dealAdvice.setVisibility(View.GONE);
//            }else {
//                dealAdvice.setVisibility(View.VISIBLE);
//                dealAdvice.setText(String.format("意见：%s", data.dealAdvice));
//            }
//            dealTime.setText(DateUtil.dateFormat(data.dealTime, Constant.TimeString.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        }
    }
}
