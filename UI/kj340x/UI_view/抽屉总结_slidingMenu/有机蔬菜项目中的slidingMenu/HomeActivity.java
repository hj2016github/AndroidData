package com.example.administrator.organic_greens.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.organic_greens.R;
import com.example.administrator.organic_greens.adapter.FragmentAdapter;
import com.example.administrator.organic_greens.adapter.ListAdapter;
import com.example.administrator.organic_greens.fragment.ClassifyFragment;
import com.example.administrator.organic_greens.fragment.HomeFragment;
import com.example.administrator.organic_greens.fragment.NearbyFragment;
import com.example.administrator.organic_greens.fragment.ProductFragment;
import com.example.administrator.organic_greens.utils.SharedUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
*initMenu():menu的相关指标初始化
* initListenerlist() 初始化menu中的listview的item,进行相关页面的跳转; initAdapter(),给listview设置adpter
*initDate() 229 初始化menu的数据; onClick有相关menu的点击事件;slidingMenu.toggle();//slidingMenu的弹出;175行出现侧滑菜单;
* */
public class HomeActivity extends BaseBroadActivity implements View.OnClickListener{//BaseBroadActivity继承了fragmentActivity也可以继承App包下的activity;
    private ViewPager homePager;
    private List<Fragment> fragList;
    private FragmentAdapter adapter;
    private ListAdapter menuAdapter;
    private LinearLayout homeLayout,homeBackgroud;
    private ImageView homeProduct,homeHouse,homeClassify,homeNearby;
    private List<ImageView> imgerList;
    private List<TextView> textList;
    private List<LinearLayout> layoutList;
    private List<Map<String,Object>> menuList;
    private ImageButton homeImage,homeImager;
    private SlidingMenu slidingMenu;
    private ListView menuView;
    private ImageView menuImage;
    private TextView menuText,menuCancel;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initMenu();//有slidingmenu;
        initHead();
        initDate();
        initAdapter();
        initListener();
    }

    private void initHead() {
        username = getIntent().getStringExtra("username");
        if(username ==null){
           menuText.setText("登录/注册");
           menuText.setEnabled(true);
           menuCancel.setVisibility(View.VISIBLE);
        }else {
            menuText.setEnabled(false);
            menuText.setText("欢迎"+username+"登录");
            menuCancel.setVisibility(View.GONE);
        }
    }

    private void initMenu() {//slidingMenu的设置;
        slidingMenu = new SlidingMenu(getApplicationContext());
        slidingMenu.setMode(SlidingMenu.RIGHT);//设置位置;
        slidingMenu.attachToActivity(this,SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setSecondaryMenu(R.layout.home_slidingmenu);//初始化view;
        menuView= (ListView) slidingMenu.findViewById(R.id.menu_list);//slidingMenu下的listview;
        menuImage= (ImageView) slidingMenu.findViewById(R.id.menu_image_head);
        menuText= (TextView) slidingMenu.findViewById(R.id.menu_text);
        menuCancel= (TextView) slidingMenu.findViewById(R.id.menu_cancel);
        //slidingMenu的各种登录与注销;
        menuText.setOnClickListener(this);
        menuCancel.setOnClickListener(this);
        menuImage.setOnClickListener(this);
        homeImage.setOnClickListener(this);
        initListenerlist();
        slidingMenu.setBehindOffset(150); //SlidingMenu划出时主页面显示的剩余宽度
        slidingMenu.setFadeDegree(0.35f);//setFadeDegree
        slidingMenu.addIgnoredView(homePager);//解决与viewpager的冲突;
    }

    private void initListenerlist(){//初始化listview的item,进行相关页面的跳转;
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(HomeActivity.this,Gonggao.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this,Gerenzhongxin.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,Erweima.class));
                        break;
                    case 3:
                        String fist = SharedUtils.getShared(getApplicationContext());
                        if(fist.equals("1")){
                            Toast.makeText(getApplicationContext(),"您还没有登录,请登录",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.setAction("android.intent.action.activity.Login");
                            startActivity(intent);
                        }
                        if(fist.equals("2")){
                            startActivity(new Intent(HomeActivity.this,Shoucang.class));
                        }
                        break;
                    case 4:
                        String fists = SharedUtils.getShared(getApplicationContext());
                        if(fists.equals("1")){
                            Toast.makeText(getApplicationContext(),"您还没有登录,请登录",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.setAction("android.intent.action.activity.Login");
                            startActivity(intent);
                        }
                        if(fists.equals("2")) {
                            startActivity(new Intent(HomeActivity.this, Gouwuche.class));
                        }
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this,Shezhi.class));
                        break;
                }
            }
        });
    }

    private void initListener() {
        homePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i <fragList.size() ; i++) {
                    imgerList.get(i).setEnabled(true);
                    textList.get(i).setBackgroundColor(Color.GRAY);
                    layoutList.get(i).setBackgroundColor(Color.WHITE);
                }
                imgerList.get(position).setEnabled(false);
                textList.get(position).setBackgroundColor(Color.BLACK);
                layoutList.get(position).setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homeImager.setOnClickListener(new View.OnClickListener() {//点击人头的弹出Menu;
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();//slidingMenu的弹出;
            }
        });
        homeImage.setOnClickListener(new View.OnClickListener() {//搜索按钮；
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,ActivitySearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initAdapter() {
        adapter=new FragmentAdapter(getSupportFragmentManager());
        adapter.setList(fragList);
        homePager.setAdapter(adapter);
        //抽屉的内容
        menuAdapter=new ListAdapter(menuList,getApplicationContext());
        menuView.setAdapter(menuAdapter);
    }

    private void initDate() {
        fragList=new ArrayList<>();
        fragList.add(new HomeFragment());
        fragList.add(new ProductFragment());
        fragList.add(new ClassifyFragment());
        fragList.add(new NearbyFragment());
        imgerList=new ArrayList<>();
        imgerList.add(homeHouse);
        imgerList.add(homeProduct);
        imgerList.add(homeClassify);
        imgerList.add(homeNearby);
        textList=new ArrayList<>();
        layoutList=new ArrayList<>();
        for (int i = 0; i <fragList.size() ; i++) {
            TextView homeText= (TextView) homeLayout.getChildAt(i);
            LinearLayout layout=(LinearLayout) homeBackgroud.getChildAt(i);
            layoutList.add(layout);
            layout.setBackgroundColor(Color.WHITE);
            homeText.setBackgroundColor(Color.GRAY);
            textList.add(homeText);
            imgerList.get(i).setEnabled(true);
            imgerList.get(i).setTag(i);
            imgerList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePager.setCurrentItem((Integer) v.getTag());
                }
            });
        }
        imgerList.get(0).setEnabled(false);
        textList.get(0).setBackgroundColor(Color.GREEN);
        layoutList.get(0).setBackgroundColor(Color.GREEN);

        //抽屉的数据
        menuList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("image",R.mipmap.ymhnotice);
        map.put("content","公告");
        menuList.add(map);

        Map<String,Object> map1=new HashMap<>();
        map1.put("image",R.mipmap.ymhchangename);
        map1.put("content","个人中心");
        menuList.add(map1);

        Map<String,Object> map2=new HashMap<>();
        map2.put("image",R.mipmap.ymhqrcode);
        map2.put("content","二维码");
        menuList.add(map2);

        Map<String,Object> map3=new HashMap<>();
        map3.put("image",R.mipmap.ymhopinionratinggray);
        map3.put("content","收藏");
        menuList.add(map3);

        Map<String,Object> map4=new HashMap<>();
        map4.put("image",R.mipmap.ymhshopingcar);
        map4.put("content","购物车");
        menuList.add(map4);

        Map<String,Object> map5=new HashMap<>();
        map5.put("image",R.mipmap.ymhset);
        map5.put("content","设置");
        menuList.add(map5);

    }

    private void initView() {
        homeBackgroud= (LinearLayout) findViewById(R.id.home_backgroud);
        homePager= (ViewPager) findViewById(R.id.home_viewpager);
        homeImage= (ImageButton) findViewById(R.id.home_image);//放大镜
        homeImager= (ImageButton) findViewById(R.id.home_image1);//人头
        homeLayout= (LinearLayout) findViewById(R.id.home_layout);
        homeHouse= (ImageView) findViewById(R.id.home_image_house);//主页
        homeProduct= (ImageView) findViewById(R.id.home_image_product);//产品
        homeClassify= (ImageView) findViewById(R.id.home_image_classify);//分类
        homeNearby= (ImageView) findViewById(R.id.home_image_nearby);//附近
    }
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 将系统当前的时间赋值给exitTime
            exitTime = System.currentTimeMillis();
        } else {
            exitApp();
        }
    }
    private void exitApp() {
        Intent intent = new Intent();
        intent.setAction("net.loonggg.exitapp");
        this.sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_text:
                startActivity(new Intent(HomeActivity.this,Login.class));
                break;
            case R.id.menu_cancel:
                SharedUtils.setShared(getApplicationContext(),"Fist",1+"");
                menuText.setText("登录/注册");
                menuText.setEnabled(true);
                menuCancel.setVisibility(View.VISIBLE);
                break;
            case R.id.menu_image_head:
                break;
        }
    }
}
