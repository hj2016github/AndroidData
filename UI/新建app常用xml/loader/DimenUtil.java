package com.ygsj.ui.loader;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.ygsj.app.Configurator;

import static com.ygsj.app.ConfigKeys.APPLICATION_CONTEXT;


public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Configurator.getInstance().getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Configurator.getInstance().getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
