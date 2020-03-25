package com.lmm.comwell;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.lmm.comwell.push.MyPushMessageReceiver;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;
// //"68621be1416463e33355878d4252e1f2";
public class BmobApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static BmobApplication instance;
	//自己的key
    public static String APPID ="c93e110e041738076e17e3cb355915e4";
    private BmobApplication() {
    }
    public synchronized static BmobApplication getInstance() {
        if (null == instance) {
            instance = new BmobApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    /**
     * SDK初始化也可以放到Application中
     */


    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,APPID,"bmob");
        // 使用推送服务时的初始化操作
        BmobPush.setDebugMode(true);
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this,APPID);
//设置BmobConfig
        BmobConfig config =new BmobConfig.Builder(BmobApplication.this)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(15)
                //文件分片上传时每片的大小（单位字节），默认512*1024

                .build();

    }


    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        IntentFilter filterl = new IntentFilter(Intent.ACTION_TIME_TICK);
        MyPushMessageReceiver receiverl = new MyPushMessageReceiver();
        registerReceiver(receiver, filterl);
        return super.registerReceiver(receiver, filter);
    }

}
