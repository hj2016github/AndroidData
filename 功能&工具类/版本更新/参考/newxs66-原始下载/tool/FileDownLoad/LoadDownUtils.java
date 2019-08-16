//package com.sangfor.vpntest.tool.FileDownLoad;
//
//import android.util.Log;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
///**
// * Created by lenovo on 2017/3/8.
// * 这个类的功能是把文件从网络下载到指定目录；
// */
//
//public class LoadDownUtils {
//
//
//
//
//
//    private  static   Boolean ifDownLoadFinish = false;
//    public static boolean downloadFiles(final String fileUrl){//参数为下载文件的地址；
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url = null;
//                InputStream in = null;
//                FileOutputStream out = null;
//                HttpURLConnection conn = null;
//
//                try {
//                    url = new URL(fileUrl);
//                    conn = (HttpURLConnection) url.openConnection();
//                    conn.connect();
//                    conn.setConnectTimeout(20000);
//                    if (fileUrl != null && fileUrl != "" && conn.getResponseCode() == 200) {
//                        //fileUrl非空并且连接成功；
//
//                        in = conn.getInputStream();
//                        long fileLength = conn.getContentLength();//文件长度；
//
//                        //保存下载文件到指定文件夹中；
//                        FileUtils.CreatDir();
//                        String FilePath = FileUtils.getFilePath(fileUrl);//file的全路径
//
//
//                        //以下获得输出流；
//                        out = new FileOutputStream(new File(FilePath));
//
//                        //以下从输入流写到输出流写到指定的文件；
//                        byte[] buffer = new byte[1024];
//                        int len = 0;//每次循环读取的字节数；
//                        long readedLength = 0l;//累加需要读取的总的字节数；
//                        while ((len = in.read(buffer)) != -1) {
//                            out.write(buffer, 0, len);
//                            readedLength += len;
//                        }
//
//                        if (readedLength >= fileLength) {
//                            ifDownLoadFinish = true;//当读取的字节的总数大于文件的长度，
//                            Log.i("ccccc","成功");
//
//                            //则标志文件下载完，ifDownLoadFinish变为true；
//
//                        }
//                        out.flush();
//
//                    } else {
//                        ifDownLoadFinish = false;
//                        Log.i("cs","失败");
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    if (out != null) {
//                        try {
//                            out.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (in != null) {
//                        try {
//                            in.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (conn != null) {
//                        conn.disconnect();
//                    }
//                }
//            }
//        }).start();
//
////        new VpnActivity().openFile(UrlUtils.encodeUrl(fileUrl));
////        Log.i("qqqqq",UrlUtils.encodeUrl(fileUrl));
//        return ifDownLoadFinish;
//
//    }
//
//}
