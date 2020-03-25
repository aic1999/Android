package com.lmm.comwell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lmm.comwell.adapter.MesAdapter;
import com.lmm.comwell.bean.Message;
import com.lmm.comwell.bean.Post;
import com.lmm.comwell.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class Fra_Mes extends Fragment {
    private ListView listView;
    private MesAdapter adapter;
    private Context context;
    private ArrayList<Message> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_msg,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView=(ListView)getView().findViewById(R.id.mes_lv);
        list=new ArrayList<>();

        adapter=new MesAdapter(list,getActivity());
        listView.setAdapter(adapter);
        adapter.notifyDataSetInvalidated();
        listView.setDividerHeight(5);
              getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),MesActivity.class);
                startActivity(intent);
            }
        });
       /* Message message = new Message();
        message.setTitle("Title");
        message.setContent("content_android:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_heightandroid:layout_height");
        message.setTime("2018-10-20");
        message.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("成功");
            }

            @Override
            public void onFailure(int i, String s) {
             toast(s);
            }
        });*/
    }
    public void getData(){
        Message message = new Message();
        BmobQuery<Message> bq = new BmobQuery<Message>();
        bq.setLimit(100);
        bq.order("-updatedAt");
        bq.include("title,content,time");
        bq.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e==null){

                    for(Message p: list){
                        p.getContent();
                        p.getTitle();
                        p.getTime();
                        list.add(p);
                        adapter.notifyDataSetInvalidated();
                    }
                }else {
                    //toast(e.toString());
                }
            }
        });

    }
    public void toast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }
}
