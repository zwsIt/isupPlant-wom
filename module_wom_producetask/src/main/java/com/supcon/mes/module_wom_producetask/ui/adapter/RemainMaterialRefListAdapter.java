package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.RemainMaterialEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/10/19
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class RemainMaterialRefListAdapter extends BaseListDataRecyclerViewAdapter<RemainMaterialEntity> {
    public RemainMaterialRefListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<RemainMaterialEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    /**
     * ViewHolder
     * created by zhangwenshuai1 2020/10/19
     * desc 尾料Item
     */
    class ViewHolder extends BaseRecyclerViewHolder<RemainMaterialEntity> {

        @BindByTag("nameTv")
        TextView nameTv;
        @BindByTag("materialBatchNumTv")
        TextView materialBatchNumTv;
        @BindByTag("remainderNumTv")
        TextView remainderNumTv;
        @BindByTag("warehouseTvTv")
        TextView warehouseTvTv;

        public ViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_remain_material;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(itemView,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(RemainMaterialEntity data) {
            if (data.getMaterial() == null || TextUtils.isEmpty(data.getMaterial().getCode())){
                nameTv.setText("物料：--");
            }else {
                nameTv.setText(String.format("物料：%s(%s)", data.getMaterial().getName(), data.getMaterial().getCode()));
            }

            materialBatchNumTv.setText(String.format("批号：%s", TextUtils.isEmpty(data.getBatchText()) ? "--" : data.getBatchText()));
            remainderNumTv.setText(String.format("可用量：%s", data.getRemainNum()));
            warehouseTvTv.setText(String.format("仓库：%s", (data.getWareId()) == null ? "--" : (TextUtils.isEmpty(data.getWareId().getName()) ? "--" : data.getWareId().getName())));
        }
    }

}
