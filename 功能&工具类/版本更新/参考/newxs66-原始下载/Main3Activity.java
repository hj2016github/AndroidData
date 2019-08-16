package com.sangfor.newxs66;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * 获取网络ip，判断是否内网
 */

public class Main3Activity extends Activity {
    private LinearLayout lay01;

    /**
     * 检查是否连接到了网络?
     */
    public boolean checkWifi() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        if (wifi | internet) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有网没网后，通知后给出提示
     */
    public void checkFinish() {
        if (!checkWifi()) {
            lay01.setVisibility(View.VISIBLE);
            // 创建builder
            AlertDialog.Builder builders = new AlertDialog.Builder(
                    Main3Activity.this);
            builders.setTitle("抱歉，网络连接失败，是否进行网络设置");
            builders.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 进入无线网络配置界面
                            Main3Activity.this.startActivity(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS));
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            startActivity(new
                                    Intent(Settings.ACTION_WIFI_SETTINGS));
                            // //进入手机中的wifi网络设置界面
                            finish();
                        }
                    });
            builders.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // 关闭当前activity
                            Main3Activity.this.finish();
                        }
                    });
            builders.setCancelable(false);
            builders.show();

        }
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main3);
        lay01 = (LinearLayout) findViewById(R.id.lay01);
        checkFinish();
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        Log.i("rrr", ip);
        if (ip.substring(0, 3).equals("172")) {
            Intent intent = new Intent(Main3Activity.this, IntranetActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Main3Activity.this, VpnActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}

