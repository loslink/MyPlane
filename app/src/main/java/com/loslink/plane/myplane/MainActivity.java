package com.loslink.plane.myplane;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private UdpMessageTool mUdpMessageTool;
    // 服务器主机ip
    private static final String HOST = "192.168.0.0";
    // 服务器请求端口号
    private static final int PORT = 6000;
    // 随便定义的发送内容，发送格式是与服务器端协议
    private static final String CONTENT = "SEND MESSAGE?key1=abc&key2=cba";
    private MControllerView controllerViewLeft, controllerViewRight;
    private int MAX_DATA = 256;
    private Integer data;
    byte P_ID = 1;

    private String mDeviceName = "BT05";
    private String mDeviceAddress = "00:15:87:00:B0:70";
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String result = (String) msg.obj;
            if (!TextUtils.isEmpty(result)) {
                Toast.makeText(MainActivity.this, "收到的数据是：" + result,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controllerViewLeft = findViewById(R.id.cv_left);
        controllerViewRight = findViewById(R.id.cv_right);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        controllerViewRight.setControllerListenr(new MControllerView.ControllerListenr() {
            @Override
            public void updateListener(float progress) {
                final int value = (int) (MAX_DATA * progress);
                data = value;

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (data != null) {
                        sendDataByBT(data);
                        data = null;
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
        startTimer();
    }

    private void sendDataByBT(int data) {
        mBluetoothLeService.WriteInt(data);
    }

    private void sendDataByUDP(int data) {
        try {
            Log.v("controllerViewRight", "value: " + data);
            byte[] byteData = new byte[2];
            byteData[0] = P_ID;
            byteData[1] = (byte) data;
            mUdpMessageTool = UdpMessageTool.getInstance();
            mUdpMessageTool.setTimeOut(5000);// 设置超时为5s
            // 向服务器发数据
            mUdpMessageTool.send(HOST, PORT, byteData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String result = mUdpMessageTool.receive(HOST, PORT);
            Thread.sleep(2000);
            if (result == null) {
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUdpMessageTool.close();
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
            Log.e(TAG, "mBluetoothLeService is okay");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                mConnected = true;
                ShowDialog();
                Log.e(TAG, "In what we need");
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.e(TAG, "RECV DATA");
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if (data != null) {

                }
            }
        }
    };

    private void ShowDialog() {
        Toast.makeText(this, "已连接", Toast.LENGTH_SHORT).show();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.close();
            mBluetoothLeService = null;
        }
        if (mConnected) {
            mBluetoothLeService.disconnect();
            mConnected = false;
        }
        Log.d(TAG, "We are in destroy");
    }

    private void startTimer() {

        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(!mConnected){
                    mBluetoothLeService.connect(mDeviceAddress);
                    mConnected = true;
                }
            }
        };
        Timer mTimer = new Timer();
        mTimer.scheduleAtFixedRate(mTimerTask, 200L, 300L);//200毫秒执行一次轮询，是否有加锁应用
    }

}
