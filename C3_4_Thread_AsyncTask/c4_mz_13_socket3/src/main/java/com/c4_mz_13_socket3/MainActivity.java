package com.c4_mz_13_socket3;
/**
 * 服务端的Socket
 * PC浏览器输入IP:port即可访问
 *IP地址问题，测试失败
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button btn_socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        btn_socket= (Button) findViewById(R.id.btn_socket);
        btn_socket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerSocketThread().start();
            }
        });
    }

    class ServerSocketThread extends Thread{
        @Override
        public void run() {
            try {
                ServerSocket ss=new ServerSocket();
                ss.bind(new InetSocketAddress("10.0.3.15",1235));
                while (true){
                    Socket socket=ss.accept();
                    String rsp="success";
                    socket.getOutputStream().write(rsp.getBytes());
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
