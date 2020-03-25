package com.lmm.comwell;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lmm.comwell.bean.Photo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class UserPhoto extends AppCompatActivity {
    private ViewPager viewpager;
    private String[] imgUrlArr;
    private List<ImageView> imgList;
    private Context context;
    private int num = 300;//起始位置300
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photo_layout);
        context=this;
        initView();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadImg();
            }
        },1000*3);

    }

    public void initView(){
        setTitle("个人相册");

        viewpager=(ViewPager) findViewById(R.id.mViewPager);
        BmobQuery<Photo> bq = new BmobQuery<Photo>();
        bq.include("img_url");
        bq.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                if (e==null) {
                    toast("加载成功");
                    for (int i=0;i<=list.size();i++) {
                    imgUrlArr=new String[list.size()];
                    for (Photo p : list) {

                        p.getImg_url();
                        imgUrlArr[i] = p.getImg_url();

                        }
                    }
                }else{
                    toast("加载失败"+e.toString() );

                }
            }
        });
//        imgUrlArr=new String[]{"http://fengshouguo.com/data/upload/mobile/special/s0/s0_05233785427198540.jpg",
//                "http://fengshouguo.com/data/upload/mobile/special/s0/s0_05233870008759730.jpg",
//                "http://fengshouguo.com/data/upload/mobile/special/s0/s0_05233785635319825.jpg",
//                "http://fengshouguo.com/data/upload/mobile/special/s0/s0_05233786821255365.jpg"};
    }
    public void loadImg(){
        imgList=new ArrayList<ImageView>();
        for(int i=0;i<imgUrlArr.length;i++){
            if (imgUrlArr[i].equals("")){

            }
            ImageView imageView=new ImageView(context);
            //Glide加载网络图片
            Glide.with(context)
                    .load(imgUrlArr[i])
                    .into(imageView);
            //设置imageview占满整个ViewPager
            imageView.setScaleType(ScaleType.FIT_XY);
            imgList.add(imageView);
        }
        viewpager.setAdapter(new MyAdapter());
        viewpager.setOnPageChangeListener(new MyPagerChangeListener());
        viewpager.setCurrentItem(300);
        mHandler.postDelayed(mRunnable, 2000*2);
    }
    public void toast(String msg) {
        Toast.makeText(UserPhoto.this, msg, Toast.LENGTH_SHORT).show();
    }
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        public void run() {
            // 每隔多长时间执行一次
            /**
             * mHandler.postDelayed(mRunnable, 1000*PhoneConstans.TIMEVALUE);
             */
            mHandler.postDelayed(mRunnable, 1000 * 3);
            num++;
            //viewHandler.sendEmptyMessage(num);
        }

    };

    //报错，不用。~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            viewpager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    class MyPagerChangeListener implements OnPageChangeListener{

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {

        }


    }


    class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return  arg0 == arg1;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager) container).addView((View) imgList.get(position % imgList.size()),
                        0);
            } catch (Exception e) {
            }

            return imgList.get(position % imgList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(list.get(position));
        }
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }


    }

}
