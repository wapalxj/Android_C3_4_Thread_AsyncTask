package com.c3.vero.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String uri="http://192.168.56.1:8080/vero/Servlet_register";
                        URL url= null;
                        try {
                            url = new URL(uri);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        HttpURLConnection connection= null;
                        try {
                            connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Accept-Encoding", "identity");
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setChunkedStreamingMode(0);
                            connection.setUseCaches(false);
                            connection.connect();

                            OutputStream os= connection.getOutputStream();
//                            1.--字符流OK
//                            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
//                            Map<String,String> para=new HashMap<>();
//                            para.put("data", "qwwewqe");
//                            para.put("name", "vvvvverooo");
//                            bw.write(change(para).toString());
//                            bw.flush();
//                            bw.close();
//                            os.close();

//                            2.--字符流OK
                            PrintWriter pw=new PrintWriter(os);
                            pw.print("data=vero&data2=vnix");
                            pw.flush();
                            pw.close();
                            os.close();
//                            3.字节流--ok
//                            BufferedOutputStream bs=new BufferedOutputStream(os);
//                            Map<String,String> para=new HashMap<>();
//                            para.put("data", "qwwewqe");
//                            byte [] s;
//                            s=change(para).toString().getBytes();
//                            bs.write(s);
//                            bs.flush();
//                            bs.close();
//                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            InputStream is=connection.getInputStream();
                            if (is!=null){
                                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                                char[] data=new char[1024];
                                int len=0;
                                StringBuilder result=new StringBuilder();
                                while (-1!=(len=br.read(data))){
                                    result.append(new StringBuilder(new String(data,0,len)));
                                }
                                Log.i("iiiiiiiiiiiiiiiiiiii", "" + result);
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                is.close();
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                        connection.disconnect();
                    }
                }).start();
            }
        });
    }

    public StringBuffer change(Map<String,String> parameterMap){
        StringBuffer parameterBuffer = new StringBuffer();
       for (Map.Entry<String,String>entry:parameterMap.entrySet()){
           parameterBuffer.append(entry.getKey())
                   .append("=")
                   .append(entry.getValue())
                   .append("&");
       }
        parameterBuffer.deleteCharAt(parameterBuffer.length()-1);
        return parameterBuffer;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
