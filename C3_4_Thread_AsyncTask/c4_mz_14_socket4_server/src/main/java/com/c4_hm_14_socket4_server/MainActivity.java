package com.c4_hm_14_socket4_server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button start;
    private ConnectedThread connectedThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start= (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
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
                ss.bind(new InetSocketAddress("127.0.0.1",1234));
                while (true){
                    Log.i("ServerSocketThread","Server--wait");
                    Socket socket=ss.accept();
                    connect(socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 以下代码和client一样
     */

    private void connect(Socket socket) {
        if (connectedThread!=null){
            connectedThread.cancel();
            connectedThread=null;
        }
        connectedThread=new ConnectedThread(socket);
        connectedThread.start();
        Log.i("ConnectedThread","Server开启成功");
    }


    class ConnectedThread extends Thread{
        private Socket socket;
        private InputStream is;
        private OutputStream os;

        public ConnectedThread(Socket socket) {
            try {
                this.socket = socket;
                this.is = socket.getInputStream();
                this.os = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte [] buff=new byte[1024];
            int length=0;

            while (true){
                try {
                    length=is.read(buff);
                    Log.i("ConnectedThread","server read"+new String(buff,0,length));
                    String data="hello client"+new Random().nextInt();
                    write(data.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] buff){
            try {
                os.write(buff);
                Log.i("ConnectedThread","server write"+new String(buff));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel(){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
