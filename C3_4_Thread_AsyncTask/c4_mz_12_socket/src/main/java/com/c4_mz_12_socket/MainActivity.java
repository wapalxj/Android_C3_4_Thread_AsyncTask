package com.c4_mz_12_socket;
/**
 * 扫描本机的可用端口号
 */

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity {
    private Button btn_scan;
    private TextView tv_sockets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        btn_scan= (Button) findViewById(R.id.btn_scan);
        tv_sockets= (TextView) findViewById(R.id.tv_sockets);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanPorts();
            }
        });
    }

    private void scanPorts() {
        new ScanPorts(1,1500).start();

    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv_sockets.append(msg.what+"OK\n");

        }
    };

    class ScanPorts extends Thread{
        private int minPort;
        private int maxPort;

        public ScanPorts(int minPort, int maxPort) {
            this.minPort = minPort;
            this.maxPort = maxPort;
        }

        @Override
        public void run() {
            for (int i=minPort;i<maxPort;i++){
                try {
                    Socket socket=new Socket();
                    SocketAddress address= new InetSocketAddress("113.55.78.141",i);
                    socket.connect(address,50);
                    //连接成功则发送该端口号，否则50MS后连接下一个端口号
                    mHandler.sendEmptyMessage(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
