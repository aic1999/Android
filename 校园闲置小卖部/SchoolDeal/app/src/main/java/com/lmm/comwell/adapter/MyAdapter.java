package com.lmm.comwell.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lmm.comwell.R;
import com.lmm.comwell.bean.Post;

import java.util.List;

import cn.bmob.v3.BmobUser;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> implements View.OnClickListener {
    private LayoutInflater myInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<Post> list;
    private String user_obj;
    private BmobUser user;
    private Context context;

    public MyAdapter(Context context, List<Post> list) {

        this.context=context;
        this.list = list;
        myInflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = myInflater.inflate(R.layout.card_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Post post = list.get(position);
        if (list.size()<0){
            return;
        }
        holder.content.setText(post.getContent());
        holder.time.setText(post.getTime());
        holder.tv_user.setText(post.getName());
        holder.tvPraise.setText("阅览:"+post.getPraise());
        if (post.getImg_url()==null){
            holder.post_image.setVisibility(View.GONE);
        }
        else {
            holder.tv_url.setText(post.getImg_url());
            Glide.with(context).load(post.getImg_url()).placeholder(R.mipmap.loading).thumbnail(0.3f).error(R.mipmap.loading).dontAnimate().into(holder.post_image);
        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(this);
        //获取POST USER OBJID
        if (mOnItemClickListener != null) {
            holder.rl_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   //一整个卡片的监听
                    user=BmobUser.getCurrentUser();
                    if(user==null){
                        Toast.makeText(context,"登陆后查看详情",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mOnItemClickListener.onItemClick(holder.itemView, post.getContent()+ ","+post.getName()+","+post.getObjectId()+","+post.getPraise()
                            +","+post.getImg_url());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user,tv_url;
        TextView content;
        TextView time;
        CardView rl_post;
        TextView tvPraise;
        ImageView post_image;
        ItemViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_post_content);
            time=(TextView)itemView.findViewById(R.id.tv_post_time);
            tv_user=(TextView)itemView.findViewById(R.id.tv_post_name);
            tvPraise=(TextView)itemView.findViewById(R.id.tvPraise);
            rl_post = (CardView) itemView.findViewById(R.id.card_view); //主页那个像卡片的东西
            post_image=(ImageView)itemView.findViewById(R.id.post_img);
            tv_url=(TextView)itemView.findViewById(R.id.tv_url);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}