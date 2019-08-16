package com.gasplatform.ygsj.mashgasmonitoring.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;

import com.gasplatform.ygsj.mashgasmonitoring.R;

public class AlarmFragment extends Fragment  {

    private LinearLayout llAlmHead;
    private TextView tvSerialNo;
    private TextView tvAlmAlarmType;
    private TextView tvAlmAlarmInfo;
    private TextView tvAlmStartTime;
    private TextView tvChart;
    private ListView lvRt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmet_alarm, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llAlmHead = (LinearLayout) view.findViewById(R.id.ll_alm_head);
        tvSerialNo = (TextView) view.findViewById(R.id.tv_serialNo);
        tvAlmAlarmType = (TextView) view.findViewById(R.id.tv_alm_alarmType);
        tvAlmAlarmInfo = (TextView) view.findViewById(R.id.tv_alm_alarmInfo);
        tvAlmStartTime = (TextView) view.findViewById(R.id.tv_alm_startTime);
        tvChart = (TextView) view.findViewById(R.id.tv_chart);
        lvRt = (ListView) view.findViewById(R.id.lv_rt);
    }

}
