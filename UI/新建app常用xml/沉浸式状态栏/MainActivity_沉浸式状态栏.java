package com.ygsj.daningapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import qiu.niorgai.StatusBarCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.webView = findViewById(R.id.web);
     //   StatusBarCompat.setStatusBarColor(this, Color.parseColor("#17a5e7"), 0);//状态栏系统蓝色,完全透明;
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#2196F3"), 0);//状态栏系统蓝色,完全透明;
        webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);//主要是这句
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
       /* WebIconDatabase.getInstance().open(getDir("icons",MODE_PRIVATE).getPath());
        Bitmap icon= webView.getFavicon();
        BitmapDrawable bd = new BitmapDrawable(icon);
        webView.setBackgroundDrawable(bd);*/
       // webSettings.setAppCacheEnabled(false);设置是否有缓存;
        //以下chrome进行调试H5的webview页面;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }*/
            webView.setWebChromeClient(new WebChromeClient());//这行最好不要丢掉
        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("http://59.48.178.214:8086/DispatchCars/login.html");
    }


}
