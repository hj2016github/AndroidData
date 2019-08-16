package com.joanzapata.iconify.sample;

import android.app.Application;
import com.joanzapata.iconify.Iconify;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        for (Font font : Font.values())//获取所有的value;
            Iconify.with(font.getFont());
    }
}
