package com.supcon.mes.module_wom_ble.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.module_wom_ble.R;
import com.supcon.mes.module_wom_ble.model.event.EventInfo;
import com.supcon.mes.module_wom_ble.model.listener.OnSuccessListener;
import com.supcon.mes.module_wom_ble.ui.BleDialog;
import com.supcon.mes.module_wom_ble.util.HexUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;
import static android.content.Context.BLUETOOTH_SERVICE;

public class BleController extends BasePresenterController {

    private boolean isScaning = false;
    private boolean isConnecting = false;
    private BluetoothGatt mBluetoothGatt;
    private boolean connected;


    //服务和特征值
    private UUID write_UUID_service;
    private UUID write_UUID_chara;
    private UUID read_UUID_service;
    private UUID read_UUID_chara;
    private UUID notify_UUID_service;
    private UUID notify_UUID_chara;
    private UUID indicate_UUID_service;
    private UUID indicate_UUID_chara;
    private String hex = "7B46363941373237323532443741397D";
    private String TAG = "BleListener";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String deviceMac;
    private Activity context;
    private BleDialog bleDialog;
    private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    private Map<String, BluetoothGatt> bluetoothGattMap = new HashMap<>();
    private List<BluetoothGatt> mBluetoothGatts=new ArrayList<>();

    public void initBle(Activity context) {
        this.context = context;
        mBluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (!isBlueAdapterEnable())
            openBleAdapter();
        bleDialog = new BleDialog(context);
    }


    /**
     * 是否有连接的蓝牙设备
     *
     * @return
     */
    public boolean isConnecting() {
        if (!isBlueAdapterEnable()) {
            connected = false;
            isConnecting = false;
            return false;
        }
        return connected;
    }

    /**
     * 打开蓝牙适配器
     */
    private void openBleAdapter() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        context.startActivityForResult(intent, 0);
    }

    /**
     * 判断蓝牙是否可用
     *
     * @return
     */
    private boolean isBlueAdapterEnable() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    /**
     * 第二次连接蓝牙设备
     */
    public void secondConnect() {
        if (!isBlueAdapterEnable()) {
            openBleAdapter();
            return;
        }
        showBleDialog();
        if (!mBluetoothDevices.isEmpty()) {
            stopScanDevice();
            bleDialog.setBluetoothDevices(mBluetoothDevices);
            bleDialog.setBlueToothDevice(getConnectedBle());
        } else {
            mBluetoothAdapter.startLeScan(scanCallback);
            bleDialog.startScanDevice();
            scanBle();
        }
    }


    private BluetoothDevice getConnectedBle() {
        if (!TextUtils.isEmpty(deviceMac) && bluetoothGattMap.containsKey(deviceMac)) {
            return bluetoothGattMap.get(deviceMac) != null ? bluetoothGattMap.get(deviceMac).getDevice() : null;
        }
        return null;
    }


    /**
     * 开始扫描 10秒后自动停止
     */
    public void scanDevice() {
        isScaning = true;
        if (!isBlueAdapterEnable()) {
            openBleAdapter();
            return;
        }
        mBluetoothAdapter.startLeScan(scanCallback);
        showBleDialog();
        scanBle();
    }

    Handler mHandler;

    private void scanBle() {
        if (mHandler == null)
            mHandler = new Handler();
        if (bleDialog != null && bleDialog.isShowing() && !bleDialog.isScanning()) {
            bleDialog.startScanDevice();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //结束扫描
                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.stopLeScan(scanCallback);
                if (bleDialog != null && bleDialog.isShowing()) {
                    bleDialog.stopScanDevice();
//                    bleDialog.setBluetoothDevices(mBluetoothDevices);
                }
            }
        }, 10000);
    }

    private void showBleDialog() {
        if (bleDialog != null && !bleDialog.isShowing()) {
            bleDialog.show();
            RxView.clicks(bleDialog.findViewById(R.id.btn_ensure))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        int selectPosition = bleDialog.getAdapter().selectPostion;
                        if (selectPosition != -1) {
                            BluetoothDevice bluetoothDevice = bleDialog.getAdapter().getItem(selectPosition);
                            bleDialog.dismiss();
                            if (connected && !TextUtils.isEmpty(deviceMac) && deviceMac.equals(bluetoothDevice.getAddress()))
                                return;
                            isConnecting = false;
                            connectBleDevice(bluetoothDevice);
                        } else {
                            ToastUtils.show(context, "请选择一个蓝牙设备！");
                        }
                    });

            bleDialog.setOnSuccessListener(new OnSuccessListener<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    stopScanDevice();
                }
            });

        }
    }

    private void connectBleDevice(BluetoothDevice bluetoothDevice) {
        if (isScaning) {
            stopScanDevice();
        }
        if (!isConnecting) {
            if (mBluetoothGatts!=null){
                for(BluetoothGatt bluetoothGatt:mBluetoothGatts){
                    bluetoothGatt.close();
                }
                mBluetoothGatts.clear();
            }
            EventInfo.postEvent(-200, "连接失败");
            isConnecting = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBluetoothGatt = bluetoothDevice.connectGatt(context,
                        true, gattCallback, TRANSPORT_LE);
            } else {
                mBluetoothGatt = bluetoothDevice.connectGatt(context,
                        true, gattCallback);
            }
            deviceMac = bluetoothDevice.getAddress();
            bluetoothGattMap.put(deviceMac,mBluetoothGatt);
            mBluetoothGatts.add(mBluetoothGatt);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disConnectDevice();
    }

    /**
     * 停止扫描
     */
    private void stopScanDevice() {
        isScaning = false;
        mBluetoothAdapter.stopLeScan(scanCallback);
        if (bleDialog != null && bleDialog.isShowing()) {
            bleDialog.stopScanDevice();
        }
    }

    BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("scanCallback", "scanning..." + device.getName());
            isConnecting = false;
            if (!mBluetoothDevices.contains(device) &&  !TextUtils.isEmpty(device.getName()) && device.getName().startsWith("ID")) {
//                && !TextUtils.isEmpty(device.getName()) && device.getName().startsWith("ID")
                mBluetoothDevices.add(device);
                if (bleDialog != null && bleDialog.isShowing()) {
                    bleDialog.addBluetoothDevices(device);
                }
            }
        }
    };


    /**
     * 断开蓝牙设备连接
     */
    private void disConnectDevice() {
        connected = false;
        isScaning = false;
        try {
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
            if (mBluetoothGatts!=null){
                for(BluetoothGatt bluetoothGatt:mBluetoothGatts){
                    bluetoothGatt.disconnect();
                }
            }
            scanCallback = null;
            mBluetoothDevices.clear();
            bluetoothGattMap.clear();
            mBluetoothAdapter = null;
            mBluetoothManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        /**
         * 断开或连接 状态发生变化时调用
         * */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i("gattCallback", gatt.getDevice().getAddress());
            isConnecting = false;
            connected = false;

            //连接成功
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.i(TAG, "连接成功");
                gatt.discoverServices();
            } else {
                //连接失败
                Log.i(TAG, "失败==" + status);
                EventInfo.postEvent(-200, "连接失败");
                gatt.close();
            }

            Log.i("gattCallback", gatt.getDevice().getAddress());

        }

        /**
         * 发现设备（真正建立连接）
         * */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            //直到这里才是真正建立了可通信的连接
            isConnecting = false;
            connected = true;
            EventInfo.postEvent(200, "连接成功");
            Log.e(TAG, "onServicesDiscovered()---建立连接");
            //获取初始化服务和特征值
            initServiceAndChara();
            //订阅通知
            mBluetoothGatt.setCharacteristicNotification(mBluetoothGatt
                    .getService(notify_UUID_service).getCharacteristic(notify_UUID_chara), true);


        }

        /**
         * 读操作的回调
         * */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicRead()");

        }

        /**
         * 写操作的回调
         * */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            Log.e(TAG, "onCharacteristicWrite()  status=" + status + ",value=" + HexUtil.encodeHexStr(characteristic.getValue()));
        }

        /**
         * 接收到硬件返回的数据
         * */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.e(TAG, "onCharacteristicChanged()" + gatt.getDevice().getAddress());
            if (!TextUtils.isEmpty(deviceMac) && deviceMac.equals(gatt.getDevice().getAddress())) {
                byte[] data = characteristic.getValue();
                String s = new String(data);
                Log.i("CharacteristicChanged", s);
                String str = HexUtil.encode(data);
                str = str.replace("\"", "");
                str = str.trim();
                Log.i(TAG, str);
                EventInfo.postEvent(2000, str);
            }

        }
    };

    private void initServiceAndChara() {
        List<BluetoothGattService> bluetoothGattServices = mBluetoothGatt.getServices();
        for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

                    read_UUID_chara = characteristic.getUuid();
                    read_UUID_service = bluetoothGattService.getUuid();
                    Log.e(TAG, "read_chara=" + read_UUID_chara + "----read_service=" + read_UUID_service);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mBluetoothGatt != null)
//                                mBluetoothGatt.readCharacteristic(characteristic);
//                        }
//                    }, 5);

                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    write_UUID_chara = characteristic.getUuid();
                    write_UUID_service = bluetoothGattService.getUuid();
                    Log.e(TAG, "write_chara=" + write_UUID_chara + "----write_service=" + write_UUID_service);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    write_UUID_chara = characteristic.getUuid();
                    write_UUID_service = bluetoothGattService.getUuid();
                    Log.e(TAG, "write_chara=" + write_UUID_chara + "----write_service=" + write_UUID_service);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    notify_UUID_chara = characteristic.getUuid();
                    notify_UUID_service = bluetoothGattService.getUuid();
                    Log.e(TAG, "notify_chara=" + notify_UUID_chara + "----notify_service=" + notify_UUID_service);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    indicate_UUID_chara = characteristic.getUuid();
                    indicate_UUID_service = bluetoothGattService.getUuid();
                    Log.e(TAG, "indicate_chara=" + indicate_UUID_chara + "----indicate_service=" + indicate_UUID_service);

                }
            }
        }
    }

}
