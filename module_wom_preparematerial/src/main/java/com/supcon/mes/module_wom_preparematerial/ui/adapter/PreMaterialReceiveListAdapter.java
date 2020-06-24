package com.supcon.mes.module_wom_preparematerial.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.base.adapter.viewholder.BaseViewHolder;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialReceiveListAdapter extends BaseListDataRecyclerViewAdapter<PreMaterialEntity> {
    public PreMaterialReceiveListAdapter(Context context) {
        super(context);
    }

    public PreMaterialReceiveListAdapter(Context context, List<PreMaterialEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<PreMaterialEntity> getViewHolder(int viewType) {
        return new PreMaterialReceiveViewHolder(context);
    }

    class PreMaterialReceiveViewHolder extends BaseRecyclerViewHolder<PreMaterialEntity>{


        public PreMaterialReceiveViewHolder(Context context) {
            super(context);
        }

        public PreMaterialReceiveViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        public PreMaterialReceiveViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_pre_material_receive;
        }

        @Override
        protected void update(PreMaterialEntity data) {

        }
    }
}
