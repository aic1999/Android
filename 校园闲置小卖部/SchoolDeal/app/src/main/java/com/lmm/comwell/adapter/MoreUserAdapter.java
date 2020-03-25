package com.lmm.comwell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lmm.comwell.R;
import com.lmm.comwell.bean.Post;

import java.util.List;



public class MoreUserAdapter extends BaseAdapter {
    private List<Post> list;
    private Context context;

    public MoreUserAdapter(Context context,List<Post> list) {
        this.context = context;
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
        // TODO Auto-generated method stub
        //if(convertView==null){
        convertView= LayoutInflater.from(context).inflate(R.layout.item_more, null);
        new IndexViewHolder(convertView);
        //}
        IndexViewHolder holder = (IndexViewHolder) convertView.getTag();
        final Post post=list.get(position);

        //holder.tvAuthor.setText(post.getAuthor().getUsername());


        return convertView;
    }

    class IndexViewHolder{
        TextView tvAuthor,tvContent,tvPraise;

        public IndexViewHolder(View v){
            tvAuthor=(TextView)v.findViewById(R.id.user_more);

            v.setTag(this);
        }
}
}
