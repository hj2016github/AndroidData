package com.ygsj.ui.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;
import com.ygsj.R;

import java.util.ArrayList;

/**
 * aviloader承载在一个弹出的dialog上,需要对其进行缩放/关闭等工作;
 */

public class Loader {
    private static final int LOADER_SIZE_SCALE = 8;//缩放比例;
    private static final int LOADER_OFFSET_SCALE = 10;//偏移量;

    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
    private static final String DEFAULT_LOADER = LoaderStyle.BallSpinFadeLoaderIndicator.name();//默认样式;

    public static void showLoading(Context context, Enum<LoaderStyle> type) {//传入枚举类;
        showLoading(context, type.name());
    }

    public static void showLoading(Context context, String type) {

        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);//在style中创建;

        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        dialog.setContentView(avLoadingIndicatorView);//在dialog上加AV;

        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();

        final Window dialogWindow = dialog.getWindow();

        if (dialogWindow != null) {
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;//加载圈是全屏的1/8
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;//1/8加偏移量;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);//可以统一关闭;
        dialog.show();
    }

    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }

    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.cancel();//cancel()会有回调,而dissmiss()没有回调;
                }
            }
        }
    }

}
