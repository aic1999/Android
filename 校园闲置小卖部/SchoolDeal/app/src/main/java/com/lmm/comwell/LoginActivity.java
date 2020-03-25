package com.lmm.comwell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lmm.comwell.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/5/2.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText log_user,log_pasd;
    private BmobUser user;
    private Button btn_log;
    String name,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);
        log_user = (EditText) findViewById(R.id.et_user);
        log_pasd = (EditText) findViewById(R.id.et_pasd);
        btn_log=(Button)findViewById(R.id.btn_log);
        user=BmobUser.getCurrentUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void login() {
        // TODO Auto-generated method stub
        name=log_user.getText().toString();
        password=log_pasd.getText().toString();
        if (name.isEmpty()) {
            toast("用户名为空");
            return;
        }
        if (password.isEmpty()) {
            toast("密码为空");
            return;
        }
       // isChecked = true;


        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    toast("登录成功！");
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast("登录失败！ " + e.toString());
                }
            }
        });

    }
    public void reg(View view){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();

    }
    public void log(View view){
    login();
    }
}
