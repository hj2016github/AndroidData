package com.soyikeji.work.work.activity;
/**
 * 通知公告的Activity
 */

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.soyikeji.work.R;
import com.soyikeji.work.work.fragment.ReadedFragment;
import com.soyikeji.work.work.fragment.ReadingFragment;

public class NoticeyActivityframe extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llinfocontainer;
    private ImageView imreturn;//返回键
    private RadioButton bt_readed, bt_reading;
    private android.app.FragmentManager mManager;
    private String userid;

    // ReadedFragment redf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticey_activityframe);
        initview();
        initData();//初始化数据；
        setListen();
        mManager = getFragmentManager();
        int id = getIntent().getIntExtra("id", 1);//默认是1==已读;


       /* if (id==1) {
            ReadedFragment  red12f = new ReadedFragment();
        }else if (d==2){
            ReadingFragment  redf = new ReadingFragment();
        }else if (iid==3){
            ReadedFragment  redf = new ReadedFragment();
        }*/
       //TODO代码冗余需要封装;
        if (id == 1) {//默认是已读的处理==直接打开是通知公告的处理;
            ReadedFragment red12f = new ReadedFragment();
            FragmentTransaction transation = mManager.beginTransaction();
            transation.add(R.id.ll_infocontainer, red12f);
            transation.commit();
        }
        if (id == 2) {//主页面点击未读的处理;
            bt_reading.setBackgroundResource(R.mipmap.quanbu01);
            bt_readed.setBackgroundResource(R.mipmap.quanbu);
            ReadingFragment redf = new ReadingFragment();
            FragmentTransaction transation = mManager.beginTransaction();
            transation.replace(R.id.ll_infocontainer, redf);
            transation.commit();
            // bt_reading.performClick();
        }
        if (id == 3) {//主页面点击是已读的处理;
            ReadedFragment redf = new ReadedFragment();
            FragmentTransaction transation = mManager.beginTransaction();
            transation.add(R.id.ll_infocontainer, redf);
            transation.commit();

        }
    }

    private void initData() {
        userid = getIntent().getStringExtra("userid");
    }

    private void setListen() {
        bt_readed.setOnClickListener(this);
        bt_reading.setOnClickListener(this);
        imreturn.setOnClickListener(this);


    }

    private void initview() {
        this.llinfocontainer = (LinearLayout) findViewById(R.id.ll_infocontainer);//小横线指示;

        this.bt_readed = (RadioButton) findViewById(R.id.bt_readed);
        this.bt_reading = (RadioButton) findViewById(R.id.bt_reading);
        this.imreturn = (ImageView) findViewById(R.id.im_return);

      /*  mManager = getFragmentManager();
        FragmentTransaction transation = mManager.beginTransaction();
        ReadedFragment  redf = new ReadedFragment();
        transation.replace(R.id.ll_infocontainer,redf);
        transation.commit();*/

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transation = mManager.beginTransaction();
        switch (v.getId()) {
            case R.id.im_return:
                finish();
                break;
            case R.id.bt_readed:

                bt_readed.setBackgroundResource(R.mipmap.quanbu01);
                bt_reading.setBackgroundResource(R.mipmap.quanbu);
                Bundle bundle = new Bundle();
                //以下切换传值;
                bundle.putString("userid", userid);
                ReadedFragment redf = new ReadedFragment();
                redf.setArguments(bundle);
                transation.replace(R.id.ll_infocontainer, redf);
                transation.commit();

                break;

            case R.id.bt_reading:
                bt_reading.setBackgroundResource(R.mipmap.quanbu01);
                bt_readed.setBackgroundResource(R.mipmap.quanbu);
                Bundle bundle1 = new Bundle();
                bundle1.putString("userid", userid);
                ReadingFragment ringf = new ReadingFragment();
                ringf.setArguments(bundle1);
                transation.replace(R.id.ll_infocontainer, ringf);
                transation.commit();
                break;
        }

    }
}