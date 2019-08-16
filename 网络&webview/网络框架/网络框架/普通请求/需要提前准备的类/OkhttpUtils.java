package com.soyikeji.work.work.Utils;

import android.content.Context;

import android.widget.ImageView;

import com.soyikeji.work.R;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/3.
 */
public class OkhttoUtils {
    private static OkHttpClient client;
    private static Request request;
    private static Call call;
    private static String json;

    //创建OkHttpClient对象
    public static void get_getJson(String url, Callback callback) {
        //创建OkHttpClient对象
        client = Singleton.getInstance();
        client.cache();
        //创建Request对象
        request = new Request.Builder()
                .url(url)
                .build();
        //创建执行任务的call对象
        call = client.newCall(request);
        //执行任务
        call.enqueue(callback);
    }

    //post请求
    public static void post_GetJson(String url, Callback callback) {
        client = Singleton.getInstance();
        FormBody.Builder builder = new FormBody.Builder();
        //数据不对需要重写；
        builder.add("pageNo", "1");
        builder.add("pageSize", "20");
        builder.add("serialIds", "2143,3404");
        builder.add("v", "4.0.0");
        request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        call = client.newCall(request);
        call.enqueue(callback);

    }


}
//.asGif()
//.diskCacheStrategy(DiskCacheStrategy.SOURCE)
//      .thumbnail(1f)