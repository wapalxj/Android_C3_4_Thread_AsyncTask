package com.c4_mz_14_socket4_client;
/**
 * Socket在Android之间通信
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button start_client;
    private Button client_write;
    private ConnectedThread connectedThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        start_client= (Button) findViewById(R.id.start_client);
        client_write= (Button) findViewById(R.id.client_write);
        start_client.setOnClickListener(this);
        client_write.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_client:
                new ClientSocketThread().start();
                break;
            case R.id.client_write:
                if (connectedThread==null){
                    return;
                }
                String data="hello server"+new Random().nextInt();
                connectedThread.write(data.getBytes());
                break;
            default:
                break;
        }
    }

    class ClientSocketThread extends Thread{
        @Override
        public void run() {
            try {
                Socket socket=new Socket("127.0.0.1",1234);
                connect(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void connect(Socket socket) {
        if (connectedThread!=null){
            connectedThread.cancel();
            connectedThread=null;
        }
        connectedThread=new ConnectedThread(socket);
        connectedThread.start();
        Log.i("ConnectedThread","Client--链接成功");
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
                    Log.i("ConnectedThread","client read:"+new String(buff,0,length));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] buff){
            try {
                os.write(buff);
                Log.i("ConnectedThread","client write"+new String(buff));
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
