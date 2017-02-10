package com.malone.greendaodemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malone.greendaodemo.bean.Girl;

import java.util.List;

/**
 * Created by luchunyang on 2017/2/10.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Girl> list;

    public MyAdapter(Context context, List<Girl> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<Girl> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item,null);

        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvUrl = (TextView) view.findViewById(R.id.tvUrl);

        Girl girl = list.get(position);
        tvName.setText(girl.getName() +"(id="+girl.getId()+")");
        tvUrl.setText(girl.getUrl());

        if(girl.getData() == null)
            iv.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.photo));
        else {
            //sqlite 存储了Bitmap字节数组
            byte[] data = girl.getData();
            iv.setImageBitmap(BitmapFactory.decodeByteArray(data,0,data.length));
        }

        return view;
    }
}
