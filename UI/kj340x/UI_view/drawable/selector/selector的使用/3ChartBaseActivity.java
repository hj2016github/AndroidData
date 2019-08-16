package com.soyikeji.work.work.Chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soyikeji.work.R;
import com.soyikeji.work.work.Chart.ChartAdapter.fragmentAdapter;
import com.soyikeji.work.work.Chart.fragment.ChartContactlistFragment;
import com.soyikeji.work.work.Chart.fragment.GrouplistFragment;
import com.soyikeji.work.work.Chart.fragment.TaitieConversationlistFragment;
import com.soyikeji.work.work.Chart.widget.MorePopWindow;
import com.soyikeji.work.work.Utils.OkhttoUtils;
import com.soyikeji.work.work.Utils.Singleton;
import com.soyikeji.work.work.app.DemoContext;
import com.soyikeji.work.work.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/*
*
* */
//import io.rong.imkit.fragment.ConversationListFragment;

public class ChartBaseActivity extends AppCompatActivity {

    private android.support.v4.view.ViewPager vp_chartBasic;//viewpager
    private android.widget.LinearLayout ll_indicator;//上划线
    private android.widget.LinearLayout lv_conversationlist;//会话列表的bt
    private android.widget.LinearLayout lv_contactlist;//通讯录的bt
    private ChartContactlistFragment CCl_fragment;//通讯录的fragment；
    private ConversationListFragment conversationListFragment;//会话的fragmet
    //private ConversationFragment conversationFragment;//qdiao
    //private TaitieConversationlistFragment ttClFragment;
    private List<Fragment> list_fragment;
    private TextView tv_contact;
    private TextView tv_conservation;
    private ImageView imgbt_conversation;
    private ImageView imgbt_contact;
    private fragmentAdapter fragAdapter;
    private List<ImageView> listImg;//Image
    private List<TextView> listText;//底下的文字
    private List<TextView> textindactorList;//指示器textview；
    private static final String TAG = ConversationActivity.class.getSimpleName();
    private String token;
    private SharedPreferences sp;
    private String id;
    private String realName;
    private String imgUrl;
    private ImageView img_activity_chart_more;
    private TextView tv_grouplist;
    private Conversation.ConversationType discussion;
    private Fragment groupListFragment;
    private ImageView img_grouplist;
    private LinearLayout lv_grouplist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_base);
        initView();
        listenViewPage();

        id = getIntent().getStringExtra("userid");
        realName = getIntent().getStringExtra("realname");
        imgUrl = getIntent().getStringExtra("imgUrl");
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        token = sp.getString("token",null);
        // Intent intent = getIntent();

        //在后台重新连接融云的方法；
        //isReconnect(intent);
    }

    private void isReconnect(Intent intent) {
        String token = null;
        if (DemoContext.getInstance() != null) {
            token = sp.getString("token",null);

        }
        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {
                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
                    reconnect(token);
                } else {

                }

            }


        }

    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {


                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }



    private void listenViewPage() {
        vp_chartBasic.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3 ; i++) {//其他的设置为没有点击；
                    listImg.get(i).setEnabled(true);
                    listText.get(i).setTextColor(Color.GRAY);
                    textindactorList.get(i).setTextColor(Color.GRAY);
                }
                //当前页面设置为点击了；
                listImg.get(position).setEnabled(false);
                listText.get(position).setTextColor(Color.parseColor("#1EA5FE"));
                textindactorList.get(position).setTextColor(Color.parseColor("#1EA5FE"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initView() {
        this.lv_contactlist = (LinearLayout) findViewById(R.id.lv_contactlist);//通讯录
        lv_grouplist = (LinearLayout) findViewById(R.id.lv_grouplist);//群组;
        this.lv_conversationlist = (LinearLayout) findViewById(R.id.lv_conversationlist);//会话列表
        this.ll_indicator = (LinearLayout) findViewById(R.id.ll_indicator);//上划线
        this.vp_chartBasic = (ViewPager) findViewById(R.id.vp_chartBasic);//viewpager

        //最底下的通讯录三个字；
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        //最底下的会话两个字；
        tv_conservation = (TextView) findViewById(R.id.tv_conversation);
        //最底下群组两个字；
        tv_grouplist = (TextView) findViewById(R.id.tv_grouplist);


        //会话的imgbutton
        imgbt_conversation = (ImageView) findViewById(R.id.img_conversation);
        //通讯录的imgbutton；
        imgbt_contact = (ImageView) findViewById(R.id.img_contact);
        //群组聊天的
        img_grouplist = (ImageView) findViewById(R.id.img_grouplist);


        //点击加号弹出popwindow
        img_activity_chart_more = (ImageView) findViewById(R.id.img_chart_more);
        img_activity_chart_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MorePopWindow morePopWindow = new MorePopWindow(ChartBaseActivity.this,realName);
                morePopWindow.showPopupWindow(img_activity_chart_more);

            }
        });

        list_fragment = new ArrayList<>();

        conversationListFragment = (ConversationListFragment) initConversationList();
        //groupListFragment = new GrouplistFragment();
        //groupListFragment = Singleton.getGrouplistFragment();
        ConversationListFragment discussionFragment = (ConversationListFragment) initDissonList();

        CCl_fragment = new ChartContactlistFragment();//通讯录的frag


        list_fragment.add(conversationListFragment);
        //list_fragment.add(groupListFragment);
        list_fragment.add(discussionFragment);
        list_fragment.add(CCl_fragment);

        fragAdapter = new fragmentAdapter(getSupportFragmentManager());
        fragAdapter.setData(list_fragment);
        vp_chartBasic.setAdapter(fragAdapter);
        vp_chartBasic.setOffscreenPageLimit(3);//保存viewpager的缓存；
        initIndactor();//初始化下面的指示内容让viewpager可点击；
    }


    /**
     * 会话列表的fragment
     */
    private Fragment mConversationFragment = null;

    /**
     * 初始化会话列表
     * @return  会话列表
     */
    private Fragment  initConversationList(){
        if (mConversationFragment == null) {
            ConversationListFragment listFragment = ConversationListFragment.getInstance();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .build();
            listFragment.setUri(uri);
            return  listFragment;
        } else {
            return  mConversationFragment;
        }
    }


    /**
     * 会话列表的fragment
     */
    private Fragment mDiscusionFragment = null;

    /**
     * 初始化会话列表
     * @return  会话列表
     */
    private Fragment  initDissonList(){
        if (mConversationFragment == null) {
            ConversationListFragment listFragment = ConversationListFragment.getInstance();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
                    .build();
            listFragment.setUri(uri);
            return  listFragment;
        } else {
            return  mConversationFragment;
        }
    }




    private void initIndactor() {
        listImg = new ArrayList<>();//ImageButton
        listText =  new ArrayList<>();//底下的文字
        final List<LinearLayout> listLiner = new ArrayList<>();

        listImg.add(imgbt_conversation);
        listImg.add(img_grouplist);
        listImg.add(imgbt_contact);

        listText.add(tv_conservation);
        listText.add(tv_grouplist);
        listText.add(tv_contact);

        listLiner.add(lv_conversationlist);
        listLiner.add(lv_grouplist);
        listLiner.add(lv_contactlist);


        textindactorList = new ArrayList<>();//指示器textview；
        for (int i = 0; i < 3 ; i++) {
            TextView tv_indicator = (TextView) ll_indicator.getChildAt(i);
            textindactorList.add(tv_indicator);//为了后面设置初始的颜色用；
            tv_indicator.setTextColor(Color.WHITE);//指示下滑设置为白色；
            listImg.get(i).setEnabled(true);//按钮设置为可点击；
            listText.get(i).setTextColor(Color.GRAY);//文字设置为灰色；

            listLiner.get(i).setTag(i);//listnener到达不了for循环里面
            listLiner.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//就是点击的view
                    vp_chartBasic.setCurrentItem((Integer) v.getTag());//设置viewpager的点击事件；
                }
            });
        }
        textindactorList.get(0).setTextColor(Color.parseColor("#1EA5FE"));
        listImg.get(0).setEnabled(false);
        listText.get(0).setTextColor(Color.parseColor("#1EA5FE"));
    }


    public  void img_reture_chartBasic(View view){//返回退出；
        finish();
    }

}
