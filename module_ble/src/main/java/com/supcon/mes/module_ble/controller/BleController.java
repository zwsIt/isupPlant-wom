package com.supcon.mes.module_ble.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;


import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.model.bean.PopupWindowEntity;

import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.module_ble.R;
import com.supcon.mes.module_ble.model.listener.OnSuccessListener;
import com.supcon.mes.module_ble.ui.BleDialog;
import com.supcon.mes.module_wom_ble.ui.WomPopupWindow;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static android.content.Context.BLUETOOTH_SERVICE;

public class BleController extends BaseViewController {

    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String SERVICE_NAME = "BluetoothCom";
    private BluetoothServerSocket mServerSocket;
    private BluetoothSocket mSocket;
    private String TAG = "BleListener";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String deviceMac;
    private BaseActivity context;
    private BleDialog bleDialog;
    private int mState;
    public static final int STATE_NONE = 0;       //未做任何事情
    public static final int STATE_LISTEN = 1;     // 连接监听
    public static final int STATE_CONNECTING = 2; // 正在连接
    public static final int STATE_CONNECTED = 3;  // 连接成功
    private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    private Map<String, BluetoothDevice> mBluetoothDeviceMap = new HashMap<>();
    private List<PopupWindowEntity> popList = new ArrayList<>();
    private WomPopupWindow mCustomPopupWindow;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;


    @BindByTag("rightBtn")
    CustomImageButton rightBtn;

    public BleController(View rootView) {
        super(rootView);

    }

    @Override
    public void initView() {
        super.initView();
        rightBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        super.initListener();

    }

    public void showPop() {
        mCustomPopupWindow.setOnItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (!bluePermission) {
                        return;
                    }
                    if (isConnecting()) {
                        secondConnect();
                    } else
                        scanDevice();
                } else if (position == 1) {
                    try {
                        if (mConnectThread!=null){
                            mConnectThread.cancel();
                        }
                        if (mConnectedThread!=null){
                            mConnectedThread.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mCustomPopupWindow.dismiss();
            }
        });
        mCustomPopupWindow.showPopupWindow(rightBtn);
    }

    public void initBle(BaseActivity context) {
        this.context = context;
        PopupWindowEntity connectEntity = new PopupWindowEntity();
        connectEntity.setText(context.getString(R.string.ble_connect));
        PopupWindowEntity disConnectEntity = new PopupWindowEntity();
        disConnectEntity.setText(context.getString(R.string.ble_disconnect));
        popList.add(connectEntity);
        popList.add(disConnectEntity);
        mCustomPopupWindow = new WomPopupWindow(context, popList);

        rightBtn.setVisibility(View.VISIBLE);

        mBluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        registerBluetoothReceiver();
        if (!isBlueAdapterEnable())
            openBleAdapter();
        bleDialog = new BleDialog(context);
    }

    private void registerBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        //发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备绑定状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //蓝牙设备状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //搜素完成
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mBluetoothReceiver, filter);
    }

    //开始搜索
    private void startDiscoveryBluetooth() {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.startDiscovery();
    }

    private void cancelDiscoveryBluetooth() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }


    BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: " + action);
            if (action == null) return;
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && !TextUtils.isEmpty(device.getName()) && "langxun".equals(device.getName())) {
                String deviceMac = device.getAddress();
                if (!mBluetoothDeviceMap.containsKey(deviceMac)) {
                    mBluetoothDeviceMap.put(deviceMac, device);
                    if (bleDialog != null && bleDialog.isShowing()) {
                        bleDialog.addBluetoothDevices(device);
                    }
                }
            }

//            switch (action) {
//                case BluetoothDevice.ACTION_FOUND:
//                    //获取搜索到设备的信息
//                    Log.i(TAG, "device name: " + device.getName() + " address: " + device.getAddress());
//                    //获取绑定状态
//                    Log.i(TAG, "device bond state : " + device.getBondState());
//                    break;
//                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
//                    Log.i(TAG, "BOND_STATE_CHANGED device name: " + device.getName() + " address: " + device.getAddress());
//                    Log.i(TAG, "BOND_STATE_CHANGED device bond state : " + device.getBondState());
//                    break;
//                case BluetoothAdapter.ACTION_STATE_CHANGED:
//                    Log.i(TAG, "BOND_STATE_CHANGED device name: " + device.getName() + " address: " + device.getAddress());
//                    Log.i(TAG, "BOND_STATE_CHANGED device bond state : " + device.getBondState());
//                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
//                    Log.i(TAG, "bluetooth discovery finished");
//                    break;
//            }
        }
    };

    public void buildServerSocket() {
        try {
            mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, SERVICE_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        public AcceptThread() {
            if (mServerSocket == null) {
                buildServerSocket();
            }
        }

        public void run() {

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    mSocket = mServerSocket.accept();
                } catch (IOException e) {
                    // Log.e(TAG, "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (mSocket != null) {
                    synchronized (BleController.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:

                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    mSocket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                if (mSocket!=null)
                    mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    public synchronized void connected(BluetoothSocket socket) {

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();


    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(SERVICE_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering())
                mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                BleController.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BleController.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket);
        }

        public void cancel() {
            try {
                if (mmSocket!=null)
                    mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    long lastTime;

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            Log.i(TAG, "BEGIN mConnectedThread");

            byte[] buffer = new byte[1024];
            byte[] buffer2 = new byte[1024];
            int bytes;
            int i;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    if (mState == STATE_CONNECTING) {
                        mHandler.sendEmptyMessage(200);
                        EventInfo.postEvent(2000, context.getString(R.string.ble_tip4));
                        mState = STATE_CONNECTED;
                    }
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastTime >= 2000) {
                        lastTime = currentTime;
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);
                        if (bytes > 0) {
                            // Send the obtained bytes to the UI Activity
                            String data = new String(buffer, "UTF-8");
                            Log.i("BleConnected", "传输数据---" + data);
                            if (data.contains("ST,GS,")) {
                                String[] dataArr = data.split("\r\n");
                                if (dataArr.length > 1 && dataArr[1].startsWith("ST,GS,")) {
                                    String weightData = dataArr[1];
                                    weightData = weightData.replace("ST,GS,", "");
                                    weightData = weightData.replace("+", "");
                                    weightData = weightData.replace("kg", "");
                                    weightData = weightData.replace("   ", "");
                                    weightData = weightData.replace("  ", "");
                                    EventInfo.postEvent(200, weightData);
                                    Log.i(TAG, "接收的数据===" + weightData);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    //Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        public void cancel() {
            try {
                if (mmSocket!=null)
                    mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        Log.i(TAG,"======"+mState);
        if (mState == STATE_CONNECTING) {
            mHandler.sendEmptyMessage(-200);
            mState = STATE_NONE;
        }

        Log.i(TAG, "连接失败");
        mState = STATE_NONE;
        EventInfo.postEvent(-200, context.getString(R.string.ble_disconnect));
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        Log.i(TAG, "断开蓝牙连接");
        mState = STATE_NONE;
        EventInfo.postEvent(-200, context.getString(R.string.ble_disconnect));
    }

    public boolean isConnected() {
        return mState == STATE_CONNECTED;
    }

    /**
     * 正在连接设备
     *
     * @return
     */
    public boolean isConnecting() {
        return mState == STATE_CONNECTED;
    }

    /**
     * 当前在操作的蓝牙设备
     *
     * @return
     */
    private BluetoothDevice getBluetoothDevice() {
        return !TextUtils.isEmpty(deviceMac) && mBluetoothDeviceMap.containsKey(deviceMac) ? mBluetoothDeviceMap.get(deviceMac) : null;
    }


    /**
     * 打开蓝牙适配器
     */
    private void openBleAdapter() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        context.startActivityForResult(intent, 0);
        mBluetoothDevices.clear();
        mState = STATE_NONE;
    }

    /**
     * 判断蓝牙是否可用
     *
     * @return
     */
    public boolean isBlueAdapterEnable() {
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
            bleDialog.setBlueToothDevice(getBluetoothDevice());
        } else {
            bleDialog.startScanDevice();
            scanBle();
        }
    }


    /**
     * 开始扫描 10秒后自动停止
     */
    public void scanDevice() {

        mState = STATE_NONE;
        if (!isBlueAdapterEnable()) {
            openBleAdapter();
            return;
        }
        startDiscoveryBluetooth();
        if (mState != STATE_LISTEN)
            showBleDialog();
        scanBle();
    }

    Handler mHandler;

    private void scanBle() {
        if (mHandler == null)
            mHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 200) {
                        context.onLoadSuccess(context.getString(R.string.ble_tip4));
                    } else if (msg.what == -200) {
                        context.onLoadFailed(context.getString(R.string.ble_tip5));
                    }
                }
            };
        if (bleDialog != null && bleDialog.isShowing() && !bleDialog.isScanning()) {
            bleDialog.startScanDevice();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //结束扫描
                cancelDiscoveryBluetooth();
                if (bleDialog != null && bleDialog.isShowing()) {
                    bleDialog.stopScanDevice();
                }
                if (mState == STATE_NONE) {

                }
            }
        }, 15000);
    }


    private void showBleDialog() {
        if (bleDialog != null && !bleDialog.isShowing()) {
            bleDialog.show();
            bleDialog.setBluetoothDevices(mBluetoothDevices);
            bleDialog.setBlueToothDevice(getBluetoothDevice());
            RxView.clicks(bleDialog.findViewById(R.id.btnEnsure))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        int selectPosition = bleDialog.getAdapter().selectPostion;
                        if (selectPosition != -1) {
                            BluetoothDevice bluetoothDevice = bleDialog.getAdapter().getItem(selectPosition);
                            bleDialog.dismiss();
                            cancelDiscoveryBluetooth();
                            if (isConnected() && !TextUtils.isEmpty(deviceMac) && deviceMac.equals(bluetoothDevice.getAddress()))
                                return;
                            mState = STATE_CONNECTING;
                            context.onLoading(context.getString(R.string.ble_connecting));
                            connect(bluetoothDevice);
                        } else {
                            ToastUtils.show(context, context.getString(R.string.ble_tip3));
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        disConnectDevice();
    }

    /**
     * 停止扫描
     */
    private void stopScanDevice() {
        cancelDiscoveryBluetooth();
        if (bleDialog != null && bleDialog.isShowing()) {
            bleDialog.stopScanDevice();
        }
    }

    /**
     * 断开蓝牙设备连接
     */
    private void disConnectDevice() {
        try {
            mState = STATE_NONE;
            if (mBluetoothReceiver != null)
                context.unregisterReceiver(mBluetoothReceiver);

            if (mConnectThread!=null)
                mConnectThread.cancel();
            if (mConnectedThread!=null)
                mConnectedThread.cancel();
           if (mAcceptThread!=null)
               mAcceptThread.cancel();
            mBluetoothDeviceMap.clear();
            mBluetoothDeviceMap = null;
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
            mBluetoothDevices.clear();
            mBluetoothAdapter = null;
            mBluetoothManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean bluePermission = false;

}
