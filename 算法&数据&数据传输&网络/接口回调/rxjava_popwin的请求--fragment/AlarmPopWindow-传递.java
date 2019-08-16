package com.gasplatform.ygsj.mashgasmonitoring.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gasplatform.ygsj.mashgasmonitoring.R;
import com.gasplatform.ygsj.mashgasmonitoring.entity.Alarm;
import com.gasplatform.ygsj.mashgasmonitoring.entity.RealValue;
import com.gasplatform.ygsj.mashgasmonitoring.fragment.AlarmFragment;
import com.gasplatform.ygsj.mashgasmonitoring.utils.GetURL;
import com.gasplatform.ygsj.mashgasmonitoring.utils.GsonUtils;
import com.gasplatform.ygsj.mashgasmonitoring.utils.OkhttoUtils;
import com.gasplatform.ygsj.mashgasmonitoring.utils.Singleton;

import java.io.IOException;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;

import static com.gasplatform.ygsj.mashgasmonitoring.fragment.AlarmFragment.*;

/*import cn.rongcloud.im.ui.activity.SearchFriendActivity;
import cn.rongcloud.im.ui.activity.SelectFriendsActivity;*/


public class AlarmPopWindow extends PopupWindow {

    private RadioButton btn_breakline;

    private RadioButton btnOutage1;
    private RadioButton btn_alarm;
    private RadioButton btn_abnormalfeed;
    private List<Alarm> alarms  ;
    @SuppressLint("InflateParams") //跳过检查
    public AlarmPopWindow(final Activity context) {//realname是为了传递参数使用;
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.pop_alarm, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int)context.getResources().getDimension(R.dimen.popwindow_alarm_width));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) context.getResources().getDimension(R.dimen.popwindow_alarm_height));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置点击屏幕其它地方弹出框消失
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作;
        //解决点击外部而popwindo无法消失的问题;
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        //以下设置点击事件
        final RadioGroup radioGroup = view.findViewById(R.id.pop_alarm_rg);
        btn_breakline = view.findViewById(R.id.btn_breakline);
        btnOutage1 = view.findViewById(R.id.btn_outage);
        btn_alarm = view.findViewById(R.id.btn_alarm);
        btn_abnormalfeed = view.findViewById(R.id.btn_abnormalfeed);
        //TODO去掉最后一次的状态,否则上来就选择,没法点击选择;
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.btn_breakline:
                        btn_breakline.setTextColor(context.getResources().getColor(R.color.white));
                        btn_abnormalfeed.setTextColor(context.getResources().getColor(R.color.hintColor));
                        btnOutage1.setTextColor(context.getResources().getColor(R.color.hintColor));
                        btn_alarm.setTextColor(context.getResources().getColor(R.color.hintColor));
                        alarmRequest("3");//断线
                        dismiss();
                        break;
                    case R.id.btn_outage:
                        btnOutage1.setTextColor(context.getResources().getColor(R.color.white));
                        btn_abnormalfeed.setTextColor(context.getResources().getColor(R.color.hintColor));
                        btn_breakline.setTextColor(context.getResources().getColor(R.color.hintColor));
                        btn_alarm.setTextColor(context.getResources().getColor(R.color.hintColor));
                        alarmRequest("2");//断电
                        dismiss();
                    break;


                     case R.id.btn_alarm:
                         btn_alarm.setTextColor(context.getResources().getColor(R.color.white));
                         btn_abnormalfeed.setTextColor(context.getResources().getColor(R.color.hintColor));
                         btn_breakline.setTextColor(context.getResources().getColor(R.color.hintColor));
                         btnOutage1.setTextColor(context.getResources().getColor(R.color.hintColor));
                         alarmRequest("1");//报警
                         dismiss();
                    break;
                     case R.id.btn_abnormalfeed://交流断电 = 馈电异常?
                         btn_abnormalfeed.setTextColor(context.getResources().getColor(R.color.white));
                         btnOutage1.setTextColor(context.getResources().getColor(R.color.hintColor));
                         btn_breakline.setTextColor(context.getResources().getColor(R.color.hintColor));
                         btn_alarm.setTextColor(context.getResources().getColor(R.color.hintColor));
                         alarmRequest("4");
                         dismiss();
                    break;
                }
            }
        });

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);

        } else {
            this.dismiss();
        }
    }

        private void alarmRequest(String param){
            OkhttoUtils.get_getJson(GetURL.getSelectAlarm(param), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Log.e("popalarm", "onResponse: "+json );
                    if (null!=json && !"".equals(json)&&(!json.contains("请求错误")))//判空;
                         alarms= GsonUtils.jsonToList(json,Alarm.class);
                         Observable.just(alarms).subscribe(Singleton.getAlarmFragmentInstance().action1);//使用rxjava进行请求数据传输到fragment进行处理;
                }
            });
        }
}
