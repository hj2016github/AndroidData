package com.soyikeji.work.work.activity;

/***
 * Picasso的使用:在435行附近
 * public static void getPicFromPicasso(String url, ImageView imageView,Context context)
 * 这个方法应该再传一个参数,圆角的弧度,之后需要进行改造;
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.soyikeji.work.R;
import com.soyikeji.work.work.Chart.ChartBaseActivity;
import com.soyikeji.work.work.Chart.widget.MyChartview;
import com.soyikeji.work.work.JPush.JPushService;
import com.soyikeji.work.work.Utils.GetURL;
import com.soyikeji.work.work.Utils.OkhttoUtils;
import com.soyikeji.work.work.Utils.ParJson;
import com.soyikeji.work.work.Utils.PicassoUtils;
import com.soyikeji.work.work.app.MyApplication;
import com.soyikeji.work.work.entity.Announcements;
import com.soyikeji.work.work.entity.Login;
import com.soyikeji.work.work.ul.GlobalContsts;
import com.soyikeji.work.work.updata_apk.UpdateAppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePageActivity extends Activity implements OnClickListener {
    private ImageView imheadportrait, IMschedule, imageView3;
    private LinearLayout ll_notice, address_book, LL_My_conference, ll_exchange, Ll_The_query_on_duty, ll_schedule, Ll_Not_to_read_notice, Has_read_the_notice;
    private static final int RESULT = 1;
    Target target;
    private Button btn_picture, btn_photo, btn_cancle, Imquit, confirm, abolish;
    private Bitmap head;// 头像Bitmap
    @SuppressLint("SdCardPath")
    private static String path = "/sdcard/myHead/";// sd路径
    long startMillis;
    private Object dialog;
    private static String calanderURL = "";
    private static String calanderEventURL = "";
    private static String calanderRemiderURL = "";
    private PopupWindow popupWindow;
    private Login login;
    private View view;
    private TextView tv_name, tv_readed, tv_reading;
    private TextView tv_gonghao;
    private String realname, gonghao, imgurl, userid;
    private UpdateAppManager updateManager;// 自主更新判断
    private SharedPreferences sp;
    private MyChartview img_mychart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // 初始化
        setview();
        // 监听器
        setonck();

        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从Sd中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(toRoundBitmap(bt));// 转换成drawable
            imheadportrait.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        realname = getIntent().getStringExtra("realname");
        gonghao = getIntent().getStringExtra("gonghao");
        imgurl = "http://60.205.150.100/" + getIntent().getStringExtra("imgURL");
        userid = getIntent().getStringExtra("userid");
        getToken(userid, realname);
        getReadData();
        //更新
        updateManager = new UpdateAppManager(this);
        updateManager.checkUpdateInfo(null);
        attchService();//为了连接service；
        //setMyChartView();//对自定义控件的数值进行测试；

    }

    int num = 8;

    private void setMyChartView() {
        //初始化
        img_mychart.setNum(num);
        if (num > 0) {
            img_mychart.setMode(1);//显示右上角数字
        } else {
            img_mychart.setMode(0);//啥也不显示
        }
    }


    private void attchService() {
        //为了连接service；

        Intent intent1 = new Intent(HomePageActivity.this, JPushService.class);
        //为了保证再次能拿到值，写进去，跳转的时候再写一次；
        String userid = sp.getString("id", null);
        String realName = sp.getString("realname", null);
        String deptName = sp.getString("deptName", null);

        SharedPreferences.Editor editor2 = sp.edit();
        editor2.putString("id", userid);
        editor2.putString("realname", realName);
        editor2.putString("deptName", deptName);
        editor2.commit();
        startService(intent1);
    }


    private void getToken(final String id, final String realName) {//连接融云拿token；
        String url1 = "http://60.205.150.100/Service/GetServiceInfo.ashx?Action=GetRongCloudToken&content={'appid':'101','userid':'" + id + "','username':'" + realName + "'}";

        OkhttoUtils.get_getJson(url1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    //// TODO: 2016/11/11 完了需要判一下空防止程序崩了；
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject jsonobjcet = jsonArray.getJSONObject(0);
                    String rongJson = jsonobjcet.getString("content");
                    JSONObject jsonRongObject = new JSONObject(rongJson);
                    String token1 = jsonRongObject.getString("token");
                    Log.i("tokenxx", "onResponse: " + token1);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token", token1);
                    editor.commit();
                    connect(token1);

                    if (RongIM.getInstance() != null) {
                        //String imgurl = "http://60.205.150.100/" +picUrl;
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(id, realName, Uri.parse(imgurl)));
                    }
                    RongIM.getInstance().setMessageAttachedUserInfo(true);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */

    private void connect(final String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName
                (getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */


            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server
                 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    // TODO: 2016/11/14 这是一个定时炸弹，肯定能让程序崩溃；
                    // getToken(userid,realname);
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("onnnnnsu", "--onSuccess" + userid);

                    initRongMessage();//对未读消息数进行监听；

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);

                }
            });
        }

    }


    //对融云的未读消息进行监听；

    /**
     * 融云消息接收，及初始化
     */
    private void initRongMessage() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.DISCUSSION
        };
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
            }
        }, 500);

    }

    //控制消息的显示，消息数据的变化和隐藏
    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int i) {
            img_mychart.setNum(i);
            if (i >= 0) {
                if (i == 0) {
                    img_mychart.setMode(0);//啥也不显示

                    //桌面图标显示未读消息数；
                    try {
                        ShortcutBadger.applyCountOrThrow(getApplicationContext(), 0);
                    } catch (ShortcutBadgeException e) {
                        e.printStackTrace();
                    }
                } else if (i > 0 && i < 100) {
                    //  mUnreadNumView.setVisibility(View.VISIBLE);
                    //   mUnreadNumView.setText(i + "");

                    img_mychart.setMode(1);//显示右上角数字
                    //桌面图标显示未读消息数；
                    try {
                        ShortcutBadger.applyCountOrThrow(getApplicationContext(), i);
                    } catch (ShortcutBadgeException e) {
                        e.printStackTrace();
                    }

                } else {
                    //   mUnreadNumView.setVisibility(View.VISIBLE);
                    //   mUnreadNumView.setText("没有未读消息");
                    img_mychart.setMode(3);
                }
            }


        }
    };


    private void getReadData() {//拿已读未读的几条信息的状态；

        OkhttoUtils.get_getJson(GetURL.GetReadURL(userid, GlobalContsts.Readstatus_yes), new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_readed.setText("0");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final List<Announcements> list = ParJson.getAnnouncements(json);
                if (list != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() == 0) {
                                tv_readed.setText("0");
                            } else {
                                tv_readed.setText(list.size() + "");
                            }

                        }
                    });
                }
            }
        });

        OkhttoUtils.get_getJson(GetURL.GetReadURL(userid, GlobalContsts.Readstatus_no), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_reading.setText("0");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final List<Announcements> list = ParJson.getAnnouncements(json);
                if (list != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_reading.setText(list.size() + "");
                        }
                    });
                }
            }
        });


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getReadData();

    }

    /**
     * 初始化
     */
    private void setview() {
        LL_My_conference = (LinearLayout) findViewById(R.id.LL_My_conference);
        ll_exchange = (LinearLayout) findViewById(R.id.ll_exchange);
        address_book = (LinearLayout) findViewById(R.id.address_book);
        Ll_The_query_on_duty = (LinearLayout) findViewById(R.id.Ll_The_query_on_duty);
        Has_read_the_notice = (LinearLayout) findViewById(R.id.Has_read_the_notice);
        Ll_Not_to_read_notice = (LinearLayout) findViewById(R.id.Ll_Not_to_read_notice);
        ll_schedule = (LinearLayout) findViewById(R.id.ll_schedule);
        ll_notice = (LinearLayout) findViewById(R.id.ll_notice);
        Imquit = (Button) findViewById(R.id.Im_quit);

        imheadportrait = (ImageView) findViewById(R.id.im_head_portrait);

        tv_name = (TextView) findViewById(R.id.textView3);
        tv_gonghao = (TextView) findViewById(R.id.textView4);
        tv_readed = (TextView) findViewById(R.id.tv_readed);
        tv_reading = (TextView) findViewById(R.id.tv_reading);
        img_mychart = (MyChartview) findViewById(R.id.Im_schedule);

    }

    @Override
    protected void onStart() {
        super.onStart();
        tv_name.setText(realname);
        PicassoUtils.getPicFromPicasso(imgurl, imheadportrait, HomePageActivity.this);
        tv_gonghao.setText(gonghao);
    }


    /**
     * 监听器
     */
    private void setonck() {
        ll_exchange.setOnClickListener(this);
        LL_My_conference.setOnClickListener(this);
        ll_schedule.setOnClickListener(this);
        Ll_The_query_on_duty.setOnClickListener(this);
        Has_read_the_notice.setOnClickListener(this);
        Ll_Not_to_read_notice.setOnClickListener(this);
        ll_notice.setOnClickListener(this);
        Imquit.setOnClickListener(this);
        imheadportrait.setOnClickListener(this);

        address_book.setOnClickListener(this);
        // img_mychart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LL_My_conference:
                Intent conference = new Intent(HomePageActivity.this,
                        MyDocuments.class);
                startActivity(conference);
                break;
            case R.id.Ll_The_query_on_duty:
                Intent query = new Intent(HomePageActivity.this,
                        TheWQueryOnDutyActivity.class);
                startActivity(query);
                break;
            case R.id.ll_exchange: //跳转融云界面；
                Intent intent5 = new Intent(HomePageActivity.this, ChartBaseActivity.class);
                intent5.putExtra("userid", userid);
                intent5.putExtra("realname", realname);
                intent5.putExtra("imgUrl", imgurl);
                startActivity(intent5);

                break;

            /*case R.id.Im_schedule:
                num--;
                img_mychart.setNum(num);
                if(num > 0){
                    img_mychart.setMode(1);
                }else {
                    img_mychart.setMode(0);
                }


                break;*/
            case R.id.Has_read_the_notice:
                Intent intent = new Intent(HomePageActivity.this, NoticeyActivityframe.class);
                intent.putExtra("id", 1);
                intent.putExtra("userid", userid);
                startActivity(intent);
                break;
            case R.id.Ll_Not_to_read_notice:
                Intent Notto = new Intent(HomePageActivity.this, NoticeyActivityframe.class);
                Notto.putExtra("id", 2);
                Notto.putExtra("userid", userid);
                startActivity(Notto);
                break;
            case R.id.ll_notice:
                Intent notice1 = new Intent(HomePageActivity.this,
                        NoticeyActivityframe.class);
                notice1.putExtra("id", 3);
                notice1.putExtra("userid", userid);
                startActivity(notice1);
                break;
            case R.id.Im_quit:
             /* setonBackPressed();*/
                SharedPreferences sharedPreferences = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFristLogin", false);
                editor.putString("id", "null");
                editor.putString("realname", "null");
                editor.putString("deptName", "null");
                editor.commit();
                sharedPreferences.getBoolean("isFristLogin", true);
                sharedPreferences.getString("id", null);
                sharedPreferences.getString("realname", null);
                sharedPreferences.getString("deptName", null);


                finish();
                break;
            case R.id.im_head_portrait:
            /*    showDialog();*/
                break;
            case R.id.confirm:
                break;
            case R.id.ll_schedule:
                Intent schedule = new Intent(HomePageActivity.this, ScheduleActivity.class);
                startActivity(schedule);
                break;
            case R.id.address_book:
                Intent intent1 = new Intent(HomePageActivity.this, ContactActivity.class);
                startActivity(intent1);
                break;


        }
    }


    private void setonBackPressed() {
        if (popupWindow == null) {
            view = View.inflate(HomePageActivity.this, R.layout.exit_the_dialog, null);
            popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            Log.i("popupWindow", "new " + popupWindow.toString());
            int btnHeight = Imquit.getHeight();
            view.measure(0, 0);
            int viewHeight = view.getMeasuredHeight();
            int offY = btnHeight + viewHeight;
            popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

            abolish = (Button) view.findViewById(R.id.abolish);
            confirm = (Button) view.findViewById(R.id.confirm);
            confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent4 = new Intent(HomePageActivity.this, MainActivity.class);
                    startActivity(intent4);

                }
            });
            abolish.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                }
            });
        }
    }

    /**
     * 换头像的对话
     */
    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,
                null);
        final Dialog dialog = new Dialog(this,
                R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        btn_picture = (Button) window.findViewById(R.id.btn_picture);
        btn_photo = (Button) window.findViewById(R.id.btn_photo);
        btn_cancle = (Button) window.findViewById(R.id.btn_cancle);

        btn_picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        btn_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(Environment
                                .getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        btn_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        imheadportrait.setImageBitmap(toRoundBitmap(head));// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ;

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 把bitmap转成圆形
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        // 取最短边做边长
        if (width < height) {
            r = width;
        } else {
            r = height;
        }
        // 构建一个bitmap
        Bitmap backgroundBm = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        // new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        // 设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        // 且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, p);
        // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }

    /**
     * 连续两次点击后�??出程�?
     */
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                try {
//
//				if (webView.canGoBack()) {
//					webView.goBack();
//					hideCustomView();
        /*		} else {*/
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 5000) { // 如果两次按键时间间隔大于2秒，则不�??�??
                        Toast.makeText(this, "再按一次退出该系统", Toast.LENGTH_SHORT)
                                .show();
                        firstTime = secondTime;// 更新firstTime
                        return true;
                    } else { // 两次按键小于2秒时,提示
                     /*   System.exit(0);*/
                        finishAffinity();

                    }

                } catch (Exception e) {
                }

                break;
        }
        return true;
    }

}
