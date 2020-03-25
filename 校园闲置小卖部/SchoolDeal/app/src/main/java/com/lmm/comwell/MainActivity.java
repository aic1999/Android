package com.lmm.comwell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmm.comwell.adapter.MyAdapter;
import com.lmm.comwell.bean.Post;
import com.lmm.comwell.bean.User;
import com.lmm.comwell.push.MyPushMessageReceiver;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private FragmentManager fragmentManager;
    private User user ;
    private RecyclerView recyclerView;
    private String content;
    private MyAdapter adapter;
    private AlertDialog al;
    private List<Post> list;
    private String appkey="c93e110e041738076e17e3cb355915e4";
    private ListView lv;
    private SwipeRefreshLayout refresh;
    private LinearLayoutManager linearLayoutManager;
    private  Fra_Main fra_main;
    private  Fra_Mes fra_mes;
    private  Fra_More fra_more;
    private RelativeLayout rl_post;
    private long mExitTime;
    BmobPushManager<BmobInstallation> bmobPush;
    private String obj;
    private TextView tv_content,tv_User,nav_user;
    public static String APPID = "c93e110e041738076e17e3cb355915e4";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //屏蔽无用消息
               /* case R.id.navigation_home:
                    setTabSelection(0);
                    setTitle("消息");
                    fab.setVisibility(View.INVISIBLE);
                    return true;*/
                case R.id.navigation_dashboard:
                    setTabSelection(1);
                    setTitle("主页");
                    fab.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    setTabSelection(2);
                    setTitle("更多");
                    fab.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,APPID,"bmob");
        BmobPush.setDebugMode(true);
        BmobInstallation.getCurrentInstallation().save();
        BmobApplication.getInstance().addActivity(this);
        // 启动推送服务
        bmobPush = new BmobPushManager<BmobInstallation>();
        BmobPush.startWork(MainActivity.this,appkey);
        fab=(FloatingActionButton)findViewById(R.id.fab);   //小加号
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BmobUser user = new BmobUser();
        user=BmobUser.getCurrentUser();
        setTabSelection(1); //底部栏的显示界面是什么，一开始是0的话就是消息（meum中已经删了）
        setTitle("消息");
        final BmobUser finalUser = user;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUser ==null){
                    toast("发表前先登录");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditActivity.class);
                    startActivity(intent);

            }
        });
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                if ((System.currentTimeMillis() - mExitTime) > 1000) {

                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                   BmobApplication.getInstance().exit();
                    moveTaskToBack(false);

                 //   System.exit(0);
                  //  this.finish();
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (fra_mes == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    fra_mes = new Fra_Mes();
                    transaction.add(R.id.content, fra_mes);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fra_mes);
                }
                break;
            case 1:
                if (fra_main == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    fra_main = new Fra_Main();
                    transaction.add(R.id.content, fra_main);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(fra_main);
                }
                break;
            case 2:
                if (fra_more == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    fra_more = new Fra_More();
                    transaction.add(R.id.content, fra_more);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(fra_more);
                }
                break;
            default:
                transaction.commit();
                break;
        }
        transaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fra_mes != null) {
            transaction.hide(fra_mes);
        }
        if (fra_main != null) {
            transaction.hide(fra_main);
        }
        if (fra_more != null) {
            transaction.hide(fra_more);
        }

    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent("cn.bmob.push.lib.service.PushService");
        sendBroadcast(intent);
        Intent sevice = new Intent(this, MyPushMessageReceiver.class);
        this.startService(sevice);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.out:
                if(user==null){
                    toast("当前无用户");

                }else {
                    BmobUser.logOut();
                    toast("已注销，重启程序生效");
                }
            case R.id.exit:
                BmobApplication.getInstance().exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);

        return true;
    }


    private void pushToAndroid(String message){
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("deviceType", "android");
        bmobPush.setQuery(query);
        bmobPush.pushMessage(message);
    }



    public void showDialog() {
        LayoutInflater inflater = getLayoutInflater();

        al= new AlertDialog.Builder(this)
                .setTitle("iTell")
                .setView(R.layout.dialog)

                .show();

    }



    public void toast(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
    }
}
