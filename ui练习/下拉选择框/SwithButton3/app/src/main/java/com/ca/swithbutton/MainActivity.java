package com.ca.swithbutton;


import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener{

    private ListView listView;
    private EditText et_input;
    private ArrayList<String> datas;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.ib_dropdown).setOnClickListener(this);

        et_input = (EditText) findViewById(R.id.et_input);
    }

    @Override
    public void onClick(View v) {
        showPopupWindow();
    }

    private void showPopupWindow() {
        initListView();

        // 显示下拉选择框
        popupWindow = new PopupWindow(listView, et_input.getWidth(), 300);

        // 设置点击外部区域, 自动隐藏
        popupWindow.setOutsideTouchable(true); // 外部可触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件

        popupWindow.setFocusable(true); //设置可获取焦点

        // 显示在指定控件下
        popupWindow.showAsDropDown(et_input, 0, -5);
    }

    // 初始化要显示的内容
    private void initListView() {
        listView = new ListView(this);
        listView.setDividerHeight(0);
        listView.setBackgroundResource(R.drawable.listview_background);
        listView.setOnItemClickListener(this);

        datas = new ArrayList<String>();
        // 创建一些数据
        for (int i = 0; i < 30; i++) {
            datas.add((10000 + i) + "");
        }

        listView.setAdapter(new MyAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        System.out.println("onItemClick: " + position);
        String string = datas.get(position);
        et_input.setText(string); // 设置文本

        popupWindow.dismiss(); // 消失了
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView == null){
                view = View.inflate(parent.getContext(), R.layout.item_number, null);
            }else {
                view = convertView;
            }

            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            tv_number.setText(datas.get(position));

            view.findViewById(R.id.ib_delete).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    notifyDataSetChanged();

                    if(datas.size() == 0){
                        // 如果删除的是最后一条, 隐藏popupwindow
                        popupWindow.dismiss();
                    }
                }
            });
            return view;
        }

    }



}

