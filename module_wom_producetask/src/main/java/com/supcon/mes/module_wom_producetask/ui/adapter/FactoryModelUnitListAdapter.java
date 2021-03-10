package com.supcon.mes.module_wom_producetask.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.module_wom_producetask.R;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class FactoryModelUnitListAdapter extends BaseListDataRecyclerViewAdapter<FactoryModelEntity> {
    public FactoryModelUnitListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<FactoryModelEntity> getViewHolder(int viewType) {
        return new UnitViewHolder(context);
    }

    /**
     * UnitViewHolder
     * created by zhangwenshuai1 2020/3/26
     * desc 工作单元Item
     */
    class UnitViewHolder extends BaseRecyclerViewHolder<FactoryModelEntity> {

        @BindByTag("factoryModelUnitTv")
        TextView factoryModelUnitTv;

        public UnitViewHolder(Context context) {
            super(context,parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_factory_model_unit;
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView).throttleFirst(200,TimeUnit.MILLISECONDS)
                    .subscribe(o -> onItemChildViewClick(itemView,getAdapterPosition(),getItem(getAdapterPosition())));
        }

        @Override
        protected void update(FactoryModelEntity data) {
            factoryModelUnitTv.setText(String.format("%s(%s)", data.getName(), data.getCode()));
        }
    }

}
