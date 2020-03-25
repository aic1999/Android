package com.lmm.comwell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lmm.comwell.R;
import com.lmm.comwell.bean.Message;
import com.lmm.comwell.bean.Photo;

import java.util.List;



public class PhotoAdapter  extends BaseAdapter{
    private List<Photo> list;
    private Context context;

    public PhotoAdapter(List<Photo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_item, null);
            new PhotoAdapter.CommentViewHolder(convertView);

            final Photo photo = list.get(position);
            ImageView iv= (ImageView) convertView.findViewById(R.id.my_photo);
            Glide.with(context)
                    .load(photo.getImg_url())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(iv);

            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        PhotoAdapter.CommentViewHolder holder = (PhotoAdapter.CommentViewHolder) convertView.getTag();

        final Photo photo = list.get(position);
        ImageView iv= (ImageView) convertView.findViewById(R.id.my_photo);
        Glide.with(context)
                .load(photo.getImg_url())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(iv);

        iv.setScaleType(ImageView.ScaleType.FIT_XY);
       // Glide.with(context).load(photo.getImg_url()).into(holder.imageView);
        return convertView;

    }

    class CommentViewHolder {
        TextView title, content, time;
        ImageView imageView;

        public CommentViewHolder(View view) {
               imageView=(ImageView)view.findViewById(R.id.my_photo);
               view.setTag(this);
        }
    }
}
