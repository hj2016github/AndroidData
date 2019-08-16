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

public class RealtimeFragment extends Fragment  {

    private LinearLayout llRtHead;
    private TextView tvSerialNo;
    private TextView tvRtName;
    private TextView tvRtLocation;
    private TextView tvRtValue;
    private TextView tvRtChart;
    private TextView textView;
    private ListView lvRt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_realtime, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llRtHead = (LinearLayout) view.findViewById(R.id.ll_rt_head);
        tvSerialNo = (TextView) view.findViewById(R.id.tv_serialNo);
        tvRtName = (TextView) view.findViewById(R.id.tv_rt_name);
        tvRtLocation = (TextView) view.findViewById(R.id.tv_rt_location);
        tvRtValue = (TextView) view.findViewById(R.id.tv_rt_value);
        tvRtChart = (TextView) view.findViewById(R.id.tv_rt_chart);
        textView = (TextView) view.findViewById(R.id.textView);
        lvRt = (ListView) view.findViewById(R.id.lv_rt);
    }

}
