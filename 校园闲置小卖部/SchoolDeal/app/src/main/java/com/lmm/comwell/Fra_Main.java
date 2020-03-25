package com.lmm.comwell;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmm.comwell.adapter.MyAdapter;
import com.lmm.comwell.bean.Post;
import com.lmm.comwell.bean.User;
import com.lmm.comwell.utils.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//主页

public class Fra_Main extends Fragment {
    private LinearLayout bottom_nav_content;
    private TextView mTextMessage;
    private FloatingActionButton fab;
    private FragmentManager fragmentManager;
    private BmobUser user ;
    private RecyclerView recyclerView;
    private String content;
    private MyAdapter adapter;
    private AlertDialog al;
    private List<Post> mlist;
    private ListView lv;
    private SwipeRefreshLayout refresh;

    private  Fra_Main fra_main;
    private  Fra_Mes fra_mes;
    private  Fra_More fra_more;
    private RelativeLayout rl_post;
    private MediaPlayer mp;//播放提示音
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private TextView tv_content,tv_User,nav_user;
    AlertDialog.Builder builder;
    CustomLinearLayoutManager linearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initl();
    }
    void init(){
        user = new User();
        user= BmobUser.getCurrentUser();
        refresh=(SwipeRefreshLayout)getView().findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {
                linearLayoutManager.setScrollEnabled(false);
                getData();
            }
        });
    }
    public void initl(){
        recyclerView=(RecyclerView) getView().findViewById(R.id.recyclerView);
        tv_content=(TextView)getView().findViewById(R.id.tv_post_content);
        fab=(FloatingActionButton)getView().findViewById(R.id.fab);     //浮窗球
        refresh=(SwipeRefreshLayout)getView().findViewById(R.id.refresh);
        rl_post=(RelativeLayout)getView().findViewById(R.id.rl_post);
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager = new CustomLinearLayoutManager(this.getContext());
        BmobApplication.getInstance().addActivity(getActivity());
        mlist=new ArrayList<Post>();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter(getActivity(),mlist);
        recyclerView.setAdapter(adapter);

        getData();
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(getActivity(),CommentActivity.class);
                intent.putExtra("name",data);
                startActivity(intent);
               // getActivity().finish();
            }
        });


    }
    private void getData() {
        // TODO Auto-generated method stub
        this.refresh.setRefreshing(true);
        final Post post = new Post();
        BmobQuery<Post> bq = new BmobQuery<Post>();
        bq.setLimit(6);
        bq.order("-updatedAt");
        bq.include("author");
        bq.include("username");
        bq.include("time");
        bq.include("title");
        bq.include("img_url");
        bq.include("user.objectId");
        mlist.clear();
        bq.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null) {
                    for (Post p : list) {
                        p.getContent();
                        p.getName();
                        p.getTitle();
                        p.getTime();
                        mlist.add(p);

                        refresh.setRefreshing(false);
                        linearLayoutManager.setScrollEnabled(true);
                        adapter.notifyDataSetChanged();

                    }
                }else{
                    toast("加载失败" );
                    refresh.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void initSP() throws Exception {
        mp = MediaPlayer.create(getContext(), R.raw.fininsh_rool);
        mp.start();
        mp.setLooping(true);
    }

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Material Design Dialog");

        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    }


