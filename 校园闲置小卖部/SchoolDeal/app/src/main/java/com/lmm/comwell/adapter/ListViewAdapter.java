package com.lmm.comwell.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lmm.comwell.R;
import com.lmm.comwell.bean.Post;

import java.util.List;

//h和comment差不多

public class ListViewAdapter extends BaseAdapter {
    private String images[];
    private Context context;
    private LayoutInflater mInflat;
    public ListViewAdapter(String[] images,Context context) {
        this.images = images;
        this.context = context;
        mInflat=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){

            convertView=  mInflat.inflate(R.layout.photo_item,null,false);
            ImageView iv= (ImageView) convertView.findViewById(R.id.my_photo);
            Glide.with(context)
                    .load(images[position])
                    .skipMemoryCache(true)
                    .into(iv);
        }

        return convertView;
    }
}


