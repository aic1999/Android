/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.BluetoothChat;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.BluetoothChat.ui.circle;
import com.example.android.BluetoothChat.ui.wheelview;
import com.example.android.BluetoothChat.util.GetSimplePhotoHelper;
import com.example.android.BluetoothChat.util.SimplePhoto;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL;

import static android.R.attr.action;
import static android.R.attr.id;
import static android.R.attr.text;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.R.id.input;
import static android.view.MotionEvent.ACTION_POINTER_1_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_INDEX_MASK;
import static com.example.android.BluetoothChat.R.drawable.wheel;
import static com.example.android.BluetoothChat.R.drawable.yuan;
import static com.example.android.BluetoothChat.R.id.ed1;
import static com.example.android.BluetoothChat.R.id.ed2;
import static com.example.android.BluetoothChat.R.id.ed3;
import static com.example.android.BluetoothChat.R.id.ed4;
import static com.example.android.BluetoothChat.R.id.iimageButton1;
import static com.example.android.BluetoothChat.R.id.in;
import static com.example.android.BluetoothChat.R.id.new_devices;
import static com.example.android.BluetoothChat.R.id.rl;
import static com.example.android.BluetoothChat.R.id.textView;
import static com.example.android.BluetoothChat.R.id.textView3;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity implements
        OnClickListener,
        View.OnTouchListener,
        View.OnLongClickListener
{
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    private View popset,popset1,popset2,popset_small,popset_small2,pri_window;
    private PopupWindow set,set1,pop_small;
    private long[] mHits = new long[2];
    private ViewGroup rl;
    private Button newone;
    int id_pri=-1;
    private int screenWidth,screenHeight;	//计算屏幕相关信息用，为了横屏适配
    String input;
    private int lastX, lastY,left,top  ,right , bottom;		//给控件返回当前手指位于屏幕的信息
    private String[] values;
    private ToggleButton tb;
    private GestureDetector gd;
    int length;
    TextView t1,t2,t3,t4;
    ImageButton cz_button,cz_wheel,cz_add,cz_luopan;
    ImageButton yuan,border,border2,tri,power,stop,add2;
    private GridLayout mGridLayout,mmGridLayout;
    TextView pop_remove,pop_set,pop_change;//长安弹窗控件
    View change;//记录是哪个当前长安的按钮弹出窗口
    int mode;//记录手指个数
    int mode2;//解决同时缩放移动问题,用于判断是否当前是第一个手指
 //   int mode3 ;//解决上一个控件的缩放值变成下一个的pri值(非本意1.0F).用于判断控件是否是第一次缩放。额，在创建Button的时候直接“初始化”吧
   // int mode4=0;//解决一指头不动，另外一个多次点击，每次点击后，放大缩小一点点的问题；判断第二个手指是否在屏幕.额，直接把oldish放在创建点（第二个手指按下）初始化为0
    //int  scale;//判断图片大概有多大,和mode3类似的问题，但是这次是真的要绑定每一个控件，用数组。
    //int  scale[30];//不可以这么写，java里要new啊。。
	//以下代码为了实现控件的缩放功能
    private int scale[]=new int[30];
    private float suoxiao;	//缩放比例
    private int big,small;//因存在缩放移动bug，所以弄两个数值，相减
    private float newDist,oldDist;//测试用
    float pri_scaleF=1.0F,atf_scaleF=1.0F;//记录缩放度数
    //float jilu;//本是中间变量，但是因为过于灵敏导致出现“无穷”（实际是反应不过来，所以就换一种方法了）。
    float jilu=0.01F;//迭代变量

    private String luopan_up="Z",luopan_down="Z",luopan_left="Z",luopan_right="Z",luopan;
    private String wheel_left="Z",wheel_right="Z";
    private Bitmap bm,photo,photo2;		//记录相册图片
    GetSimplePhotoHelper mPhotoHelper;
    private float pri_circle=0F,aft_circle=0F;
    static final int CHOOSE_PHOTO=33;
    static final int CHOOSE_PHOTO2=44;
    RelativeLayout.LayoutParams p;
    Button minenewone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout

          requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set up the custom title
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

       init();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");

    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
//        Toast.makeText(this,String.valueOf(tb.isChecked()),Toast.LENGTH_SHORT).show();
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mChatService.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;

            case CHOOSE_PHOTO:
                Uri uri = data.getData();
                try {
//                    if (photo != null)
//                        photo.recycle();不要这个就不会报错
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            uri);
                   // String[] proj = {MediaStore.Images.Media.DATA};
                  ///  Cursor cursor = managedQuery(uri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                  //  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                  //  cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                   // String path = cursor.getString(column_index);
                 //   System.out.println(path+"！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                  ImageButton bt=new   ImageButton(this);
                Drawable temp=new BitmapDrawable(photo);
                 bt.setBackground(temp);
//                Drawable newtemp=zoomDrawable(temp,70,70);
//                bt.setBackground(newtemp);
           //     bt.setLayoutParams(p);
                //     bt.setImageDrawable(temp);
//                bt.setMaxWidth(70);
//                 bt.setMaxHeight(70);
//               bt.setScaleType(ImageView.ScaleType.FIT_XY);
//                bt.setAdjustViewBounds(true);
               // bt.setBackgroundResource(temp);
               mGridLayout.addView(bt,p);
                bt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minenewone=new Button(BluetoothChat.this);
                        minenewone.setBackground(v.getBackground());
                        setminenewone();
                    }
                });
                break;
            case CHOOSE_PHOTO2:
                Uri uri2 = data.getData();
                try {
                    photo2 = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            uri2);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                Button b=new Button(this);
                Drawable tem=new BitmapDrawable(photo2);
                b.setBackground(tem);
             //   b.setBackgroundResource(tem);
                mmGridLayout.addView(b,p);
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        minenewone=new Button(BluetoothChat.this);
                        minenewone.setBackground(v.getBackground());
                        setminenewone();
                    }
                });


                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }

    public void init(){
        p=new RelativeLayout.LayoutParams(70, 70);
         p.setMargins(10,10,10,10);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;

        popset =getLayoutInflater().inflate(R.layout.pop_set, null);
        popset_small=getLayoutInflater().inflate(R.layout.pop_small, null);
        pri_window=getLayoutInflater().inflate(R.layout.main, null);

        popset1 =getLayoutInflater().inflate(R.layout.pop_set1, null);
        popset2 =getLayoutInflater().inflate(R.layout.pop_set2, null);
        length=(screenHeight-popset.getWidth())/4;
        tb=(ToggleButton) popset.findViewById(R.id.toggleButton);
        values = new String[50];
        gd=new GestureDetector(new Zi());
        gd.setIsLongpressEnabled(false);

        t1=(TextView)popset.findViewById(R.id.t1);
        t2=(TextView)popset.findViewById(R.id.t2);
        t3=(TextView)popset.findViewById(R.id.t3);
        t4=(TextView)popset.findViewById(R.id.t4);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);

        rl=(ViewGroup)findViewById(R.id.rl);
        cz_button=(ImageButton)popset1.findViewById(R.id.imageButton1);
        cz_button.setOnClickListener(this);
        cz_wheel=(ImageButton)popset1.findViewById(R.id.imageButton2);
        cz_wheel.setOnClickListener(this);
        cz_add=(ImageButton)popset1.findViewById(R.id.imageButton3);
        cz_add.setOnClickListener(this);
        cz_luopan=(ImageButton)popset1.findViewById(R.id.imageButton4);
        cz_luopan.setOnClickListener(this);
        yuan=(ImageButton)popset2.findViewById(R.id.iimageButton1);
        yuan.setOnClickListener(this);
        border=(ImageButton)popset2.findViewById(R.id.iimageButton2);
        border.setOnClickListener(this);
        border2=(ImageButton)popset2.findViewById(R.id.iimageButton3);
        border2.setOnClickListener(this);
        tri=(ImageButton)popset2.findViewById(R.id.iimageButton4);
        tri.setOnClickListener(this);
        power=(ImageButton)popset2.findViewById(R.id.iimageButton5);
        power.setOnClickListener(this);
        stop=(ImageButton)popset2.findViewById(R.id.iimageButton6);
        stop.setOnClickListener(this);
        add2=(ImageButton)popset2.findViewById(R.id.iimageButton7);
        add2.setOnClickListener(this);

        pop_remove=(TextView)popset_small.findViewById(textView);
        pop_set=(TextView)popset_small.findViewById(R.id.textView2);
        pop_change=(TextView)popset_small.findViewById(R.id.textView3);
        pop_remove.setOnClickListener(this);
        pop_set.setOnClickListener(this);
        pop_change.setOnClickListener(this);

    }
    @Override
    public void onClick(final View view) {
       newone=new Button(this);
        switch (view.getId()){
            case  R.id.t1:
                set1 = new PopupWindow(popset1, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                setSet();
                set1.showAtLocation(pri_window,0,popset.getWidth()+length,0);
                break;

            case R.id.t2:
                set1 = new PopupWindow(popset2, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                setSet();
                set1.showAtLocation(pri_window,0,popset.getWidth()+length,100);
                //findR.id.ttbutton
                break;
            case R.id.t3:
                tb.setChecked(!tb.isChecked());
                break;
            case R.id.t4:
                //设置就存储
                if(tb.isChecked()){
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();;
                    editor.putInt("bg",R.drawable.button1);
                    editor.putString("sendv","A");
                    editor.putInt("bgid",1);
                    editor.putInt("bgleft",233);
                    editor.putInt("bgtop",79);
                    editor.apply();
                }else{
                    SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                    Button nn=new Button(this);
                    nn.setOnTouchListener(this);
                    nn.setOnLongClickListener(this);
                    pri_scaleF=1.0F;
                    atf_scaleF=1.0F;
                     nn.setBackgroundResource(pref.getInt("bg",0));
                    rl.addView(nn,100,100);
                    int left=pref.getInt("bgleft",0);
                    int top=pref.getInt("bgtop",0);
                    int right=left+100;
                    int bottom=top+100;
                    nn.setId(pref.getInt("bgid",0));
                    values[nn.getId()]=pref.getString("sendv","Z");
                    id_pri=1;
                     nn.layout(left, top, right, bottom);
                ViewGroup.MarginLayoutParams lp =(ViewGroup.MarginLayoutParams)nn.getLayoutParams();
                lp.leftMargin =left;
                lp.topMargin = top ;
                nn.setLayoutParams(lp);
                }
                break;

            case  R.id.imageButton1:
                newone.setBackgroundResource(R.layout.button1);
                setnewone();
                break;
            case R.id.imageButton2:
                wheelview ccc=new wheelview(this);
                ccc.setBackgroundResource(R.drawable.wheel);
                  ccc.startPropertyAnim(ccc);
                rl.addView( ccc,70,70);
                ccc.setOnTouchListener(this);
                ccc.setOnLongClickListener(this);
                ccc.setId(++id_pri);
                values[ccc.getId()]="ZZ";
                break;
            case R.id.imageButton3 :
                 mGridLayout=(GridLayout)popset1.findViewById(R.id.gl1);
                getCamerView();
                break;
            case R.id.imageButton4 :
                luopan_up="Z";luopan_down="Z";luopan_left="Z";luopan_right="Z";
                luopan=luopan_left+luopan_up+luopan_right+luopan_down;
                circle  c=new circle(this);
                rl.addView( c);
                 c.setOnTouchListener(this);
                c.setOnLongClickListener(this);
                c.setId(++id_pri);
                values[c.getId()]=luopan;
                break;

            case R.id.iimageButton1:
                newone.setBackgroundResource(R.layout.yuan);
                setnewone();
                break;
            case R.id.iimageButton2:
                newone.setBackgroundResource(R.layout.border);
                setnewone();
                break;
            case R.id.iimageButton3:
                newone.setBackgroundResource(R.layout.border2);
                setnewone();
                break;
            case R.id.iimageButton4:
                newone.setBackgroundResource(R.layout.tri);
                setnewone();
                break;
            case R.id.iimageButton5:
                newone.setBackgroundResource(R.drawable.power);
                setnewone();
                break;
            case R.id.iimageButton6:
                newone.setBackgroundResource(R.drawable.stop);
                setnewone();
                break;
            case R.id.iimageButton7:
                mmGridLayout=(GridLayout)popset2.findViewById(R.id.gl2);
                getCamerView2();

                break;

            case textView:
               rl.removeView(change);
                pop_small.dismiss();
                break;
            case R.id.textView2:
                if(change.getClass().equals(circle.class)){
                    popSetvaules2();
                }else if(change.getClass().equals(wheelview.class)){
                  popSetvaules3();
                }else {
                    popSetvaules();
                 }
                break;
            case R.id.textView3:
                if(change.getClass().equals(circle.class)||change.getClass().equals(wheelview.class)){
                }else{
                    change.setBackground(toturn(change.getBackground()));
                }
                break;

            default:break;
        }
    }

    //得到相册图片
    private void getCamerView() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent,CHOOSE_PHOTO);
    }
    private void getCamerView2() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent,CHOOSE_PHOTO2);
    }


    //普通按钮设置数值
    private void popSetvaules() {
        final EditText et = new EditText(this);
        et.setText(values[change.getId()]);
        new AlertDialog.Builder(this).setTitle("FIND")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "不可以为空" + input, Toast.LENGTH_LONG).show();
                        }else{
                            values[change.getId()]=input;
                            pop_small.dismiss();
                        }
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    //罗盘按钮设置数值
    private void popSetvaules2() {

        View popset_circle=getLayoutInflater().inflate(R.layout.cirle_set_pop, null);
        final EditText ed1=(EditText)popset_circle.findViewById(R.id.ed1);
        final EditText ed2=(EditText)popset_circle.findViewById(R.id.ed2);
        final EditText ed3=(EditText)popset_circle.findViewById(R.id.ed3);
        final EditText ed4=(EditText)popset_circle.findViewById(R.id.ed4);
        luopan_left= String.valueOf(values[change.getId()].charAt(0));
        luopan_up= String.valueOf(values[change.getId()].charAt(1));
        luopan_right= String.valueOf(values[change.getId()].charAt(2));
        luopan_down= String.valueOf(values[change.getId()].charAt(3));
        ed1.setText(luopan_left);
        ed2.setText(luopan_up);
        ed3.setText(luopan_right);
        ed4.setText(luopan_down);

        new AlertDialog.Builder(this).setTitle("设置")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(popset_circle)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     //   input =//et.getText().toString();
                        luopan_left=ed1.getText().toString();
                        luopan_up=ed2.getText().toString();
                        luopan_right=ed3.getText().toString();
                        luopan_down=ed4.getText().toString();
                        luopan=luopan_left+luopan_up+luopan_right+luopan_down;
                        if (luopan_left.equals("")||luopan_up.equals("")||luopan_right.equals("")||luopan_down.equals("")) {
                            Toast.makeText(getApplicationContext(), "不可以为空"  , Toast.LENGTH_LONG).show();
                        }else{
                            pop_small.dismiss();
                            values[change.getId()]=luopan;
                        }
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }
    //wheelview设置数值
    private void popSetvaules3() {
        View popset_wheel=getLayoutInflater().inflate(R.layout.wheel_set_pop, null);
        final EditText ed1=(EditText)popset_wheel.findViewById(R.id.ed1);
        final EditText ed2=(EditText)popset_wheel.findViewById(R.id.ed2);
        wheel_right= String.valueOf(values[change.getId()].charAt(1));
        wheel_left= String.valueOf(values[change.getId()].charAt(0));
        ed1.setText(wheel_left);
        ed2.setText(wheel_right);

        new AlertDialog.Builder(this).setTitle("设置")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(popset_wheel)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        wheel_left=ed1.getText().toString();
                        wheel_right=ed2.getText().toString();
                        if ( wheel_left.equals("")|| wheel_right.equals("")  ) {
                            Toast.makeText(getApplicationContext(), "不可以为空"  , Toast.LENGTH_LONG).show();
                        }else{
                            values[change.getId()]=wheel_left+wheel_right;
                            pop_small.dismiss();
                        }
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    //创建新按钮
    private void setnewone() {
        newone.setId(++id_pri);
        values[newone.getId()]="Z";
        rl.addView(newone,100,100);
        newone.setOnTouchListener(this);
      //  newone.setOnClickListener(new MyListener());
        newone.setOnLongClickListener(this);
        pri_scaleF=1.0F;
        atf_scaleF=1.0F;
    }

    private void setminenewone(){
    minenewone.setId(++id_pri);
    values[minenewone.getId()]="Z";
    rl.addView(minenewone,100,100);
    minenewone.setOnLongClickListener(new Longclickmine());
    minenewone.setOnTouchListener(this);
    pri_scaleF=1.0F;
    atf_scaleF=1.0F;
    }

    @Override
    public boolean onLongClick(final View view) {
        pop_change.setVisibility(View.VISIBLE);
        if(tb.isChecked()&&mode!=2){
             pop_small=new PopupWindow(popset_small, ViewGroup.LayoutParams.WRAP_CONTENT,
                     ViewGroup.LayoutParams.WRAP_CONTENT);
             pop_small.setOutsideTouchable(true);
             pop_small.setBackgroundDrawable(new BitmapDrawable());
             pop_small.setFocusable(true);
             pop_small.showAsDropDown(view,view.getWidth(),-view.getHeight()/2);
             change=view;
         }
        return true;
    }

    class Longclickmine implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            if(tb.isChecked()&&mode!=2){
                pop_small=new PopupWindow(popset_small, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                pop_small.setOutsideTouchable(true);
                pop_small.setBackgroundDrawable(new BitmapDrawable());
                pop_small.setFocusable(true);
                pop_small.showAsDropDown(view,view.getWidth(),-view.getHeight()/2);
                change=view;
                pop_change.setVisibility(View.INVISIBLE);
            }
            return true;
        }
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(tb.isChecked()){
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();//获取手机开机时间
                if (mHits[mHits.length - 1] - mHits[0] < 300) {
                    /**双击的业务逻辑*/
                    rl.removeView(v);
                }
            }
        }
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {

        // TODO Auto-generated method stub
        int action=event.getAction();
        if(tb.isChecked()){//可修改时
            switch(action & MotionEvent.ACTION_MASK ){
                case  MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                    mode2=99;
                    mode=1;
                    break;
                case MotionEvent.ACTION_UP:
                    mode2=0;
                    break;
                case MotionEvent.ACTION_POINTER_1_UP://第二个触摸点离开(不分顺序的)，action_po.._p好像也是不分
                    mode -= 1;                       // int pointerCount = event.getPointerCount();也是计数而不是确定是那个
                    mode2=0;
                    oldDist=0;

                    break;
                case MotionEvent.ACTION_POINTER_1_DOWN:
                    mode += 1;
                    mode2=0;
                    oldDist=0;
                    break;

                case MotionEvent.ACTION_MOVE:
                    int dx =(int)event.getRawX() - lastX;
                    int dy =(int)event.getRawY() - lastY;
                    newDist=0;

                    if (mode==2) {
                         newDist = spacing(event);
                        //放大
                        if (newDist > oldDist + 5&&oldDist!=0) {

                            //过于灵敏导致出错
                            //  jilu=newDist / oldDist;
                            //    atf_scaleF=jilu;
                            //    System.out.print(jilu+"hehehe333333333333333333hehehehhe"+atf_scaleF+'\n');
                            //    suofang(v);
                            //    oldDist = newDist;
                            //  pri_scaleF=atf_scaleF;

                            for(int i=0;i<10;i++){
                               atf_scaleF=atf_scaleF+jilu;
                                 suofang(v);
                                pri_scaleF=atf_scaleF;
                            }
                             big++;
                        }

                        //缩小
                       else
                         if(newDist < oldDist - 5) {
                        //     suoxiao=vClassifier( event.getX(0),event.getX(1))*jilu;
                            for(int i=0;i<1;i++){
                                atf_scaleF=atf_scaleF-3*jilu;
                                suofang(v);
                                pri_scaleF=atf_scaleF;
                            }
                            small++;
                     }
                        oldDist = newDist;
                    }
//大概因为这两句话不是同时的，所以才会出现判断错误，涉及到了线程之类额把，不然怎么会这么灵敏
                    else if(mode2==99){
                        left = v.getLeft() + dx;
                        top = v.getTop() + dy;
                        right = v.getRight() + dx;
                        bottom = v.getBottom() + dy;
                        if(left < 0){
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if(right > screenWidth){
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if(top < 0){
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if(bottom > screenHeight){
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        ViewGroup.MarginLayoutParams lp =(ViewGroup.MarginLayoutParams)v.getLayoutParams();
                        lp.leftMargin =left;
                        lp.topMargin = top ;
                        v.setLayoutParams(lp);

                       // Log.e("HAHA",left+"      "+top+"       "+right+"   "+bottom);
                    }

                    break;
            }
        }else{
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    if(v.getClass().equals(circle.class)||v.getClass().equals(wheelview.class)){
                        break;
                    }
                    //   sendMessage(values[v.getId()]);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(v.getClass().equals(circle.class)){
                        circle c=(circle)v;
                      //  System.out.println(c.getDu()+"!!!!!!!!!");
                        //按照一二三四象限排序
                        if(c.getDu()>0&&c.getDu()<90 ){
                           // System.out.println("*************************");
                        }else if(c.getDu()>90&&c.getDu()<180){
                           // System.out.println("--------------------------");
                        }else if(c.getDu()>-180&&c.getDu()<-90){
                           // System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
                        }
                        else if(c.getDu()>-90&&c.getDu()<0){
                           //// System.out.println("########################");
                        }
                    }else if(v.getClass().equals(wheelview.class)) {
                        wheelview w=(wheelview) v;
                        System.out.println(w.getValues()+"!!!!!!!!!");
                        if(w.getValues()>0 ){
                          //  System.out.println("*************************");
                         }else{
                         //   System.out.println("--------------------------");
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                      //     sendMessage("Z");
                    break;
            }
        }
          return false;
    }

    class Zi extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            set = new PopupWindow(popset, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            set.setOutsideTouchable(true);
           // set.setHeight(1);逼格好高的样子
            set.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            set.setWidth(60);
            set.setBackgroundDrawable(new BitmapDrawable());
            set.setFocusable(true);
             set.setAnimationStyle(R.style.AnimationLeftFade);
            set.showAtLocation(tb, Gravity.LEFT,   -300,0);
            return super.onDoubleTap(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consume = gd.onTouchEvent(event);
        return consume;
    }

    // /弹窗参数设置
    private void setSet(){
        set1.setOutsideTouchable(true);
        set1.setBackgroundDrawable(new BitmapDrawable());
        set1.setFocusable(true);
       set1.setAnimationStyle(R.style.AnimationLeftFade);
    }

    public Drawable toturn(Drawable iimg){
        Bitmap img=drawableToBitmap(iimg);
        Matrix matrix = new Matrix();
        matrix.postRotate(90); /*翻转90度*/
        int width = img.getWidth();
        int height =img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        iimg=new BitmapDrawable(img);
        return iimg;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
         Bitmap bitmap = Bitmap.createBitmap(
              drawable.getIntrinsicWidth(),
               drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                 : Bitmap.Config.RGB_565);
          Canvas canvas = new Canvas(bitmap);
         drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
           drawable.draw(canvas);
         return bitmap;
    }

    private float spacing(MotionEvent event) {
        //获得直线距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        System.out.println(event.getX(0)+">"+event.getX(1) +"！！！！！！！！！！！！！！！！"+event.getY(0)+">"+event.getY(1));
        return (float) Math.sqrt(x * x + y * y);
    }

    void suofang(final  View vv){

        ObjectAnimator anim = ObjectAnimator//
                                .ofFloat(vv, "zhy", pri_scaleF,atf_scaleF)//一开始的大小和后来的大小，哇哈哈哈哈哈哈哈
                                 .setDuration(50);
                        anim.start();

                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation)
                            {
                                float cVal = (Float) animation.getAnimatedValue();
                                vv.setAlpha(cVal);
                                vv.setScaleX(cVal);
                                vv.setScaleY(cVal);
                            }
                        });
    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            if(photo==null){
////                System.out.println("photono!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////                return true;
////            }
////            Intent home = new Intent(Intent.ACTION_MAIN);
////            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            home.addCategory(Intent.CATEGORY_HOME);
////            startActivity(home);
////            return true;
//        return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    //图片模糊，不要。
static Drawable zoomDrawable(Drawable drawable, int w, int h)
{
    int width = drawable.getIntrinsicWidth();
    int height= drawable.getIntrinsicHeight();
    Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
    Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象
    float scaleWidth = ((float)w / width);   // 计算缩放比例
    float scaleHeight = ((float)h / height);
    matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
    Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
    return new BitmapDrawable(newbmp);       // 把 bitmap 转换成 drawable 并返回
}

}
