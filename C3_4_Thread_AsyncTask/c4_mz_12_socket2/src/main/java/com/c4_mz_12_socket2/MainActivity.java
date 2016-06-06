package com.c4_mz_12_socket2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button btn_socket;
    private String host="www.baidu.com";
    private int port=80;
    private String masterheader="GET / HTTP/1.1\r\nHost: www.baidu.com\r\n\r\n";//构造必须的请求头
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
                socketTest();
            }
        });
    }

    private void socketTest() {
            new Thread(){
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(host,port);
                        OutputStream os=socket.getOutputStream();
                        OutputStreamWriter writer=new OutputStreamWriter(os);
                        BufferedWriter bufferedWriter=new BufferedWriter(writer);
                        bufferedWriter.write(masterheader);
                        bufferedWriter.flush();

                        InputStream is=socket.getInputStream();
                        InputStreamReader reader =new InputStreamReader(is);
                        BufferedReader br=new BufferedReader(reader);
                        StringBuffer sb=new StringBuffer();
                        String line="";
                        while ((line=br.readLine())!=null){
                            sb.append(line);
                            Log.i("socket---line",sb.toString());
                        }
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
    }

}
