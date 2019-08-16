package com.gasplatform.ygsj.mashgasmonitoring.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gasplatform.ygsj.mashgasmonitoring.R;
import com.gasplatform.ygsj.mashgasmonitoring.adapter.mViewpagerAdapter;
import com.gasplatform.ygsj.mashgasmonitoring.utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private android.widget.ImageButton imgBtnmore;
    private android.widget.TextView tvhead;
    private android.support.v4.view.ViewPager viewpagermain;
    private android.widget.ImageButton imgbtntabrealtime;
    private android.widget.TextView tvtabrealtime;
    private android.widget.ImageButton imgbtntabalarm;
    private android.widget.TextView tvtabalarm;
    private android.widget.ImageButton imgbtntabhistory;
    private android.widget.TextView tvtabhistory;
    ConstraintLayout cl_tabs;
    private List<Fragment> fragmentList;
    mViewpagerAdapter mViewpagerAdapter;
    List<ImageButton> imageButtons;
    List<TextView> textViews;
    private ImageButton imageButton;
    private ImageView imggoback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        initIndicator();//给imgbtn的list添加btn,text的list添加tv;
        initTab();//初始化3个tab;
        scrollViewpager();//适配并滑动viewpager;


    }

    private void initIndicator() {//constraintlayout无法直接getchildAt(i)进行强转,故先添加list;
        imageButtons = new ArrayList<>();
        textViews = new ArrayList<>();
        imageButtons.add(imgbtntabrealtime);imageButtons.add(imgbtntabalarm);
        imageButtons.add(imgbtntabhistory);
        textViews.add(tvtabrealtime);textViews.add(tvtabalarm);textViews.add(tvtabhistory);

    }


    private void scrollViewpager() {//适配并滑动viewpager;
        mViewpagerAdapter = new mViewpagerAdapter(getSupportFragmentManager());
        mViewpagerAdapter.setList(gatherFragments());
        viewpagermain.setAdapter(mViewpagerAdapter);
        viewpagermain.setOffscreenPageLimit(2);//对viewpager的fragment进行缓存;
        viewpagermain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {//滑动tab的处理
                for (int i = 0; i < 3; i++) {
                    imageButtons.get(i).setEnabled(true);
                    textViews.get(i).setTextColor(getResources().getColor(R.color.white));
                }
                imageButtons.get(position).setEnabled(false);
                textViews.get(position).setTextColor(getResources().getColor(R.color.blue));

                changeHead(position); //标题头的变化;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTab() {

        for (int i = 0; i < 3; i++) {
            textViews.get(i).setTextColor(getResources().getColor(R.color.white));
            imageButtons.get(i).setEnabled(true);
            final ImageButton imageButton = imageButtons.get(i);//不能提出来变成类变量,内部类调用有容易起问题;
            final TextView textView = textViews.get(i);
            imageButton.setTag(i);
            textView.setTag(i);
            imageButton.setOnClickListener(new View.OnClickListener() {//点击tab的处理;
                @Override
                public void onClick(View v) {
                    changeHead((Integer) imageButton.getTag());//点击时候变化标题头文字;
                    imageButton.setEnabled(false);
                    viewpagermain.setCurrentItem((Integer) imageButton.getTag());//点击的时候移动到相应的fragment上;
                    textViews.get((Integer) imageButton.getTag()).setTextColor(getResources().getColor(R.color.blue));

                }
            });

            //因为不能区域性点击不能同时点击(不能把imgBtn跟tv同时点击),所以就对tv也进行了处理;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeHead((Integer) textView.getTag());//点击时候变化标题头文字;
                    textView.setTextColor(getResources().getColor(R.color.blue));
                    viewpagermain.setCurrentItem((Integer) textView.getTag());
                    imageButtons.get((Integer) textView.getTag()).setEnabled(false);
                }
            });
        }
        textViews.get(0).setTextColor(getResources().getColor(R.color.blue));
        imageButtons.get(0).setEnabled(false);
        changeHead(0);

    }

    private List<Fragment> gatherFragments() {//拿到fragment的集合,供viewpager使用;
        fragmentList = new ArrayList<>();
        fragmentList.add(Singleton.getRealtimeFragmentInstance());
        fragmentList.add(Singleton.getAlarmFragmentInstance());
        fragmentList.add(Singleton.getHistoryFragmentInstance());
        return fragmentList;
    }

    private void initview() {
        this.tvtabhistory = (TextView) findViewById(R.id.tv_tab_history);
        this.imgbtntabhistory = (ImageButton) findViewById(R.id.imgbtn_tab_history);
        this.tvtabalarm = (TextView) findViewById(R.id.tv_tab_alarm);
        this.imgbtntabalarm = (ImageButton) findViewById(R.id.imgbtn_tab_alarm);
        this.tvtabrealtime = (TextView) findViewById(R.id.tv_tab_realtime);
        this.imgbtntabrealtime = (ImageButton) findViewById(R.id.imgbtn_tab_realtime);
        this.viewpagermain = (ViewPager) findViewById(R.id.viewpager_main);
        this.tvhead = (TextView) findViewById(R.id.tv_head);
        this.imgBtnmore = (ImageButton) findViewById(R.id.imgBtn_more);
        cl_tabs = (ConstraintLayout) findViewById(R.id.cl_tabs);
        this.imggoback = (ImageView) findViewById(R.id.img_goback);
        imggoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void changeHead(int position) {//变换标题头文字
        switch (position){
            case 0 :
                tvhead.setText(getResources().getText(R.string.realtimeData_head));
                break;
            case 1 :
                tvhead.setText(getResources().getText(R.string.alarm_head));
                break;
            case 2:
                tvhead.setText(getResources().getText(R.string.history_head));
                break;
            default:
                tvhead.setText("数据展示-全部");
                break;
        }
    }


    /**
     * 连续两次点击后退出程序
     */
    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000){
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                }else {
                    finishAffinity();//关闭当前activity所属的activity栈中所有的activity
                }
                break;
        }
        return true;
    }


}
