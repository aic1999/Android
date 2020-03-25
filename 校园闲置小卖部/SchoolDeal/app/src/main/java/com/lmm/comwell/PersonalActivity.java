package com.lmm.comwell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lmm.comwell.adapter.MesAdapter;
import com.lmm.comwell.adapter.MyAdapter;
import com.lmm.comwell.adapter.PersonAdapter;
import com.lmm.comwell.bean.Message;
import com.lmm.comwell.bean.Post;
import com.lmm.comwell.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


//我的空间，查看我或者他人发布的商品信息列表
public class PersonalActivity extends AppCompatActivity{
    private TextView user_info_name,user_info_word,user_info_sex,show_data;
    private String objId;
    private RecyclerView info_list;
    private List<Post> mlist;
    private String data_info[]={};
    private PersonAdapter adapter;
    private LinearLayout show_head;
    private SwipeRefreshLayout refresh; //上下啦刷新
    private LinearLayoutManager linearLayoutManager;
    private int mTouchSlop;
    private int com_num;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean mShow = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        user_info_name=(TextView)findViewById(R.id.info_name);
        user_info_word=(TextView)findViewById(R.id.info_word);
        info_list=(RecyclerView) findViewById(R.id.info_list);  //改进
        user_info_sex=(TextView)findViewById(R.id.info_sex);
        refresh=(SwipeRefreshLayout)findViewById(R.id.refresh);
        show_data=(TextView) findViewById(R.id.show_data);
        show_head=(LinearLayout)findViewById(R.id.show_head);
        linearLayoutManager = new LinearLayoutManager(PersonalActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        objId=getIntent().getStringExtra("objId");
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(objId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    if (TextUtils.isEmpty(user.getWords())){
                        user_info_word.setText("这个小孩很懒，什么都没留下");
                    }else {
                        user_info_word.setText(user.getWords());
                    }
                    if (user.getSex().equals("男")){
                        user_info_sex.setText("(帅哥)");
                    }else if (user.getSex().equals("女")){
                        user_info_sex.setText("(美女)");
                    }
                    user_info_name.setText(user.getUsername());


                }else{
                    toast(e.toString());
                }
            }
        });

        person_post();
        mlist=new ArrayList<>();
        adapter= new PersonAdapter(mlist,this);
        info_list.setLayoutManager(linearLayoutManager);
        info_list.setAdapter(adapter);

        //adapter.notifyDataSetInvalidated();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {
                show_data.setText("已加载全部");
                         //adapter.notifyDataSetInvalidated();
                         adapter.notifyDataSetChanged();    //数据变化就生成
                         refresh.setRefreshing(false);
            }
        });

        //begin~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(PersonalActivity.this,CommentActivity.class);
                intent.putExtra("name",data);
                startActivity(intent);
            }
        });
        //~~~~~~~~~~~~~~~~~~~~~end
        //不知道啥用
//        info_list.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        mFirstY = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        mCurrentY = event.getY();
//                        if (mCurrentY - mFirstY > mTouchSlop) {
//                            direction = 0;// down
//                        } else if (mFirstY - mCurrentY > mTouchSlop) {
//                            direction = 1;// up
//                        }
//                        if (direction == 1) {
//                            //上滑todo
//                        } else if (direction == 0) {
//                            //下滑todo
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//
//                }
//                return false;
//            }
//        });

    }
    void person_post(){
        User user =new User();
        user.setObjectId(objId);
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);    // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    for(Post p: list) {
                        p.getContent();
                        p.getTitle();
                        p.getTime();
                        mlist.add(p);
                        System.out.println("22222222222222222-"+mlist.size()+"-3333333333333333333333");
                    }
                }else{
                    toast(e.toString());
                }
            }
        });

    }
    public void toast(String msg) {
        Toast.makeText(PersonalActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                PersonalActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
