package vero.com.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv;
    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        tv= (TextView) findViewById(R.id.tv);
        start= (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                start();
                break;
            default:
                break;
        }
    }

    public void start(){
        //@1:对应doInBackground的参数，传入execute()的参数
        //@3:对应doInBackground的返回值，传入onPostExecute的参数
        //@3:进度，
        new AsyncTask<Integer,Integer,Integer>(){

            @Override
            protected void onPreExecute() {
                //主线程执行，准备执行之前的回到
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Integer... params) {
                //params------>execute的参数
                //子线程执行，操作
                int start = params[0];
                int end = params[1];
                int result=0;
                for (int i=start;i<end;i++){
                    try {
                        Thread.sleep(20);
                        result=i;
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                //result：doInBackground的返回值
                //主线程执行，执行完成的回调
                tv.setText(result+"");
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                //values:publishProgress的参数
                //主线程执行，进度改变的回调
                tv.setText(values[0].toString());
            }

        }.execute(0,100);
    }
}
