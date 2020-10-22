package com.supcon.mes.module_ble.ui;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.listener.OnItemChildViewClickListener;

import com.supcon.mes.module_ble.R;
import com.supcon.mes.module_ble.model.listener.OnSuccessListener;
import com.supcon.mes.module_ble.ui.adapter.BleAdapter;
import com.supcon.mes.module_ble.util.VerticalSpaceDecoration;
import com.supcon.mes.module_ble.model.listener.OnSuccessListener;
import com.supcon.mes.module_ble.ui.adapter.BleAdapter;



import java.util.List;
import java.util.concurrent.TimeUnit;

public class BleDialog extends Dialog {
    private Context context;
    TextView btnCancel;
    TextView btnEnsure;
    RecyclerView contentView;
    ProgressBar progressBar;
    BleAdapter adapter;


    public void setBlueToothDevice(BluetoothDevice blueToothDevice) {
        if (this.isShowing() && adapter != null) {
            List<BluetoothDevice> bluetoothDevices = adapter.getList();
            if (blueToothDevice != null && bluetoothDevices != null && !bluetoothDevices.isEmpty()) {
                int size = bluetoothDevices.size();
                for (int i = 0; i < size; i++) {
                    BluetoothDevice device = bluetoothDevices.get(i);
                    if (blueToothDevice.getAddress().equals(device.getAddress())) {
                        adapter.selectPostion = i;
                        contentView.scrollToPosition(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    public void setBluetoothDevices(List<BluetoothDevice> bluetoothDevices) {
        if (this.isShowing() && adapter != null) {
            adapter.setList(bluetoothDevices);
        }
    }
    public void addBluetoothDevices(BluetoothDevice bluetoothDevice){
        if (this.isShowing() && adapter != null) {
            adapter.addData(bluetoothDevice);
            adapter.notifyDataSetChanged();
        }
    }


    public BleDialog(Context context) {
        super(context, R.style.DialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ble);
        btnCancel = findViewById(R.id.btnCancel);
        btnEnsure = findViewById(R.id.btnEnsure);
        contentView = findViewById(R.id.contentView);
        progressBar = findViewById(R.id.progressBar);
        adapter = new BleAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        VerticalSpaceDecoration decoration = new VerticalSpaceDecoration();
        decoration.setDeiverHeight(getDimen(R.dimen.dp_1))
                    .setColor(Color.parseColor("#eaeaea"))
                    .setMargin(0);
        contentView.addItemDecoration(decoration);
        contentView.setLayoutManager(layoutManager);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        //获得window窗口的属性
        WindowManager.LayoutParams params = window.getAttributes();
        //设置窗口宽度为充满全屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        //设置窗口高度为包裹内容
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(params);
        this.setCanceledOnTouchOutside(false);
    }


    private float getDimen(int dimen){
        return context.getResources().getDimension(dimen);
    }
    @Override
    protected void onStart() {
        super.onStart();
        contentView.setAdapter(adapter);
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (onSuccessListener!=null){
                    onSuccessListener.onSuccess(position);
                }
                adapter.selectPostion = position;
                adapter.notifyDataSetChanged();
            }
        });
        RxView.clicks(btnCancel)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    dismiss();
                });

    }

    OnSuccessListener<Integer> onSuccessListener;

    public void setOnSuccessListener(OnSuccessListener<Integer> onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public BleAdapter getAdapter() {
        return adapter;
    }


    private boolean scanning=true;
    public void stopScanDevice() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            scanning=false;
        }
    }

    public boolean isScanning() {
        return scanning;
    }

    public void startScanDevice(){
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            scanning=true;
        }
    }
}
