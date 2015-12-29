package com.c3.vero.c3_5_02_web_listview;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vero on 2015/12/10.
 */
public class StudentAdapter extends ArrayAdapter<Student>{
    private List<Student> students;
    private LayoutInflater inflater;
    private int res;

    public StudentAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        students=objects;
        inflater=LayoutInflater.from(context);
        res=resource;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Student getItem(int position) {
        return students.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=inflater.inflate(res,null);
            viewHolder =new ViewHolder();
            viewHolder.sid=(TextView)view.findViewById(R.id.sid);
            viewHolder.name=(TextView)view.findViewById(R.id.name);
            viewHolder.age=(TextView)view.findViewById(R.id.age);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        Student student=students.get(position);
        viewHolder.sid.setText(student.getSid());
        viewHolder.name.setText(student.getName());
        viewHolder.age.setText(String.valueOf(student.getAge()));

        return view;
    }

    class ViewHolder{
        public TextView sid;
        public TextView name;
        public TextView age;
    }
}
