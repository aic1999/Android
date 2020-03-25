package com.lmm.comwell.utils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//解决监听类取不到值，测试，已无用，删。
//为了得到匿名监听里面的数值
public class classInterface extends FindListener<BmobUser>{
    final String[] obj_info = {""};
    private  String urll;
    @Override
        public void done(List<BmobUser> list, BmobException e) {
            for (BmobUser data : list) {
                obj_info[0] = data.getObjectId();
                urll = obj_info[0];
            }
        System.out.println("mmmmmmmmmmmmmmmm+"+getUrll());  //取到了啊
    }
      public String getUrll(){
        return urll;
      }
}
