package com.c4_hm_04_news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import domain.NewsItem;
import domain.NewsService;
import loopj.android.image.SmartImageView;

public class MainActivity extends AppCompatActivity {

    private String path="http://113.55.71.193:8080/news.xml";
    private List<NewsItem> items;
    private ListView lv;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        items=NewsService.getAllNewsItems(path);
        loadData();
    }

    private void initView(){
        lv= (ListView) findViewById(R.id.lv);
    }

    private void loadData(){
        if (myAdapter==null){
            myAdapter=new MyAdapter();
            lv.setAdapter(myAdapter);
        }else {
            myAdapter.notifyDataSetChanged();
        }

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView==null){
                view=View.inflate(MainActivity.this,R.layout.item,null);
                viewHolder=new ViewHolder();
                viewHolder.siv= (SmartImageView) view.findViewById(R.id.item_iv);
                viewHolder.title= (TextView) view.findViewById(R.id.item_title);
                viewHolder.desc= (TextView) view.findViewById(R.id.item_desc);
                viewHolder.type= (TextView) view.findViewById(R.id.item_type);
                view.setTag(viewHolder);
            }else {
                view=convertView;
                viewHolder= (ViewHolder) view.getTag();
            }

            NewsItem item=items.get(position);
            String tp=item.getType();
            if ("1".equals(tp)){
                //评论
                viewHolder.type.setText("评论："+item.getComment());
            }else if ("2".equals(tp)){
                //视频
                viewHolder.type.setText("视频");
            }else if ("3".equals(tp)){
                //直播
                viewHolder.type.setText("直播");
            }

            viewHolder.title.setText(item.getTitle());
            viewHolder.desc.setText(item.getDescription());
            Log.i("aaaaaaaaa",item.getImage());
            viewHolder.siv.setImageUrl(item.getImage());
            return view;
        }
    }

    class ViewHolder{
        SmartImageView siv;
        TextView title;
        TextView desc;
        TextView type;
    }

}
