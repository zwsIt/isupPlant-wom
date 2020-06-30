package com.supcon.mes.module_wom_preparematerial.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.module_wom_preparematerial.R;

import java.util.List;


/**
 * Created by wangshizhan on 2017/8/16.
 * Email:wangshizhan@supcon.com
 */

public class RejectReasonAdapter extends BaseListDataRecyclerViewAdapter<String> {

    private int textSize;
    private int textColor;
    private int position;
    private boolean isGrid = true;

    public RejectReasonAdapter(Context context) {
        super(context);

    }


    public RejectReasonAdapter(Context context, boolean isGrid) {
        this(context);
        this.isGrid = isGrid;
    }

    public RejectReasonAdapter(Context context, List<String> list){
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<String> getViewHolder(int viewType) {

        if(isGrid){
            return new FilterViewHolder(context);
        }
        else{
            return new FilterViewHolder(context);
        }


    }


    public void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    class FilterViewHolder extends BaseRecyclerViewHolder<String> implements View.OnClickListener{

        @BindByTag("rejectReasonName")
        TextView rejectReasonName;

        public FilterViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            rejectReasonName = itemView.findViewById(R.id.rejectReasonName);
            if(textSize!=0){
                rejectReasonName.setTextSize(textSize);
            }

            if(textColor!=0){
                rejectReasonName.setTextColor(textColor);
            }
        }

        @Override
        protected void initListener() {
            super.initListener();

            rejectReasonName.setOnClickListener(this);

        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_reject_reason;
        }

        @Override
        protected void update(String data) {

            if(getAdapterPosition() == position){
                if(textColor!=0){
                    rejectReasonName.setTextColor(textColor);
                }
                else{
                    rejectReasonName.setTextColor(context.getResources().getColor(R.color.white));
                    rejectReasonName.setBackgroundResource(R.drawable.sh_reject_reason_checked);
                }

//                filterName.setBackgroundResource(R.drawable.sh_filter_light_blue);
            }
            else{
                rejectReasonName.setTextColor(context.getResources().getColor(R.color.dark_text99));
//                filterName.setBackgroundResource(R.drawable.sl_transparent_press_no_stroke);
                rejectReasonName.setBackgroundResource(R.drawable.sh_reject_reason_uncheck);
            }
            rejectReasonName.setText(data);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            RejectReasonAdapter.this.position = position;
            String reason = getItem(position);
            onItemChildViewClick(v, 0, reason);
            notifyDataSetChanged();
        }
    }


    class LinearFilterViewHolder extends BaseRecyclerViewHolder<String> implements View.OnClickListener{

        @BindByTag("rejectReasonName")
        TextView rejectReasonName;

        public LinearFilterViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initView() {
            super.initView();
            rejectReasonName = itemView.findViewById(R.id.rejectReasonName);
            if(textSize!=0){
                rejectReasonName.setTextSize(textSize);
            }

            if(textColor!=0){
                rejectReasonName.setTextColor(textColor);
            }
        }

        @Override
        protected void initListener() {
            super.initListener();

            rejectReasonName.setOnClickListener(this);

        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_reject_reason;
        }

        @Override
        protected void update(String data) {

            if(getAdapterPosition() == position){
                rejectReasonName.setTextColor(context.getResources().getColor(com.supcon.mes.mbap.R.color.filterTextBlue));
                rejectReasonName.setBackgroundResource(com.supcon.mes.mbap.R.drawable.sh_filter_light_blue);
            }
            else{
                if(textColor!=0){
                    rejectReasonName.setTextColor(textColor);
                }
                else{
                    rejectReasonName.setTextColor(context.getResources().getColor(com.supcon.mes.mbap.R.color.textColorlightblack));
                }
                rejectReasonName.setBackgroundResource(com.supcon.mes.mbap.R.drawable.sh_filter_gray);
            }
            rejectReasonName.setText(data);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            RejectReasonAdapter.this.position = position;
            String reason = getItem(position);
            onItemChildViewClick(v, 0, reason);

        }
    }
}
