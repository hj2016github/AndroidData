package com.sangfor.newxs66.tool.OkHttpUtils;

import okhttp3.OkHttpClient;

/**
 * 封装ok
 * Created by lenovo on 2017/2/1.
 */
public class HugerSingleton {
    private static OkHttpClient ourInstance = new OkHttpClient();

    public static OkHttpClient getInstance() {
        return ourInstance;
    }

    private HugerSingleton() {
    }
}
