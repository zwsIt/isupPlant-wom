package com.supcon.mes.module_wom_replenishmaterial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.view.CustomContentTextDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.UrlUtil;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ReplenishMaterialTableEditListAdapter extends BaseListDataRecyclerViewAdapter<ReplenishMaterialTableEntity> {
    public ReplenishMaterialTableEditListAdapter(Context context) {
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
        @BindByTag("vessel")
        CustomTextView vessel;
        @BindByTag("replenishModelIv")
        ImageView replenishModelIv;

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
            productNameTv.setOnLongClickListener(v -> {
                CustomContentTextDialog.showContent(context, productNameTv.getText().toString());
                return true;
            });
            RxView.clicks(itemView)
                    .throttleFirst(300,TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Bundle bundle = new Bundle();
                            ReplenishMaterialTableEntity replenishMaterialTableEntity = getItem(getAdapterPosition());
                            bundle.putSerializable(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE,replenishMaterialTableEntity);
                            if (replenishMaterialTableEntity.getPending() != null){
                                IntentRouter.go(context, UrlUtil.getPendingViewCode(replenishMaterialTableEntity.getPending().openUrl),bundle);
                            }else {
                                IntentRouter.go(context, ReplenishConstant.Router.REPLENISH_MATERIAL_SCAN,bundle);
                            }

                        }
                    });
        }

        @Override
        protected void update(ReplenishMaterialTableEntity data) {
            produceBatchNumTv.setText(data.getTableNo());
            statusTv.setText(data.getFmState() == null ? "--" : data.getFmState().getValue());
            productNameTv.setText(data.getMaterial().name);
            materialCode.setContent(data.getMaterial().code);
            numCustomTv.setContent((data.getActualNumber() == null ? 0 : data.getActualNumber().toString()) + "/" + (data.getPlanNumber() == null ? 0 : data.getPlanNumber().toString()));
            eamPoint.setContent(data.getEquipment().getName()/* + "("+data.getEquipment().getCode()+")"*/);
            vessel.setContent(data.getVessel() == null ? "--" : data.getVessel().getCode());

            if (data.getEquipment() != null && data.getEquipment().getRunModel() != null
                    && ReplenishConstant.SystemCode.MODEL_NOTIFY.equals(data.getEquipment().getRunModel().id)){
                replenishModelIv.setImageResource(R.drawable.ic_notify);
            }else if (data.getEquipment() != null && data.getEquipment().getRunModel() != null
                    && ReplenishConstant.SystemCode.MODEL_MANUAL.equals(data.getEquipment().getRunModel().id)){
                replenishModelIv.setImageResource(R.drawable.ic_active);
            }else {
                replenishModelIv.setImageResource(R.drawable.ic_active);
            }
        }
    }


}
