package com.supcon.mes.module_wom_replenishmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.CommonBaseEntity;
import com.supcon.mes.middleware.model.bean.SelectEntity;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.AssociatedEquipmentEntity;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class CommonListAdapter extends BaseListDataRecyclerViewAdapter<CommonBaseEntity> {
    public CommonListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<CommonBaseEntity> getViewHolder(int viewType) {
        return new ViewHolder(context);
    }

    /**
     * ViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc Item
     */
    class ViewHolder extends BaseRecyclerViewHolder<CommonBaseEntity> {

        @BindByTag("nameTv")
        TextView nameTv;

        public ViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_common;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(itemView,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(CommonBaseEntity data) {
            nameTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_factory_model_line,0,0,0);
            nameTv.setText(String.format("%s(%s)", data.getName(), data.getCode()));
        }
    }

}
