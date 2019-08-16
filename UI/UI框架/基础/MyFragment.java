package com.tyyh.android06_fragmentviewpager_day31;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 设置4个fragment太麻烦,直接用一个代替四个;
 */
public class MyFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment,container,false);
        TextView textView= (TextView) view.findViewById(R.id.tv_title_fragment);
        Bundle bundle=getArguments();
        if(bundle!=null){
            int index= (int) bundle.get("key");
            switch (index){
                case 1:
                    textView.setText("这是微信界面");
                    break;
                case 2:
                    textView.setText("这是通讯录界面");
                    break;
                case 3:
                    textView.setText("这是发现界面");
                    break;
                case 4:
                    textView.setText("这是我爱你界面");
                    break;
            }
        }
        return view;
    }
}
