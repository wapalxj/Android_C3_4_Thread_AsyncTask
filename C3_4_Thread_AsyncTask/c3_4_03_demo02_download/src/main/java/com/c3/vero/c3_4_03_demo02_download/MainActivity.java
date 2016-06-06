package com.c3.vero.c3_4_03_demo02_download;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar=null;
    private Button button=null;
    private TextView textView=null;
    class Download extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url=new URL(params[0]);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept-Encoding", "identity");//这行必须有，不然进度条不会动
                long length=connection.getContentLength();
                InputStream is=connection.getInputStream();
                int perLength=0;
                String result=null;
                if (is!=null){
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    byte []buf=new byte[1024];
                    int ch=-1;
                    int count=0;
                    while ((ch=is.read(buf))!=-1){
                        bos.write(buf,0,ch);
                        count+=ch;
                        if (length>-1){
                            perLength=(int)((count/(float)length)*100);
                            publishProgress(perLength);
                        }
                        Thread.sleep(100);
                    }
                    result=new String(bos.toByteArray());
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String para) {
            progressBar.setVisibility(View.GONE);
            textView.setText(para);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.testview);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        button=(Button)findViewById(R.id.down);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Download().execute("http://www.baidu.com");
            }
        });
    }


}
