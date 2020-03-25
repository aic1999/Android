package com.lmm.comwell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lmm.comwell.adapter.ListViewAdapter;
import com.lmm.comwell.adapter.PhotoAdapter;
import com.lmm.comwell.bean.Photo;
import com.lmm.comwell.bean.Post;
import com.lmm.comwell.bean.User;
import android.app.*;

//得到商品图片的app
public class PhotoActivity extends AppCompatActivity
{
    private static int RESULT_LOAD_IMAGE = 1;
    private String path;
    private User user;
    private List<Photo> list;
    ProgressDialog dialog =null;
    private String[] images;
    private ListViewAdapter adapter;
    private ListView mlist;
    private ImageView imageView,down_img;
    private List<String> list_url=new ArrayList<>();
    private SwipeRefreshLayout refresh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layout);
        setTitle("个人相册");
        mlist=(ListView)findViewById(R.id.photo_list) ;
        imageView=(ImageView)findViewById(R.id.my_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = new User();
        user=BmobUser.getCurrentUser(User.class);
        list=new ArrayList<>();
        getUrl();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUrl();
                adapter=new ListViewAdapter(images,PhotoActivity.this);
                mlist.setAdapter(adapter);
                adapter.notifyDataSetInvalidated();
            }
        },1000*4);


        refresh=(SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override            public void onRefresh() {
                Glide.get(PhotoActivity.this).clearMemory();
                adapter.notifyDataSetInvalidated();
                refresh.setRefreshing(false);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                Intent intent = new Intent();
//                intent.setClass(EditActivity.this,MainActivity.class);
//                startActivity(intent);
                PhotoActivity.this.finish();
                break;
            case R.id.addimg:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addimg, menu);

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);
            cursor.close();
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog = new ProgressDialog(PhotoActivity.this);
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setTitle("上传中...");
                    dialog.setIndeterminate(false);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    final BmobFile file = new BmobFile(new File(path));
                    file.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException p1) {
                            final Photo photo = new Photo();
                            photo.setPhoto(file);
                            photo.setUser(user);
                            photo.save(new SaveListener<String>() {
                                @Override
                                public void done(String p1, BmobException p2) {
                                    // TODO: Implement this method
                                    if (p2 == null) {
                                        toast("成功上传图片"+user.getUsername()+file.getUrl());
                                        String img_url=file.getUrl();
                                        photo.setImg_url(img_url);
                                        photo.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e==null){
                                                    toast("上传url成功");
                                                    Intent intent=new Intent(PhotoActivity.this,PerActivity.class);
                                                    startActivity(intent);
                                                }else{
                                                    toast("上传url失败");
                                                }
                                            }
                                        });
                                        dialog.dismiss();
                                    } else {
                                        toast(p2.toString() + "失败");
                                        dialog.dismiss();
                                    }
                                }

                            });
                            // TODO: Implement this method
                        }
                        @Override
                        public void onProgress (Integer value){
                            // 返回的上传进度（百分比）
                            dialog.setProgress(value);
                        }

                    });
                }
            },1000*2);

        }

    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }
    public void getUrl(){

        BmobQuery<Photo> bq = new BmobQuery<Photo>();
        bq.include("img_url");
        bq.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                if (e==null) {
                    toast("加载成功"+list.size()+"");
                    refresh.setRefreshing(false);
                    for (int i=0;i<list.size();i++) {
                        images=new String[list.size()];
                        for (Photo p : list) {
                            images[i] = p.getImg_url();
                            i++;
                        }
                    }
                }else{
                    refresh.setRefreshing(false);
                    toast("加载失败"+e.toString() );

                }
            }
        });


}}



