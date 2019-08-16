package com.sangfor.newxs66;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

public class Main2Activity extends Activity {
    private ViewPager vp_pic3;//第一次安装3张图片
    private MyPageAdapter pageAdapter;//ViewPager适配器
    private ArrayList<View> list;//数据源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        vp_pic3 = (ViewPager) findViewById(R.id.vp_pic3);
        initData();//添加页面展示
        pageAdapter = new MyPageAdapter(list);//适配器启动
        vp_pic3.setAdapter(pageAdapter);//适配器开启
    }

    private void initData() {
        list = new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.view_01, null);
        list.add(view1);
        View view2 = LayoutInflater.from(this).inflate(R.layout.view_02, null);
        list.add(view2);
        View view3 = LayoutInflater.from(this).inflate(R.layout.view_03, null);
        list.add(view3);
           ImageView wang_iv_start= (ImageView) view3.findViewById(R.id.wang_iv_start);//得到第三张页面上的ImageView控件

        wang_iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转
                Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent);
                SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("start", 2);
                            editor.commit();
            }
        });
    }
}
