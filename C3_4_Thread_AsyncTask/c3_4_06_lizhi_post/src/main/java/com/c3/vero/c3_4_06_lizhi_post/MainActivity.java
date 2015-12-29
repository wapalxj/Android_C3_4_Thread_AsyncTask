package com.c3.vero.c3_4_06_lizhi_post;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private List<Feed> datas;
    private FeedAdapter adapter;

    private Handler mhandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==Autils.TYPE_TXT){
                parseJson((String)msg.obj);
            }else {
                String url=msg.getData().getString("url");
                ImageView imageView =(ImageView)lv.findViewWithTag(url);
                if (imageView!=null){
                    imageView.setImageBitmap((Bitmap)msg.obj);
                }
            }
        }
    };

    Autils aUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aUtils =new Autils(mhandler);//实例化网络请求工具类
        lv=(ListView)findViewById(R.id.lvId);
        datas=new ArrayList<>();
        adapter=new FeedAdapter(getApplicationContext(),datas, aUtils);
        lv.setAdapter(adapter);

        aUtils.getAsync(Autils.TYPE_TXT, Urls.LIST_URL1);

    }

    //通过GSON解析数据
    private void parseJson(String txt){
        try {
            JSONArray array=new JSONObject(txt)
                    .getJSONObject("paramz")
                    .getJSONArray("feeds");
            Gson gson=new Gson();
            TypeToken<List<Feed>> typeToken =new TypeToken<List<Feed>>(){};
            //通过GSON解析数据
            List<Feed> list=gson.fromJson(array.toString(),typeToken.getType());

            datas.clear();
            datas.addAll(list);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
