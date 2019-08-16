package com.sangfor.newxs66.tool.OkHttpUtils;


import com.sangfor.newxs66.Connector;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/3/13.
 */
public class OkHttpUtils {
    private static OkHttpClient client;
    private static Request request;
    private static Call call;

    //异步get请求
    public static void getRequest(String url, Callback callback) {
        //创建OkHttpClient对象
        //client = Singleton.getInstance();//多线程容易出错，所以用用饿汉模式
        //System.out.println(Thread.currentThread().getId());//测试线程；
        client = HugerSingleton.getInstance();
        client.cache();
        //创建Request对象
        request = new Request.Builder().url(url) .build();

        //创建执行任务的call对象
        call = client.newCall(request);
        //执行任务异步请求任务
        call.enqueue(callback);
    }


    /**
     * 上传文件
     * @param actionUrl 接口地址
     * @param filePath  本地文件地址
     */
    //异步无参上传文件
    public static void upLoadFile(String actionUrl, String filePath, Callback callBack) {
        client = HugerSingleton.getInstance();
        //补全请求地址
        String requstUrl =  String.format("%s%s", Connector.BASEURL,actionUrl);//需要修改
        //创建File
        File file = new File(filePath);
        //创建RequestBody,application/octet-stream为二进制流，相当于未知类型；
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        //创建Request
        Request request = new Request.Builder().url(requstUrl).post(requestBody).build();
        //创建执行任务的call对象
        call = client.newBuilder().writeTimeout(75, TimeUnit.SECONDS).build().newCall(request);
        //call = client.newCall(request);
        //执行任务异步请求任务
        call.enqueue(callBack);
    }


    //同步带参上传文件,需要指定文件类型，可以指定上传文件类型，也可以不指定；
    public  static  void  MultipartRequset(String url,
                                           // String otherParam,
                                           String filePath,
                                           Callback callback
    ){

        File file = new File(filePath);
        // String fileFormat = "image/jpeg";//下面creat第一个MediaType参数可以为空；
        // RequestBody requestBody = RequestBody.create(MediaType.parse(fileFormat),file);//第一参数可以为空；
        RequestBody requestBody  = RequestBody.create(null,file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) //必须加这句否则form表单的内容不容易提交上去；
                .addFormDataPart("anyfileFileName",file.getName())//两个参数的第二个位value；"key需要跟后台要"
                .addFormDataPart("anyfile",file.getName(),requestBody).build();//第二个参数可以随便指定一个文件的名字；
        //key需要跟后台要；
        client = HugerSingleton.getInstance();
        Request request = new Request.Builder()
                .url(url)
                .post(body).build();

        //创建执行任务的call对象
        call = client.newCall(request);
        //执行任务异步请求任务
        call.enqueue(callback);

    }



 /*
 *传参举例；
 * HashMap<String,Object> map = new HashMap<>();
  *      map.put("key",new File(""));
  *      map.put("key",Object.class);
* */

    //异步带参上传文件，比较灵活的方式，需要在调用之前写点HashMap来指定参数；
    public  static  void  AsyncMultipartRequset( String actionUrl,
                                                 HashMap<String, Object> paramsMap,
                                                 Callback callback){

        client = HugerSingleton.getInstance();
        String requstUrl =  String.format("%s%s", Connector.BASEURL,actionUrl);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//必须加这句否则form表单的内容不容易提交上去；

        //追加参数：如果是普通类型则按
        for (String key: paramsMap.keySet() ) {//遍历HashMap，如果是文件类型按照文件类型处理，如果不是按照其他类型处理；
            Object object = paramsMap.get(key);//从key中获取Object；
            if (!(object instanceof File)) {
                builder.addFormDataPart(key,object.toString().trim());
            }else {//如果是file类型参数的处理；
                File file = (File) object;
                builder.addFormDataPart(key,file.getName(),RequestBody.create(null,file));//不需要指定文件类型；
            }
        }
        //创建RequestBody
        RequestBody requsetBody =  builder.build();
        //创建Request
        Request request = new Request.Builder().url(requstUrl).post(requsetBody).build();
        //创建执行任务的call对象
        call = client.newBuilder().writeTimeout(120, TimeUnit.SECONDS).build().newCall(request);//设置超时时间为2分钟
        //call = client.newCall(request);
        //执行任务异步请求任务
        call.enqueue(callback);
    }

    public  static  void  MultipartRequset_file(String url,
                                                File file,
                                                Callback callback
    ){

        // String fileFormat = "image/jpeg";//下面creat第一个MediaType参数可以为空；
        // RequestBody requestBody = RequestBody.create(MediaType.parse(fileFormat),file);//第一参数可以为空；
        RequestBody requestBody  = RequestBody.create(null,file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) //必须加这句否则form表单的内容不容易提交上去；
                .addFormDataPart("anyfileFileName",file.getName())//两个参数的第二个位value；"key需要跟后台要"
                .addFormDataPart("anyfile",file.getName(),requestBody).build();//第二个参数可以随便指定一个文件的名字；
        //key需要跟后台要；
        client = HugerSingleton.getInstance();
        Request request = new Request.Builder()
                .url(url)
                .post(body).build();

        //创建执行任务的call对象
        call = client.newCall(request);
        //执行任务异步请求任务
        call.enqueue(callback);

    }

}
