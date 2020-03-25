package com.lmm.comwell.adapter;


import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmm.comwell.R;
import com.lmm.comwell.bean.Message;

//不要的功能
public class MesAdapter extends BaseAdapter {
    private List<Message> list;
    private Context context;

    public MesAdapter(List<Message> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.mes_item, null);
            new CommentViewHolder(convertView);
        }
        CommentViewHolder holder = (CommentViewHolder) convertView.getTag();

        final Message message = list.get(position);
        holder.title.setText(message.getTitle());
        holder.content.setText(message.getContent());
        holder.time.setText(message.getTime());
        return convertView;

    }

    class CommentViewHolder {
        TextView title, content, time;

        public CommentViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.mes_title);
            content = (TextView) view.findViewById(R.id.mes_content);
            time = (TextView) view.findViewById(R.id.mes_time);
            view.setTag(this);
        }
    }
}
