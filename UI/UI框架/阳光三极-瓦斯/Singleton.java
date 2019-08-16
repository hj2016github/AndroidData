package com.gasplatform.ygsj.mashgasmonitoring.utils;


import com.gasplatform.ygsj.mashgasmonitoring.fragment.AlarmFragment;
import com.gasplatform.ygsj.mashgasmonitoring.fragment.HistoryFragment;
import com.gasplatform.ygsj.mashgasmonitoring.fragment.RealtimeFragment;

import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2016/8/3.
 */
public class Singleton {
    private static OkHttpClient client = null;
    private  static  RealtimeFragment realtimeFragment = null;
    private  static HistoryFragment historyFragment =null;
    private  static AlarmFragment alarmFragment = null;
    private Singleton() {
    }

    public static OkHttpClient getOkhttpInstance() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    public static RealtimeFragment getRealtimeFragmentInstance() {
        if (realtimeFragment == null) {
            realtimeFragment = new RealtimeFragment();
        }
        return  realtimeFragment;
    }

    public static  AlarmFragment getAlarmFragmentInstance() {
        if (alarmFragment == null) {
            alarmFragment = new AlarmFragment();
        }
        return alarmFragment;
    }

    public static  HistoryFragment getHistoryFragmentInstance() {
        if (historyFragment == null) {
            historyFragment = new HistoryFragment();
        }
        return  historyFragment;
    }




}
