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
public class RegisterActivity extends AppCompatActivity {
    private EditText et_name,et_pasd;
    private Button register;
    private BmobUser user;
    private String name,pasd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        et_name=(EditText)findViewById(R.id.et_user_reg);
        et_pasd=(EditText)findViewById(R.id.et_pasd_reg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void register() {
        // TODO Auto-generated method stub

        name=et_name.getText().toString();
        pasd=et_pasd.getText().toString();
        if(name.isEmpty()){
            toast("用户名为空");
            return;
        }
        if(pasd.isEmpty()){
            toast("密码为空");
            return;
        }

        User user=new User();
        user.setUsername(name);
        user.setPassword(pasd);
        user.setSex("男");
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    toast("注册成功！");
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast("注册失败！ "+e.toString());
                }
            }
        });

    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void btn_register(View view){
        register();
    }
}
