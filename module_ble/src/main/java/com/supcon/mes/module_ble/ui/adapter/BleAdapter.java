package com.supcon.mes.module_ble.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_ble.R;


import java.util.concurrent.TimeUnit;


public class BleAdapter extends BaseListDataRecyclerViewAdapter<BluetoothDevice> {


    public BleAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<BluetoothDevice> getViewHolder(int viewType) {
        return new BleViewHolder(context);
    }

    public int selectPostion=-1;
    class BleViewHolder extends BaseRecyclerViewHolder<BluetoothDevice> {

        @BindByTag("customBleTv")
        CustomTextView customBleTv;
        @BindByTag("customBleMacTv")
        CustomTextView customBleMacTv;
        @BindByTag("imgChecked")
        ImageView imgChecked;
        public BleViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o->{
                        onItemChildViewClick(itemView,0);
                    });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_ble;
        }

        @Override
        protected void update(BluetoothDevice data) {
            customBleTv.setContent(data.getName());
            customBleMacTv.setContent(data.getAddress());
            if (selectPostion==getAdapterPosition()){
                imgChecked.setVisibility(View.VISIBLE);
            }else {
                imgChecked.setVisibility(View.GONE);
            }
        }
    }
}
