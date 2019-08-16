package com.gasplatform.ygsj.mashgasmonitoring.activity.update;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


import com.gasplatform.ygsj.mashgasmonitoring.BuildConfig;
import com.gasplatform.ygsj.mashgasmonitoring.R;
import com.gasplatform.ygsj.mashgasmonitoring.download.DownLoadCallback;
import com.gasplatform.ygsj.mashgasmonitoring.download.DownloadEntity;
import com.gasplatform.ygsj.mashgasmonitoring.utils.GetURL;
import com.gasplatform.ygsj.mashgasmonitoring.utils.OkhttpUtils;
import com.gasplatform.ygsj.mashgasmonitoring.utils.ParJson;
import com.gasplatform.ygsj.mashgasmonitoring.utils.Singleton;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/8.
 */

public class UpdateAppManager implements DownloadDialog.Clic_download, DownloadDialog.Clic_cancel {
    private static final String TAG = "Data";
    private Activity mActivity;
    private ProgressBar progressBar;
    private String url_apk;//服务器apk的路径;
    private String url_version_json;//apk的json的路径;
    private DownloadEntity downloadEntity;
    private AlertDialog progressDialog;

    public UpdateAppManager(Activity activity) {
        this.mActivity = activity;
        url_apk = GetURL.Url_apk;
        url_version_json = GetURL.Url_apkVesionJson;
        downloadEntity = Singleton.getDownloadEntityInstance();
        if (!downloadEntity.isSaved()) downloadEntity.save();//保存过就不在进行保存;
    }

    /**
     * 检查是否使用wifi
     */
    public boolean checkWifi() {
        ConnectivityManager con = (ConnectivityManager) mActivity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (wifi) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取本地应用的版本
     *
     * @return
     */
    private String getVersionName_Location() {
        // 获取packagemanager的实例
        PackageManager packageManager = mActivity.getPackageManager();
        PackageInfo packInfo;
        String version = null;

        try {
            packInfo = packageManager.getPackageInfo(mActivity.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    public void checkUpdateInfo() {
        OkhttpUtils.get_getJson(url_version_json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.e(TAG,"check---"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                String versionService = ParJson.getAppVersion(json);
                String versionLocation = getVersionName_Location();
                if (!versionService.equals(versionLocation)) {
                    handler.sendEmptyMessage(UpdateAppManager.SHOWDIALOG);
                }
            }
        });
    }

    static final int SHOWDIALOG = 0;
    static final int UPDATEPROGRESS = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOWDIALOG:
                    showNoticeDialog();//显示提醒框;
                    break;
                case UPDATEPROGRESS:
                    int progress_ = (int) msg.obj;
                    progressBar.setProgress(progress_);
                    break;
            }

        }
    };


    /**
     * 显示提示更新对话框
     */
    DownloadDialog notice_dialog;
    private void showNoticeDialog() {
        //自定义通话框，通过接口调用，设置监听
        notice_dialog = new DownloadDialog(mActivity, R.style.Theme_AppCompat_Dialog);
        notice_dialog.setClic_download(this);//点击下载apk;
        notice_dialog.setClic_cancel(this);
        notice_dialog.show();
    }

    @Override
    public void onclick_download() {
        notice_dialog.dismiss();
        ifUseWifi();//检测wifi;
    }

    @Override
    public void onclick_cancel() {
        notice_dialog.dismiss();
    }



    /**
     * 检测应用更新信息
     */
    public void ifUseWifi() {
        if (!checkWifi()) {
            WifiDialog();//没有wifi使用流量的对话框
        } else {
            showDownloadPop();//直接显示prograsspop进行下载
        }
    }


    private void WifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("没有Wifi可连接");
        builder.setIcon(R.mipmap.logo);
        builder.setMessage("确定使用流量吗?");
        builder.setCancelable(true);//设置框外点击消失
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//点击确定使用流量下载;
                showDownloadPop();//显示prograsspop进行下载
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 显示下载进度对话框
     */
    private void showDownloadPop() {
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.pop_download, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_download);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        progressDialog =  builder.create();
        progressDialog.show();
        downloadApp();
    }

    /**
     * 下载新版本应用
     */
    private void downloadApp() {
        OkhttpUtils.asyncRequestDownLoadFile(url_apk, new DownLoadCallback() {
            @Override
            public void success(File file) {
                //Log.e(TAG, "success: " + file.getName() + ": " + file.length());
               // Log.e(TAG, "success: " + file.getAbsolutePath());
                if(LitePal.find(DownloadEntity.class,downloadEntity.getId()).isSuccess()){
                    doInstallAuthority(file);//下载成功则准备授权下载;
                    progressDialog.dismiss();
                }

            }

            @Override
            public void fail(int errorCode, String errorMessage) {
               // Log.e(TAG, "fail: " + errorMessage);
            }

            @Override
            public void progress(final int progress) {
                    Message message = Message.obtain();
                    message.obj = progress;
                    message.what = 1;
                    handler.sendMessage(message);
            }
        });
    }


    private void doInstallAuthority(File file) {//>=24进行授权;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//授权后在MainActivity的回调中进行安装;
                    ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.REQUEST_INSTALL_PACKAGES}, 1);//sdk>26 android8.0以上需要有REQUEST_INSTALL_PACKAGES权限;
        } else {
            installApk(file);//直接安装;
            mActivity.finish();
        }
    }

    /**
     * 安装新版本应用
     */
    public void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri =null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri  = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID+".downApk", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri =  Uri.fromFile(file);
        }
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        mActivity.startActivity(intent);
        mActivity.finish();
    }





}
