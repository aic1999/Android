package com.lmm.comwell;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lmm.comwell.adapter.MoreUserAdapter;
import com.lmm.comwell.bean.User;
import com.lmm.comwell.utils.Utils;
import com.lmm.comwell.utils.classInterface;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

//首页的个人消息界面
public class Fra_More extends Fragment {
    private ListView lv_user;
    private TextView tv_name,tv_logout,tv_Sex;
    private ListView lv_sectting;
    private ListView lv_more;
    private TextView tv_word;
    private User user ;
    private AlertDialog al;
    private RelativeLayout sex_chose,user_photo_check;
    private MoreUserAdapter adapter;
    private String userName;
    private TextView tv_log;
    private boolean isUser;
    private String url,url2;
    private int[] imageids = { R.mipmap.userl };
    classInterface ci;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_more,container,false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
   init();
    }
    private void getUrl() {
        final String[] obj_info = {""};
        final BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", tv_name.getText().toString());
        //System.out.println("@@@@@@@@@@@@@@@@+"+tv_name.getText().toString());
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {
                for (BmobUser data : list) {
                    obj_info[0] = data.getObjectId();
                    Fra_More.this.url = obj_info[0];    //Fra_More.this.url也没有用
                    System.out.println("@@@@@@@@@@@@@@@@@@+"+url);  //取到了啊
                }
                System.out.println("@@@@@@@@@@@@@@@@@@2+"+url);  //取到了啊
            }
        });
        System.out.println("@@@@@@@@@@@@@@@@@@3+"+url);  //没取到
       /* ci=new classInterface();
        query.findObjects(ci);
        System.out.println("@@@@@@@@@@@@@@@@@@3+"+ci.getUrll());  //没取到
        if(ci.getUrll()==null){
            ci=new classInterface();
            query.findObjects(ci);
            System.out.println("@@@@@@@@@@@@@@@@@@4+"+ci.getUrll());  //没取到
        }*/
    }

    public void init(){

        tv_name=(TextView)getView().findViewById(R.id.fra_name);
        tv_word=(TextView) getView().findViewById(R.id.tv_userword);
        tv_log=(TextView) getView().findViewById(R.id.btn_user_logout);
        sex_chose=(RelativeLayout)getView().findViewById(R.id.sex_choice);
        user_photo_check=(RelativeLayout)getView().findViewById(R.id.user_photo_check);
        user = new User();
        user= BmobUser.getCurrentUser(User.class);
        tv_Sex=(TextView)getView().findViewById(R.id.sex_choice_switch);

        if (user==null){
            isUser=false;
            tv_log.setText("点我登录");
        }else{
            isUser=true;
        }

        if (user==null){
            tv_name.setText("未登录");


        }else {

            user=BmobUser.getCurrentUser(User.class);
            tv_name.setText(user.getUsername());
            tv_Sex.setText(user.getSex());
            if (TextUtils.isEmpty(user.getWords())){
                tv_word.setText("这个小孩很懒，一句话都没留");
            }
            else {
                tv_word.setText(user.getWords());
            }
        }
       sex_chose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //判断网络是否可以使用
               if (user==null){
                   toast("请登陆后修改");
                   return;
               }
               if(Utils.isNetworkConnected(getContext()) ){
                   showDialog_Sex();

               } else{
                   toast("已进入无网络的异次元");
               }

           }
       });
        tv_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user==null){
                    toast("未登录");
                    return;
                }
               showDialog();
            }
        });
           user_photo_check.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //判断网络是否可以使用
                   if(Utils.isNetworkConnected(getContext())){
                       Intent intent = new Intent(getActivity(), PersonalActivity.class);
                       intent.putExtra("objId", url);
                       System.out.println("@@@@----test in fra_more:-------"+url);
                       startActivity(intent);
                   }else{
                       toast("已进入无网络的异次元");
                   }

               }
           });

        tv_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user==null){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                   BmobUser.logOut();
                    toast("已注销，重启程序生效");
                    tv_name.setText("未登录");
                    tv_word.setText("");
                    tv_log.setText("已注销");
                }
            }
        });
        getUrl();
    }
    private void showDialog_Sex() {

     //   final View customView =View.inflate(getActivity(),R.layout.sex_choose_layout,null);
        final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle("你只能选一个")
              //  .setView(customView)
                .setPositiveButton("女", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                          user.setSex("女");
                          user.update(user.getObjectId(), new UpdateListener() {
                              @Override
                              public void done(BmobException e) {
                                  if (e==null){
                                      toast("变性成功");
                                      tv_Sex.setText("女");
                                  }else{
                                      toast("变性失败"+e.toString());

                                  }

                              }
                          });



                    }
                }).setNegativeButton("男", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user.setSex("男");
                user.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            toast("变性成功");
                            tv_Sex.setText("男");
                        }else{
                            toast("变性失败"+e.toString());

                        }

                    }
                });
                dialogInterface.dismiss();

            }
        }).create().show();

    }
    private void showDialog() {
        final View customView =View.inflate(getActivity(),R.layout.input_text_dialog,null);
        final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle("编辑我的个性签名")
                .setView(customView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        EditText textView=(EditText) customView.findViewById(R.id.dia_edit);
                        final String input_word=textView.getText().toString();
                        user.setWords(input_word);
                        user.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    toast("更新签名成功");
                                    tv_word.setText(input_word);
                                    dialogInterface.dismiss();
                                }else{
                                    toast("失败"+e.toString());

                                }
                            }
                        });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        }).create().show();

    }
    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


}
