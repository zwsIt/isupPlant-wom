package com.supcon.mes.module_wom_replenishmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ReplenishMaterialTableEditAdapter extends BaseListDataRecyclerViewAdapter<ReplenishMaterialTableEntity> {
    public ReplenishMaterialTableEditAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<ReplenishMaterialTableEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<ReplenishMaterialTableEntity> {

        @BindByTag("produceBatchNumTv")
        TextView produceBatchNumTv;
        @BindByTag("statusTv")
        TextView statusTv;
        @BindByTag("productNameTv")
        TextView productNameTv;
        @BindByTag("materialCode")
        CustomTextView materialCode;
        @BindByTag("numCustomTv")
        CustomTextView numCustomTv;
        @BindByTag("eamPoint")
        CustomTextView eamPoint;
        @BindByTag("time")
        CustomTextView time;
        @BindByTag("startTv")
        TextView startTv;

        public ViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_replenish_table;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            productNameTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CustomContentTextDialog.showContent(context,productNameTv.getText().toString());
                    return false;
                }
            });
            RxView.clicks(startTv).throttleFirst(300, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            onItemChildViewClick(startTv,0,getItem(getAdapterPosition()));
                        }
                    });
        }

        @Override
        protected void update(ReplenishMaterialTableEntity data) {
            produceBatchNumTv.setText(data.getCode());
            statusTv.setText("状态"/*data.*/);
            productNameTv.setText(data.getMaterial().name);
            materialCode.setContent(data.getMaterial().code);
            numCustomTv.setContent(data.getActualNumber() == null ? "0" : data.getActualNumber().toString() + "/" + data.getPlanNumber().toString());
            eamPoint.setContent(data.getEquipment().getName() + "("+data.getEquipment().getCode()+")");
            time.setContent(data.getCreateTime() == null ? "--" : DateUtil.dateTimeFormat(data.getCreateTime()));
        }
    }


}
