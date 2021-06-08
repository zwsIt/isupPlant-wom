package com.supcon.mes.module_wom_batchmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionEntity;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchInstructionListActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * @author created by zhangwenshuai1
 * @desc 配料指令
 * @date 2021/5/21
 */
public class BatchInstructionListAdapter extends BaseListDataRecyclerViewAdapter<BatchInstructionEntity> {

    public BatchInstructionListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BatchInstructionEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    class ViewHolder extends BaseRecyclerViewHolder<BatchInstructionEntity> {

        @BindByTag("index")
        TextView index;
        @BindByTag("itemAreaLineTop")
        View itemAreaLineTop;
        @BindByTag("itemAreaDot")
        ImageView itemAreaDot;
        @BindByTag("itemAreaLineBottom")
        View itemAreaLineBottom;
        @BindByTag("itemAreaDotLayout")
        LinearLayout itemAreaDotLayout;
        @BindByTag("batchArea")
        TextView batchArea;
        @BindByTag("material")
        TextView material;
        @BindByTag("dealTime")
        TextView dealTime;
        @BindByTag("statusTv")
        TextView statusTv;
        @BindByTag("intoIv")
        ImageView intoIv;

        public ViewHolder(Context context) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.batch_item_instruction_list;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(@NonNull Object o) throws Exception {
                            return intoIv.getVisibility() == View.VISIBLE;
                        }
                    })
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(BmConstant.IntentKey.BATCH_MATERIAL_INSTRUCTION,getItem(getAdapterPosition()));
                            bundle.putString(BmConstant.IntentKey.BUCKET_CODE,((BatchInstructionListActivity)context).getBucketCode());
                            IntentRouter.go(context, Constant.Router.BATCH_MATERIAL_INSTRUCTION_EDIT,bundle);
                        }
                    });
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void update(BatchInstructionEntity data) {

            if (getAdapterPosition() == 0) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                layoutParams.topMargin = DisplayUtil.dip2px(10, context);
                itemView.setLayoutParams(layoutParams);
            }
            if (getAdapterPosition() == getItemCount() -1) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                layoutParams.bottomMargin = DisplayUtil.dip2px(10, context);
                itemView.setLayoutParams(layoutParams);
            }else {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                layoutParams.bottomMargin = 0;
                itemView.setLayoutParams(layoutParams);
            }

            index.setText(String.valueOf(data.getPlOrder()));
            batchArea.setText(/*data.getAreaMange().getName()*/(data.getActualNumber() == null ? "0" : data.getActualNumber()) + "/" + data.getPlanNumber());

            if (getAdapterPosition() == 0) {
                itemAreaLineTop.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                itemAreaLineTop.setBackgroundColor(context.getResources().getColor(R.color.wom_blue_5DB7FF));
            }

            if (data.getCompleteTime() != null) {
                // 结束
                material.setTextColor(context.getResources().getColor(R.color.wom_black_333333));
                itemAreaDot.setImageResource(R.drawable.wom_ic_node_blue);
                itemAreaLineBottom.setBackgroundColor(context.getResources().getColor(R.color.wom_blue_5DB7FF));
                dealTime.setText(DateUtil.dateFormat(data.getCompleteTime(), "MM-dd HH:mm"));
                statusTv.setText(R.string.batch_end);
                statusTv.setTextColor(context.getResources().getColor(R.color.dark_green));
                intoIv.setVisibility(View.GONE);
            } else {
                // 执行中、未开始
                if (data.getPlOrder() == 1 || getItem(getAdapterPosition()-1).getCompleteTime() != null) {
                    material.setTextColor(context.getResources().getColor(R.color.wom_blue_5DB7FF));
                    itemAreaDot.setImageResource(R.drawable.wom_ic_node_blue);
                    itemAreaLineBottom.setBackgroundColor(context.getResources().getColor(R.color.wom_blue_5DB7FF));
                    dealTime.setText("--");
                    statusTv.setText(R.string.wom_executing);
                    statusTv.setTextColor(context.getResources().getColor(R.color.wom_blue_5DB7FF));
                    if (data.getAreaMange().isAutoBurden()){
                        intoIv.setVisibility(View.GONE);
                    }else {
                        intoIv.setVisibility(View.VISIBLE);
                    }

                } else {
                    material.setTextColor(context.getResources().getColor(R.color.gray_text));
                    itemAreaDot.setImageResource(R.drawable.wom_ic_node_gery);
                    itemAreaLineBottom.setBackgroundColor(context.getResources().getColor(R.color.wom_grey_DDDDDD));
                    dealTime.setText("--");
                    statusTv.setText(R.string.wom_wait_executed);
                    statusTv.setTextColor(context.getResources().getColor(R.color.gray_text));
                    intoIv.setVisibility(View.GONE);

//                    if (getItem(getAdapterPosition() - 1) != null && getItem(getAdapterPosition()-1).getCompleteTime() != null) {
//                        itemAreaLineTop.setBackgroundColor(context.getResources().getColor(R.color.wom_blue_5DB7FF));
//                    } else {
//                        if (getAdapterPosition() != 0) {
                            itemAreaLineTop.setBackgroundColor(context.getResources().getColor(R.color.wom_grey_DDDDDD));
//                        }
//                    }
                }
            }

            if (getAdapterPosition() == getItemCount() - 1) {
                itemAreaLineBottom.setVisibility(View.GONE);
            } else {
                itemAreaLineBottom.setVisibility(View.VISIBLE);
            }

            if (data.getMaterial() != null && !TextUtils.isEmpty(data.getMaterial().getName())) {
                material.setText(/*String.format("%s(%s)", */data.getMaterial().getName()/*, data.getMaterial().getCode())*/);
            } else {
                material.setText("--");
            }
        }
    }
}
