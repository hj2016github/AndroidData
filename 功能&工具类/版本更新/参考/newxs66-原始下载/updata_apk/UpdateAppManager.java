package com.sangfor.newxs66.updata_apk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.sangfor.newxs66.Connector;
import com.sangfor.newxs66.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateAppManager implements MyDialog.Clic_ok, MyDialog.Clic_no {
    // 文件分隔符
    private static final String FILE_SEPARATOR = "/";
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment
            .getExternalStorageDirectory()
            + FILE_SEPARATOR
            + "autoupdate"
            + FILE_SEPARATOR;
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "autoupdate.apk";
    // 更新应用版本标记
    private static final int UPDARE_TOKEN = 0x29;
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 0x31;

    private Context context;
    private String message = "检测到本程序有新版本发布，建议您更新！";
    //version.json
//	private String spec = "http://192.168.6.197:8080/App/sooyie.apk";
//	private String versionInfo = "http://192.168.6.197:8080/App/version.json";
    private String spec;
    private String versionInfo;
    // private String spec =
    // "http://222.42.1.209:81/1Q2W3E4R5T6Y7U8I9O0P1Z2X3C4V5B/mt.hotalk.com:8080/release/hotalk1.9.17.0088.apk";
    // 下载应用的对话框
    private Dialog dialog;
    // 下载应用的进度条
    private ProgressBar progressBar;
    // 进度条的当前刻度值
    private int curProgress;
    // 用户是否取消下载
    private boolean isCancel;

    public UpdateAppManager(Context context) {
        this.context = context;
        spec = Connector.ApkUrl + Connector.ApkName;
        versionInfo = Connector.ApkUrl + "version.json";
        Log.i("www", versionInfo);

    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDARE_TOKEN:
                    progressBar.setProgress(curProgress);
                    break;

                case INSTALL_TOKEN:
                    installApp();
                    break;
            }
        }
    };

    /**
     * 获取当前应用的版本名
     *
     * @return
     * @throws Exception
     */
    private String getVersionName_Location() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        Log.w("www", packageManager + "");
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

        String version = packInfo.versionName;
        Log.i("www", "版本号:" + version);
        return version;
    }


    /**
     * 检测应用更新信息
     *
     * @throws Exception
     */
    Handler handler_v;
    //服务器上apk的version_name
    String version_intnet;
    String content;
    static final int CODE = 1;
    double version_name_location;
    double version_name_intnet;

    public void checkUpdateInfo(final ImageView iv) {
        // 操作发送网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtils();
                content = HttpUtils.sendGetClient(versionInfo);
                handler_v.sendEmptyMessage(UpdateAppManager.CODE);
            }
        }).start();

        handler_v = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//				if (msg.what==1)
                try {
                    //网络服务器的version_name
                    JSONObject jsonObj = new JSONObject(content);
                    version_intnet = (String) jsonObj.get("version_name");
                    version_name_intnet = Double.valueOf(version_intnet);
                    System.out.println("服务器的版本码:" + version_intnet);
                    Log.i("qqq", "服务器的版本码:" + version_intnet.toString());
                    //当前系统程序version_name
                    String Version = getVersionName_Location();
                    version_name_location = Double.valueOf(Version);
                    System.out.println("本机应用的版本码:" + Version);
                    Log.i("qqq", "本机应用的版本码:" + Version.toString());
                    //判断如果服务器版本码大于本地版本码，则重新安装
                    if (version_name_intnet > version_name_location) {

                        if (iv != null) {
                            iv.setVisibility(View.VISIBLE);
                        } else {
                            showNoticeDialog();
                        }
                    } else {
                        if (iv != null) {
                            iv.setVisibility(View.GONE);
                        }
//                        Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(context, "json异常", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        };


    }

    /**
     * 检查是否连接到了网络
     */
    public boolean checkWifi() {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
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
     * 显示提示更新对话框
     */
    MyDialog notice_dialog;

    private void showNoticeDialog() {
        //自定义通话框，通过接口调用，设置监听
        notice_dialog = new MyDialog(context,
                R.style.MyDialog);
        notice_dialog.setClic_ok(this);
        notice_dialog.setClic_no(this);
        notice_dialog.show();
        ;
        // new
        // AlertDialog.Builder(context).setTitle("软件版本更新").setMessage(message)
        // .setPositiveButton("下载", new OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // showDownloadDialog();
        // }
        // }).setNegativeButton("以后再说", new OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // }
        // }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    private void showDownloadDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.updata_progressbar,
                null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isCancel = true;
            }
        });
        dialog = builder.create();
        dialog.show();
        downloadApp();
    }

    /**
     * 下载新版本应用
     */
    private void downloadApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(spec);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    long fileLength = conn.getContentLength();
                    in = conn.getInputStream();
                    File filePath = new File(FILE_PATH);
                    if (!filePath.exists()) {
                        filePath.mkdir();
                    }
                    out = new FileOutputStream(new File(FILE_NAME));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while ((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
                        if (isCancel) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                        handler.sendEmptyMessage(UPDARE_TOKEN);
                        if (readedLength >= fileLength) {
                            dialog.dismiss();
                            // 下载完毕，通知安装
                            handler.sendEmptyMessage(INSTALL_TOKEN);

                            break;
                        }
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://"+ appFile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @Override
    public void onclick_no() {
        // TODO Auto-generated method stub
        notice_dialog.dismiss();
        showDownloadDialog();
    }

    @Override
    public void onclick_ok() {
        // TODO Auto-generated method stub
        notice_dialog.dismiss();
    }
}