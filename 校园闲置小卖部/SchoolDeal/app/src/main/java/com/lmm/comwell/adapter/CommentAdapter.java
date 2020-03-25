package com.lmm.comwell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lmm.comwell.R;
import com.lmm.comwell.bean.Comment;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
	private List<Comment> list;
	private Context context;

	public CommentAdapter(List<Comment> list, Context context) {
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
		CommentViewHolder holder;

		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.comm_item, null);
			holder=new CommentViewHolder();
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_comm_author);
			holder.tvContent=(TextView) convertView.findViewById(R.id.tv_comm_content);
			holder.tvTime=(TextView) convertView.findViewById(R.id.comm_time);

			convertView.setTag(holder);
		}else{
			holder=(CommentViewHolder)convertView.getTag();
		}
		final Comment comment=list.get(position);

		holder.tvName.setText(comment.getUser().getUsername());
		holder.tvContent.setText(comment.getContent());
	    holder.tvTime.setText(comment.getTime());

		return convertView;
	}

	class CommentViewHolder{
		TextView tvName;
		TextView tvContent;
		TextView tvTime;


	}

}
