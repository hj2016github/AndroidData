package com.tyyh.android06_fragmentviewpager_day31;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {//用AppCompActivity其实也一样;
    private LinearLayout titleLayout,linesLayout;
    private ViewPager viewPager;
    private List<Fragment> list;
    private MyAdapter adapter;
    private List<TextView> listTitle;
    private List<TextView> listLines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//1初始化控件;
        initData();//2给fragment的list添加fragment;
        initTitleData();//3处理tab的指示;
//4,调用:要求传入FragmentManager,因为是v4包的所以是getSupportFragmentManager()
        //先得到适配器;
        adapter=new MyAdapter(getSupportFragmentManager());
        adapter.setList(list);//适配器传入fragment的list;
        viewPager.setAdapter(adapter);//viewpager设置适配器,这时就可以滑动了;
        //以下替换了setOnPageChangeListener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<list.size();i++){//所有的text变成可点击;
                    listTitle.get(i).setEnabled(true);
                    listLines.get(i).setBackgroundColor(Color.GRAY);
                }
                listTitle.get(position).setEnabled(false);//当前不可点击;
                listLines.get(position).setBackgroundColor(Color.GREEN);//当前的变成指示变成绿色;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initTitleData() {//3,处理tab的指示;
        listTitle=new ArrayList<>();//tab文字的变化的集合,为了集中处理点击事件
        listLines=new ArrayList<>();//指示线的变化的集合,为了集中处理点击事件
        for(int i=0;i<list.size();i++){
            TextView textView= (TextView) titleLayout.getChildAt(i);//titleLayout为linealayout;子view为textview;
            textView.setEnabled(true);//可点击;
            listTitle.add(textView);
            TextView textViewlines= (TextView) linesLayout.getChildAt(i);
            textViewlines.setBackgroundColor(Color.GRAY);//初始时候设置为灰色;
            listLines.add(textViewlines);
            textView.setTag(i);//把i传给onClickListener传值使用;内部内的值无法直接传入;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(true);//可点击
                    viewPager.setCurrentItem((Integer) view.getTag());//viewpager移动到i
                }
            });

        }
        //单独初始化第一个tab
        listTitle.get(0).setEnabled(false);//不可点击;
        listLines.get(0).setBackgroundColor(Color.GREEN);//设置为绿色;

    }

    private void initData() {//2,给fragment的list添加fragment;
        //这里为了省事添加了同一个fragment,只不过有4个不同的对象,四个对象传不同的值,传给fragment:fragment用switch..case处理,代表不同的界面;
       //真正的项目要求有四个fragment,每个都是单例;
        //猜测是移动viwpager的时候在适配器中进行对fragment的相关操作并传值;
        list=new ArrayList<>();
        MyFragment fragment1=new MyFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("key",1);
        fragment1.setArguments(bundle);
        list.add(fragment1);

        MyFragment fragment2=new MyFragment();
        Bundle bundle2=new Bundle();
        bundle2.putInt("key",2);
        fragment2.setArguments(bundle2);
        list.add(fragment2);

        MyFragment fragment3=new MyFragment();
        Bundle bundle3=new Bundle();
        bundle3.putInt("key",3);
        fragment3.setArguments(bundle3);
        list.add(fragment3);

        MyFragment fragment4=new MyFragment();
        Bundle bundle4=new Bundle();
        bundle4.putInt("key",4);
        fragment4.setArguments(bundle4);
        list.add(fragment4);
    }

    private void initView() {//初始化main.xml的控件;
        titleLayout= (LinearLayout) findViewById(R.id.title);
        linesLayout= (LinearLayout) findViewById(R.id.lines);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
    }
}
