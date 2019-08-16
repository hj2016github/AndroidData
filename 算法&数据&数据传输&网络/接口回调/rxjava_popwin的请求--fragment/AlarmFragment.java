package com.gasplatform.ygsj.mashgasmonitoring.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gasplatform.ygsj.mashgasmonitoring.R;
import com.gasplatform.ygsj.mashgasmonitoring.adapter.ItemLvAlarmAdapter;
import com.gasplatform.ygsj.mashgasmonitoring.entity.Alarm;
import com.gasplatform.ygsj.mashgasmonitoring.utils.GetURL;
import com.gasplatform.ygsj.mashgasmonitoring.utils.GsonUtils;
import com.gasplatform.ygsj.mashgasmonitoring.utils.OkhttoUtils;
import com.gasplatform.ygsj.mashgasmonitoring.utils.Singleton;
import com.gasplatform.ygsj.mashgasmonitoring.widget.AlarmPopWindow;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

public class AlarmFragment extends Fragment implements View.OnClickListener {

    private TextView tvHead;
    private ImageView imgGoback;

    private LinearLayout llAlmHead;
    private TextView tvSerialNo;
    private TextView tvAlmAlarmType;
    private TextView tvAlmAlarmInfo;
    private TextView tvAlmStartTime;
    private TextView tvChart;
    private ListView lvRt;
    private ImageView img_more;
    private List<Alarm> alarmList;
    private AlarmPopWindow alarmPopWindow;

     Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    alarmList = (List<Alarm>) msg.obj;
                    if (alarmList!= null && alarmList.size()> 0){
                        ItemLvAlarmAdapter alarmAdapter = new ItemLvAlarmAdapter(getActivity(),alarmList);
                        lvRt.setAdapter(alarmAdapter);
                    }

                    break;
                case 1:
                    alarmList = (List<Alarm>) msg.obj;
                    if (alarmList!= null && alarmList.size()> 0){
                        ItemLvAlarmAdapter alarmAdapter = new ItemLvAlarmAdapter(getActivity(),alarmList);
                        lvRt.setAdapter(alarmAdapter);
                        alarmAdapter.notifyDataSetChanged();
                    }
            }
            super.handleMessage(msg);
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ObtainAlarmValue();
        return inflater.inflate(R.layout.fragmet_alarm, null);
    }

    private void ObtainAlarmValue() {
        OkhttoUtils.get_getJson(GetURL.getAllAlarm(), new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e("json", "onFailure: "+e.toString() );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                  final String json =   response.body().string();
                Log.e("json", "success: "+json.toString() );
                if (null!=json && !"".equals(json)&&(!json.contains("请求错误"))){
                    alarmList = GsonUtils.jsonToList(json, Alarm.class);
                    Message message = Message.obtain();
                    message.what = 0 ;
                    message.obj = alarmList;
                    handler.sendMessage(message);
                }
            }
        });

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        img_more = view.findViewById(R.id.imgBtn_more);
        img_more.setOnClickListener(this);

        imgGoback = (ImageView) view.findViewById(R.id.img_goback);
        imgGoback.setOnClickListener(this);


        tvHead = (TextView) view.findViewById(R.id.tv_head);
        llAlmHead = (LinearLayout) view.findViewById(R.id.ll_alm_head);
        tvSerialNo = (TextView) view.findViewById(R.id.tv_serialNo);
        tvAlmAlarmType = (TextView) view.findViewById(R.id.tv_alm_alarmType);
        tvAlmAlarmInfo = (TextView) view.findViewById(R.id.tv_alm_alarmInfo);
        tvAlmStartTime = (TextView) view.findViewById(R.id.tv_alm_startTime);
        tvChart = (TextView) view.findViewById(R.id.tv_chart);
        lvRt = (ListView) view.findViewById(R.id.lv_rt);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_more:
                alarmPopWindow = Singleton.getAlarmPopWindowInstance(getActivity());
                alarmPopWindow.showPopupWindow(img_more);
                break;
            case  R.id.img_goback:
                getActivity().finish();
                break;
        }
    }

    //rxjava的进行popwindow与fragment之间进行传输;
        public  Action1<List> action1 = new Action1<List>(){
            @Override
            public void call(List list) {
                    Message message = Message.obtain();
                    message.obj  = list;
                    message.what = 1;
                    handler.sendMessage(message);
            }
        };
}
