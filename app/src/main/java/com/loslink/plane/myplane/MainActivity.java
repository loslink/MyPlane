package com.loslink.plane.myplane;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private UdpMessageTool mUdpMessageTool;
    // 服务器主机ip
    private static final String HOST = "192.168.0.0";
    // 服务器请求端口号
    private static final int PORT = 6000;
    // 随便定义的发送内容，发送格式是与服务器端协议
    private static final String CONTENT = "SEND MESSAGE?key1=abc&key2=cba";
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
//        findViewById(R.id.udp).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        //Looper.prepare();
//                        sendDataByUDP();
//                    }
//                }).start();
//            }
//        });
    }

    private void sendDataByUDP() {
        try {
            mUdpMessageTool = UdpMessageTool.getInstance();
            mUdpMessageTool.setTimeOut(5000);// 设置超时为5s
            // 向服务器发数据
            mUdpMessageTool.send(HOST, PORT, CONTENT.getBytes());
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
}
