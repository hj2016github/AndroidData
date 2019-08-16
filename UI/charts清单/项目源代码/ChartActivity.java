package com.gasplatform.ygsj.mashgasmonitoring.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gasplatform.ygsj.mashgasmonitoring.R;
import com.gasplatform.ygsj.mashgasmonitoring.entity.RealValue;
import com.gasplatform.ygsj.mashgasmonitoring.utils.GetURL;
import com.gasplatform.ygsj.mashgasmonitoring.utils.OkhttpUtils;
import com.gasplatform.ygsj.mashgasmonitoring.utils.ParJson;
import com.gasplatform.ygsj.mashgasmonitoring.utils.Singleton;
import com.gasplatform.ygsj.mashgasmonitoring.utils.TimeUtils;
import com.gasplatform.ygsj.mashgasmonitoring.widget.chart.ChartUtils;
import com.gasplatform.ygsj.mashgasmonitoring.widget.chart.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.mob.MobSDK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ChartActivity extends AppCompatActivity {
    private int serialNo;
    private String SensorName;
    private String unit;
    private android.widget.TextView tvchartname;
    private android.widget.TextView tvuintval;
    private android.widget.ImageButton imgback;
    private com.github.mikephil.charting.charts.LineChart chart;
    private RealValue realValue;
    private List<Entry> values;
    private List<String> listRealtime = new ArrayList<>();
    private Map<String, Object> map_data;
    private LineDataSet.Mode mode;
    private TextView tv_worning;
    private ProgressBar pb_wait;
    String realtime;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                     tv_worning.setVisibility(View.GONE);

                      map_data  = (Map<String, Object>) msg.obj;
                      values = (List<Entry>) map_data.get("vals");
                      listRealtime = (List<String>) map_data.get("rts");
                    if (values.size()>0&&listRealtime.size()>0)
                     ChartUtils.notifyDataSetChanged(ChartActivity.this,chart, values, ChartUtils.dayValue,SensorName,listRealtime,
                            mode);

                     pb_wait.setVisibility(View.GONE);
                      if (listRealtime.size() > 2400)//listRealtime是无限扩张的集合,为了节省内存,10分钟后自动关闭动态显示;
                          finish();
                        break;
                case 1:
                    break;
            }
        }
    };
    private int orientation;
    private ImageView img_share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //以下横竖屏切换:
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_chart_horizontal);
        }else{
            setContentView(R.layout.activity_chart);
        }


        //TODO默认暂时定为0;
        //以下定直方图还是曲线图;
        serialNo = getIntent().getIntExtra("SerialNo",0);
        unit = getIntent().getStringExtra("unit");
        SensorName = getIntent().getStringExtra("SensorName");
        if (unit.equals("")){
            mode= LineDataSet.Mode.STEPPED;
        }else {
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER;
        }

        initView();
        //以下横竖屏切换;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            ChartUtils.initChart(this,chart,8);

        }
        else {
            ChartUtils.initChart(this,chart,4);

        }
        getData(); //实时刷新数据;
        //TODO 是否设置强制横屏?可能会引起掉线的情况;
    }

    //以下保持最后一次状态;
    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("values", (ArrayList<? extends Parcelable>) values);
        outState.putStringArrayList("realtimes", (ArrayList<String>) listRealtime);
    }*/

    private void initView() {
        this.chart = (LineChart) findViewById(R.id.chart);
        this.imgback = (ImageButton) findViewById(R.id.img_back);
        this.tvuintval = (TextView) findViewById(R.id.tv_uint_val);
        this.tvchartname = (TextView) findViewById(R.id.tv_chart_name);
        tv_worning = (TextView)  findViewById(R.id.tv_worning);
        pb_wait = findViewById(R.id.progressBar_wait);
        pb_wait.setVisibility(View.VISIBLE);
        img_share = findViewById(R.id.img_share);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                timer.cancel();
                //OkhttpUtils.cancelAllCall();
            }
        });
        tvuintval.setText(unit);
        tvchartname.setText(SensorName);
        img_share.setOnClickListener(new View.OnClickListener() {//分享;
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }


    //以下Okhttp请求实时数据;
    private RealValue getCurveData() {
       // Log.e("charurl","getCurveData: "+ GetURL.getAllRealTimeValue());
        OkhttpUtils.get_getJson(GetURL.getAllRealTimeValue(), new Callback() {//TODO需要改请求地址;
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO掉线后如何处理,是否关闭?
                       // Toast.makeText(ChartActivity.this, "网络掉线,请重新打开查看", Toast.LENGTH_SHORT).show();
                        tv_worning.setVisibility(View.VISIBLE);
                        pb_wait.setVisibility(View.GONE);
                        tv_worning.setText("网络掉线,正在尝试重新连接...");
                      //  finish();

                    }
                });
                //Log.e("break", "run: "+"网络问题" );
              //  Log.e("exception","ex"+e.toString());
              //  OkhttpUtils.cancelAllCall();
              //  call.cancel();
               // getData();//掉线后重新连接;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                if (null!=json && !"".equals(json)&&(!json.contains("请求错误"))&&(!json.contains("ConnectionRefused"))){

                    realValue = ParJson.test21Realvalue(json);//TODO需要改解析方法;
                   // Log.e("millsec", "run: "+TimeUtils.dateToMillSec(realValue.getMinuteSamplingTime()));
                }

            }
        });
        return  realValue;
    }

    //以下开定时器进行调用okhttp请求,对横竖屏,以及<x轴坐标数与>x轴的坐标数分情况进处理;
    //<x轴的坐标数就一直添加,>x轴的坐标数删除第一个,添加最后一个;保持y轴的list的数量为4/8(横屏)
    //x轴的坐标点position一直增加,对应把时间填入相应的list中,保持一一对应,发送到主线程进行进行format;
    private int position = 0;
    private Timer timer;						//定时刷新折线图
    private List<Entry> getData() {
            values = new ArrayList<>();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    realValue =   getCurveData();
                    if(realValue!=null){
                   // TimeUtils.CurveTimeFarmat_(realValue.getSamplingTime());//x轴数据;
                  if (orientation ==  Configuration.ORIENTATION_LANDSCAPE){
                      if (values.size() > 7){//横屏是x轴画8个点;
                          // if (realValue!=null  ){
                          values.remove(0);
                          values.add(new Entry(position,(float) realValue.getCurveValue()));
                           /*realtime = TimeUtils.chartTimeFarmat(realValue.getSamplingTime());
                           listRealtime.remove(0);
                           listRealtime.add(realtime);*/

                          // }
                      }else {//横屏0-7的处理;
                          // if (realValue != null){
                          values.add(new Entry(position,(float) realValue.getCurveValue()));
                           /* realtime = TimeUtils.chartTimeFarmat(realValue.getSamplingTime());
                            listRealtime.add(realtime);*/
                          // }
                      }
                  }else {//竖屏;
                          if (values.size() > 3){//竖屏x轴4个坐标;
                              values.remove(0);
                              values.add(new Entry(position,(float) realValue.getCurveValue()));
                          }else {//0-3的处理;
                              values.add(new Entry(position,(float) realValue.getCurveValue()));
                          }
                        }

                       if (realValue.getSamplingTime()!=null){//position与realtime一一对应;
                           realtime = TimeUtils.chartTimeFarmat(realValue.getSamplingTime());//横轴坐标的时间一直添加;
                           listRealtime.add(realtime);
                           position++ ;
                       }


                    //if (position > 3)
                    //Log.e("pos%4", "position:"+ position + " position%4: "+position%4 + "-->"+realValue.getSamplingTime() + " ,"+realValue.getCurveValue());
                   // Log.e("pos&time", "postiton: "+position+" time"+realValue.getSamplingTime()+"-->"+listRealtime.get(position) );



                    map_data = new HashMap<>();
                    map_data.put("rts",listRealtime);
                    map_data.put("vals",values);
                   // map_data.put("pos",position);
                    Message message =Message.obtain( );
                    message.what = 0;
                    message.obj = map_data;
                    handler.sendMessage(message);
                      }
                }
            },0,10000); //10s钟进行刷新,5秒请求会有掉线的情况;



        return values;
    }

     @Override
     protected void onDestroy() {
         super.onDestroy();
         //OkhttpUtils.cancelAllCall();
         timer.cancel();
     }

//一键分享;
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("share");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        //分享出去的字段
        //TODO进行图片分享处理;
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
       // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

}
