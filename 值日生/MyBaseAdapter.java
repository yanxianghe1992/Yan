package com.example.administrator.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class MyBaseAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public MyBaseAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.textview, null);
            holder = new MyHolder();
            holder.textView = (TextView) view.findViewById(R.id.my_tv1);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        holder.textView.setText(list.get(i));
        return view;
    }

    class MyHolder {
        TextView textView;
    }
}
