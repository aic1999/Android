package com.lmm.comwell.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmm.comwell.R;
import com.lmm.comwell.bean.Post;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.CommentViewHolder>  {
    private List<Post> list;
    private Context context;
    private MyAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;


    public PersonAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    //Item操作
    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        final Post post = list.get(position);
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
        holder.time.setText(post.getTime());
        holder.itemView.setTag(this);
        holder.rl_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("=================================");
                System.out.println(post.getContent()+ ","+post.getName()+","+post.getObjectId()+","+post.getPraise()
                        +","+post.getImg_url());
                mOnItemClickListener.onItemClick(holder.itemView, post.getContent()+ ","+post.getName()+","+post.getObjectId()+","+post.getPraise()
                        +","+post.getImg_url());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //整个自定义控件操作
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_person_item, null);
        CommentViewHolder holder= new CommentViewHolder(view);
        //view.setOnClickListener(this);
        return holder;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rl_post;
        TextView title, content, time;
            public CommentViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.info_item_title);
                content = (TextView) view.findViewById(R.id.info_item_content);
                time = (TextView) view.findViewById(R.id.info_item_time);
                rl_post = (LinearLayout) itemView.findViewById(R.id.rl_post); //主页那个像卡片的东西
                view.setTag(this);
            }
    }
    //~~~~~~~~~~~~~~~~~~~~~~
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(MyAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}


