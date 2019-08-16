package com.sangfor.newxs66;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sangfor.newxs66.tool.FileDownLoad.FileUtils;
import com.sangfor.newxs66.tool.FileUpLoad.ReWebChomeClient;
import com.sangfor.newxs66.tool.FileUpLoad.UpPicUtils;
import com.sangfor.newxs66.tool.OkHttpUtils.OkHttpUtils;
import com.sangfor.newxs66.tool.OpenFile.OpenFile;
import com.sangfor.newxs66.tool.UrlUtils;
import com.sangfor.newxs66.updata_apk.UpdateAppManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 内网登录
 */
@SuppressLint("JavascriptInterface")
public class IntranetActivity extends Activity {
    private WebView webView;
    private LinearLayout lay01;
    private static final String HTTP_RES = Connector.OA_Url;
    private UpdateAppManager updateManager;// 自主更新判断
    private static final String TAG = "IntranetActivity";
    private ImageView image2;
    private String filePath;
    private File file;
    private ProgressDialog pb1;

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
                    IntranetActivity.this);
            builders.setTitle("抱歉，网络连接失败，是否进行网络设置");
            builders.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 进入无线网络配置界面
                            IntranetActivity.this.startActivity(new Intent(
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
                            IntranetActivity.this.finish();
                        }
                    });
            builders.setCancelable(false);
            builders.show();

        }
        return;
    }

    ReWebChomeClient xwebchromeclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_intranet);

        image2 = (ImageView) findViewById(R.id.image2);
        lay01 = (LinearLayout) findViewById(R.id.lay01);
        checkFinish();//判断有无网络
        //版本更新
        updateManager = new UpdateAppManager(this);
        updateManager.checkUpdateInfo(null);

        webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式


        //防止乱码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        final WebSettings webSettings = webView.getSettings();
        // 不使用缓存，只从网络获取数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        // 设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient());

        LoadPageByWebView(HTTP_RES);
        new Thread(new MyThread()).start();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                readHtmlFormAssets();
            }
        });

    }

    //图片和webView同时加载
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    image2.setVisibility(View.GONE);
                    LoadPageByWebView(HTTP_RES);
                    webView.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    pb1.dismiss();
                    break;
            }
        }
    };

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);// 发送消息
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    //上传文件是个耗时操作，从程序看webview销毁的速度要快于上传的速度，对webview的销毁进行重构；
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        // webView.destroy();
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }

    /**
     * 调用本地
     */
    private void readHtmlFormAssets() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果
        webView.loadUrl("file:///android_asset/errornews.html");
    }


    public void LoadPageByWebView(String url) {


        if (webView == null) {
            webView = (WebView) findViewById(R.id.webView);
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式

        //防止乱码
        webView.loadData(url, "text/html", "utf-8");
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        WebSettings webSettings = webView.getSettings();
        // 不使用缓存，只从网络获取数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        // 设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
        // 网页中包含JavaScript内容需调用以下方法，参数为true
        webSettings.setJavaScriptEnabled(true);
        // 开启H5(APPCache)缓存功能
        webSettings.setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        webView.setWebViewClient(new MyWebViewClient(lay01));
        webView.addJavascriptInterface(new JavascriptInterface(), "Android");
        SharedPreferences preferences = getSharedPreferences("liu", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String name = preferences.getString("uName", null);
        String pass = preferences.getString("uPass", null);
        editor.commit();
        if (name != null && name != "" && pass != null && pass != "") {
            String url1 = url + "txtName=" + name + "&" + "txtPass=" + pass;
            webView.loadUrl(url1);
            Log.e("aaa", "url1" + url1);
        } else {
            webView.loadUrl(url);
            Log.e("aaa", url);
        }

    }

    /**
     * 后台获取用户名密码
     */
    class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void downUrl(String url) {
            final String downUrl = url;
            SharedPreferences preferences = IntranetActivity.this.getSharedPreferences("liu", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("downUrl", downUrl);
            editor.commit();
        }
        @android.webkit.JavascriptInterface
        public void login(String txtName, String txtPass) {
            final String name1 = txtName;
            final String pwd1 = txtPass;
            SharedPreferences preferences = IntranetActivity.this.getSharedPreferences("liu", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("uName", name1);
            editor.putString("uPass", pwd1);
            editor.commit();
        }


        @android.webkit.JavascriptInterface
        public void clickOnAndroid() {        // 清空账号密码

            SharedPreferences preferences = IntranetActivity.this.getSharedPreferences("liu", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("uName");
            editor.remove("uPass");
            editor.commit();

        }

        @android.webkit.JavascriptInterface
        //打电话
        public void CallFunc(String numberStr) {
            String t = "tel:" + numberStr;
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(t));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 下载附件
         *
         * @param url
         */
        @android.webkit.JavascriptInterface
        public void DownLoad(String url) {
            SharedPreferences preferences = getSharedPreferences("liu", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String IPurl = preferences.getString("downUrl", null);
            editor.commit();
//            final String downUrl1 = Connector.FileDownLoadUrl + url;
            final String downUrl1 = IPurl+ url;
            final String url1 = UrlUtils.encodeUrl(downUrl1);
            filePath = FileUtils.getFilePath(url1);
            file = new File(filePath);
//            if (file.exists()) {
//                startActivity(OpenFile.openFile(filePath));
//            } else {
                pb1 = ProgressDialog.show(IntranetActivity.this, "", "加载中，请稍后……",false,false);
                downloadFiles(url1);

//            }
        }

        /**
         * 上传
         */
        @android.webkit.JavascriptInterface
        public void UpPic() {


//            //调用弹出对话框；
//            ReWebChomeClient.setContext(IntranetActivity.this);
//            ReWebChomeClient.changeHeadIcon();//弹出对话框进行选择图片或者拍照；


        }
    }


    //上传文件的方法：
    private void upLoadFiles_test(File file) {
        String url = Connector.UpPicUrl;
        OkHttpUtils.MultipartRequset_file(url, file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i(TAG, "onFailure:" + e.toString());//错误的原因
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(IntranetActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.message());
                Log.i(TAG, "onResponse: " + response.body().string());
                Log.i(TAG, "onResponse: " + response.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(IntranetActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void openFile() {

        startActivity(OpenFile.openFile(filePath));
    }

    private long firstTime = 0;

    /**
     * 双击右键退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                try {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    if (secondTime - firstTime > 1000) {
                        firstTime = secondTime;// 更新firstTime
                        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT)
                                .show();
                        return true;

                    } else {

                        System.exit(0);
                    }
                } catch (Exception e) {
                }
                break;
        }
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        //当前无可用网络提示图片
        LinearLayout lay01;

        public MyWebViewClient(LinearLayout lay01) {
            this.lay01 = lay01;
        }

        @Override
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler,
                    error);//这样会调用到handler.cancel()

//            isPageError = true;
            handler.proceed();// 忽略证书错误
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    private Boolean ifDownLoadFinish = false;

    public boolean downloadFiles(final String fileUrl) {//参数为下载文件的地址；

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;

                try {
                    url = new URL(fileUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    conn.setConnectTimeout(20000);
                    if (fileUrl != null && fileUrl != "" && conn.getResponseCode() == 200) {
                        //fileUrl非空并且连接成功；

                        in = conn.getInputStream();
                        long fileLength = conn.getContentLength();//文件长度；

                        //保存下载文件到指定文件夹中；

                        FileUtils.CreatDir();
                        String FilePath = FileUtils.getFilePath(fileUrl);//file的全路径


                        //以下获得输出流；
                        out = new FileOutputStream(new File(FilePath));

                        //以下从输入流写到输出流写到指定的文件；
                        byte[] buffer = new byte[1024];
                        int len = 0;//每次循环读取的字节数；
                        long readedLength = 0l;//累加需要读取的总的字节数；
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                            readedLength += len;
                        }
                        if (readedLength >= fileLength) {
                            ifDownLoadFinish = true;//当读取的字节的总数大于文件的长度，
                            Log.i("ccccc", "成功");
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);// 发送消息
                            openFile();
                            //则标志文件下载完，ifDownLoadFinish变为true；

                        }
                        out.flush();

                    } else {
                        ifDownLoadFinish = false;
                        Log.i("cs", "失败");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
        return ifDownLoadFinish;
    }

    /*
   * 照相机传照片的回调
   * */
    String mCameraFilePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {//拍照
            Uri resultUri = data.getData();
            if (resultUri != null) {
                resultUri = data.getData();
//                String filePath = getRealPathFromURI(resultUri);
//                upLoadFiles_test(filePath);
//                File file = new File(resultUri.toString());
//                upLoadFiles_test(file);
                String filePath = UpPicUtils.getPath(this, resultUri);
//                String filePath = getRealPathFromURI(resultUri);
//                File file = new File(filePath);
//                file.getName();
            } else if (data == null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                resultUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
//                String filePath = getRealPathFromURI(resultUri);
//                upLoadFiles_test(filePath);
                String filePath = getRealPathFromURI(resultUri);
                File file = new File(filePath);
                //File file = new File(resultUri.toString());
                upLoadFiles_test(file);


            }

        } else if (requestCode == 3) {//相册上传
            Uri resultUri = (data == null || resultCode != RESULT_OK) ? null : data.getData();
//             String filePath = getRealPathFromURI(resultUri);
//             upLoadFiles_test(filePath);
            String filePath = UpPicUtils.getPath(this, resultUri);
//            String filePath = getRealPathFromURI(resultUri);
            Log.i("mmmm", "llll" + filePath);
            File file = new File(filePath);
//                file.getName();
            UpLoadPath(filePath);


        }

    }


    //Uri转file的真实路径；
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void UpLoadPath(String filepath) {
        String url = Connector.UpPicUrl;
        OkHttpUtils.MultipartRequset(url, filepath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(IntranetActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.message());
                Log.i(TAG, "onResponse: " + response.body().string());
                Log.i(TAG, "onResponse: " + response.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(IntranetActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

}


