package com.gzr.translatedemo;

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

public class Transfer {
    private static String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";//请求的网址
    public  static String q = "you";//请求翻译query
    private  static String from = "en";//翻译源语言
    private  static String to = "zh";//译文语言
    private  static String appid = "20180619000177939";////APP ID
    private  static String salt = "512";//随机数
    private  static String secretkey = "TJUubcLqFdwyekmqL_sI";////密钥
    public static String result;
    private  static JSONObject js;

    private static RequestParams params;
    private static HttpUtils http;

    public  static  String translate(String q) {//POST请求
        System.out.println("!!!!!!!!!!!!!!!!!!"+q);
        params = new RequestParams();
        params.addQueryStringParameter("q", q);//请求翻译query
        params.addQueryStringParameter("from", from);//翻译源语言
        params.addQueryStringParameter("to", to);//译文语言
        params.addQueryStringParameter("appid", appid);//APP ID
        params.addQueryStringParameter("salt", salt);//随机数
        params.addQueryStringParameter("sign", MD5.getMD5(appid+q+salt+secretkey));//签名
        http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub

                        String r=arg0.result;
                        try {
                            js = new JSONObject(r);
                            JSONArray value = js.getJSONArray("trans_result");
                            JSONObject child = null;
                            for(int i=0;i<value.length();i++){
                                child = value.getJSONObject(i);
                                result = child.getString("dst");
                                System.out.println("mygodmygodgodgodwhywhy"+result);
                            }
                            System.out.println("crycrycrycyr=="+result);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
        System.out.println("????????????????????"+result);
        return result;
    }

}
