package com.gzr.translatedemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public String result="";
	private TextView tv;
	private EditText et;
	private Button btn;
	private String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";//请求的网址
	public String q = "";//请求翻译query
	private String from = "en";//翻译源语言
	private String to = "zh";//译文语言
	private String appid = "20180619000177939";//"你的id";//APP ID
	private String salt = "512";//随机数
	private String secretkey = "TJUubcLqFdwyekmqL_sI";//"你的密钥";//密钥
	private JSONObject js;
	private Transfer transfer;
	private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		intent = new Intent(MainActivity.this, CService.class);
		startService(intent);

        tv = (TextView) findViewById(R.id.tv_text);
        et = (EditText) findViewById(R.id.et_text);
        btn = (Button) findViewById(R.id.btn);
        transfer=new Transfer();
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				q = et.getText().toString();
				//result=transfer.translate(q);
                //tv.setText(result);
                ////出现bug，如果在这里tv.setText,需要点两次才可以
				translate();
				//tv.setText(result);
			}
		});
    }
    public void translate() {//POST请求
		RequestParams params = new RequestParams();
		System.out.println("mian："+q);
		params.addQueryStringParameter("q", q);//请求翻译query
		params.addQueryStringParameter("from", from);//翻译源语言
		params.addQueryStringParameter("to", to);//译文语言
		params.addQueryStringParameter("appid", appid);//APP ID
		params.addQueryStringParameter("salt", salt);//随机数
		params.addQueryStringParameter("sign", MD5.getMD5(appid+q+salt+secretkey));//签名
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "错误",
								Toast.LENGTH_SHORT).show();
					}


					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = 1;
						msg.obj = arg0.result;
						handler.sendMessage(msg);
                        String r=arg0.result;
                        try {
                            js = new JSONObject(r);
                            JSONArray value = js.getJSONArray("trans_result");
                            JSONObject child = null;
                            for(int i=0;i<value.length();i++){
                                child = value.getJSONObject(i);
                                result = child.getString("dst");
                               // tv.setText(result);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
					}
				});
	}
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				String r = (String) msg.obj;
				System.out.println(r);
				try {
					js = new JSONObject(r);
					JSONArray value = js.getJSONArray("trans_result");
					JSONObject child = null;
					for(int i=0;i<value.length();i++){
						child = value.getJSONObject(i);
						result = child.getString("dst");
						tv.setText(result);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						String errorCode = js.getString("error_code");
						if("52001".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "请求超时，请重试",
									Toast.LENGTH_SHORT).show();
						}else if("52002".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "系统错误，请重试",
									Toast.LENGTH_SHORT).show();
						}else if("52003".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "未授权用户，请检查您的appid是否正确",
									Toast.LENGTH_SHORT).show();
						}else if("54000".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "必填参数为空，请检查是否少传参数",
									Toast.LENGTH_SHORT).show();
						}else if("58000".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "客户端IP非法，请检查您填写的IP地址是否正确可修改您填写的服务器IP地址",
									Toast.LENGTH_SHORT).show();
						}else if("54001".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "签名错误，请检查您的签名生成方法",
									Toast.LENGTH_SHORT).show();
						}else if("54003".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "访问频率受限，请降低您的调用频率",
									Toast.LENGTH_SHORT).show();
						}else if("58001".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "译文语言方向不支持，请检查译文语言是否在语言列表里",
									Toast.LENGTH_SHORT).show();
						}else if("54004".equals(errorCode)){
							Toast.makeText(getApplicationContext(), "账户余额不足，请前往管理控制台为账户充值",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
    };
}
