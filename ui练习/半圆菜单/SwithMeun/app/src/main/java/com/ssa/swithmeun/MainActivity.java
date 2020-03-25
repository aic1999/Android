package com.ssa.swithmeun;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.ssa.swithmeun.util.AnimationUtils;

public class MainActivity extends Activity implements OnClickListener{

    private RelativeLayout rl_level1;
    private RelativeLayout rl_level2;
    private RelativeLayout rl_level3;
    boolean isLevel3Display = true;
    boolean isLevel2Display = true;
    boolean isLevel1Display = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        initViews();
    }

    private void initViews() {
        // 添加点击事件
        findViewById(R.id.ib_home).setOnClickListener(this);
        findViewById(R.id.ib_menu).setOnClickListener(this);

        rl_level1 = (RelativeLayout) findViewById(R.id.rl_level1);
        rl_level2 = (RelativeLayout) findViewById(R.id.rl_level2);
        rl_level3 = (RelativeLayout) findViewById(R.id.rl_level3);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // keyCode 事件码
        System.out.println("onKeyDown: " + keyCode);
        if(keyCode == KeyEvent.KEYCODE_MENU){

            if(AnimationUtils.runningAnimationCount > 0){
                // 当前有动画正在执行, 取消当前事件
                return true;
            }

//			如果按下的是菜单按钮
            if(isLevel1Display){
                long delay = 0;
                // 隐藏三级菜单
                if(isLevel3Display){
                    AnimationUtils.rotateOutAnim(rl_level3, 0);
                    isLevel3Display = false;
                    delay += 200;
                }

                // 隐藏二级菜单
                if(isLevel2Display){
                    AnimationUtils.rotateOutAnim(rl_level2, delay);
                    isLevel2Display = false;
                    delay += 200;
                }

                // 隐藏一级菜单
                AnimationUtils.rotateOutAnim(rl_level1, delay);

            }else {
                // 顺次转进来
                AnimationUtils.rotateInAnim(rl_level1, 0);
                AnimationUtils.rotateInAnim(rl_level2, 200);
                AnimationUtils.rotateInAnim(rl_level3, 400);

                isLevel3Display = true;
                isLevel2Display = true;
            }
            isLevel1Display = !isLevel1Display;

            return true;// 消费了当前事件
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if(AnimationUtils.runningAnimationCount > 0){
            // 当前有动画正在执行, 取消当前事件
            return;
        }
        switch (v.getId()) {
            case R.id.ib_home:

                if(isLevel2Display){
                    long delay = 0;
                    // 如果当前三级菜单已经显示, 先转出去
                    if(isLevel3Display){
                        AnimationUtils.rotateOutAnim(rl_level3, 0);
                        isLevel3Display = false;
                        delay += 200;
                    }

                    // 如果当前二级菜单已经显示, 转出去
                    AnimationUtils.rotateOutAnim(rl_level2, delay);
                }else {
                    // 如果当前二级菜单没有显示, 转出来
                    AnimationUtils.rotateInAnim(rl_level2, 0);
                }
                // 置反
                isLevel2Display = !isLevel2Display;
                break;
            case R.id.ib_menu:
                if(isLevel3Display){
                    // 如果当前三级菜单已经显示, 转出去
                    AnimationUtils.rotateOutAnim(rl_level3, 0);
                }else {
                    // 如果当前三级菜单没有显示, 转出来
                    AnimationUtils.rotateInAnim(rl_level3, 0);
                }
                // 置反
                isLevel3Display = !isLevel3Display;
                break;

            default:
                break;
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

}
