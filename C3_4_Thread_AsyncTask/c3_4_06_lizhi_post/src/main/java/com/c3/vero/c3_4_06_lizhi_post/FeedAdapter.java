package com.c3.vero.c3_4_06_lizhi_post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vero on 2015/12/3.
 */
public class FeedAdapter extends BaseAdapter {

    private Context context;
    private List<Feed> datas;
    private Autils aUtils;
    public FeedAdapter(Context context, List<Feed> datas,Autils aUtils){
        this.context=context;
        this.datas=datas;
        this.aUtils=aUtils;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return datas.get(position).getOid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_feed,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.coverId);
            viewHolder.subjectTv=(TextView)convertView.findViewById(R.id.subjectId);
            viewHolder.summaryTv=(TextView)convertView.findViewById(R.id.summaryId);

            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
            viewHolder.imageView.setImageResource(android.R.mipmap.sym_def_app_icon);
        }
        viewHolder.subjectTv.setText(datas.get(position).getData().getSubject());
        viewHolder.summaryTv.setText(datas.get(position).getData().getSummary());

        String imgPath=Urls.BASE_URL+datas.get(position).getData().getCover();

        viewHolder.imageView.setTag(imgPath);
        aUtils.getAsync(aUtils.TYPE_IMG,imgPath);

        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView subjectTv,summaryTv;

    }
}
