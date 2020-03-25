package com.gzr.translatedemo;


import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CService extends Service {

    private String oldString="";
    private final static String DIVIDE_RESULT = "com.intel.unit.Clipy";
    private boolean stop_state = false;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind----");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("onUnbind----");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate----");
        //get clipboardService
        final ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        new Thread(new Runnable() {
            /**
             *
             */
            @Override
            public void run() {
                while (!stop_state) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Date date=new Date();
                    String time=sdf.format(date);
                    System.out.println("time---->"+time);

                    if(!"2015-06-11 06:11:01".equals(time)){
                        Intent intent = new Intent(DIVIDE_RESULT);

                        String message=clipboardManager.getText().toString();
                        System.out.println("message----->"+message);
                        intent.putExtra("count", message);

                        if(message!=null||message!=""){
                            if(!message.equals(oldString)) {
                                sendBroadcast(intent);
                                oldString=message;
                            }
                        }
                        //clipboardManager.setText(""); danger
                    }

                }
                System.out.println("................");
                CService.this.stopSelf();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand----");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy----");
        super.onDestroy();

        if (stop_state) {
            this.stopSelf();
        } else {
            this.startService(new Intent(this, CService.class));
        }
    }
}