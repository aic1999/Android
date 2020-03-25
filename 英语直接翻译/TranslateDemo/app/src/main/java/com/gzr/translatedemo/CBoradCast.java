package com.gzr.translatedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

public class CBoradCast extends BroadcastReceiver {
    private MainActivity ma;
    private static String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";//请求的网址
    public static String q = "you";//请求翻译query
    private static String from = "en";//翻译源语言
    private static String to = "zh";//译文语言
    private static String appid = "20180619000177939";//"你的id";//APP ID
    private static String salt = "512";//随机数
    private static String secretkey = "TJUubcLqFdwyekmqL_sI";//"你的密钥";//密钥
    public static String result;
    private static JSONObject js;
    private  Transfer transfer;
    private String finalRes;

    //private int inittime=0;
    private static final String Action_Name="com.intel.unit.Clipy";


    @Override
    public void onReceive(Context context, Intent intent) {
        transfer=new Transfer();
        String action=intent.getAction();
        if(Action_Name.equals(action)){//&&(inittime>0)){
            String content=intent.getStringExtra("count");
//            Transfer.q=content;
//            Transfer.translate();
//            System.out.println("广播得到的结果："+Transfer.result);
           // q=content;
            //translate();
            String result=Transfer.translate(content);
            System.out.println("aiaiaiaiai"+result);
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
        //System.out.println("次数："+inittime);
        //inittime++; //这个不是全局变量？？？每一次都是新的initime...

    }
//    public void translate() {//POST请求
//        RequestParams params = new RequestParams();
//        System.out.println("fanfanfan的bug："+q);
//        params.addQueryStringParameter("q", q);//请求翻译query
//        params.addQueryStringParameter("from", from);//翻译源语言
//        params.addQueryStringParameter("to", to);//译文语言
//        params.addQueryStringParameter("appid", appid);//APP ID
//        params.addQueryStringParameter("salt", salt);//随机数
//        params.addQueryStringParameter("sign", MD5.getMD5(appid+q+salt+secretkey));//签名
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, url, params,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> arg0) {
//                        // TODO Auto-generated method stub
//                        Message msg = new Message();
//                        msg.what = 1;
//                        msg.obj = arg0.result;
//                        handler.sendMessage(msg);
//                        String r=arg0.result;
//                        try {
//                            js = new JSONObject(r);
//                            JSONArray value = js.getJSONArray("trans_result");
//                            JSONObject child = null;
//                            for(int i=0;i<value.length();i++){
//                                child = value.getJSONObject(i);
//                                result = child.getString("dst");
//                                finalRes=result;
//                                // tv.setText(result);
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//    private android.os.Handler handler = new android.os.Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                String r = (String) msg.obj;
//                System.out.println(r);
//                try {
//                    js = new JSONObject(r);
//                    JSONArray value = js.getJSONArray("trans_result");
//                    JSONObject child = null;
//                    for(int i=0;i<value.length();i++){
//                        child = value.getJSONObject(i);
//                        result = child.getString("dst");
//                        finalRes=result;
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//
//                }
//            }
//        }
//    };

}

