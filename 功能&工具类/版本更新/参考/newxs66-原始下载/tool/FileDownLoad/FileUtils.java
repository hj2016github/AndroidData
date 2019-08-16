package com.sangfor.newxs66.tool.FileDownLoad;

import android.os.Environment;

import com.sangfor.newxs66.tool.UrlUtils;

import java.io.File;

/**
 * Created by lenovo on 2017/3/8.
 * 此类的功能主要是把网址url转成android的文件的路径
 */

public class FileUtils {

    private static final String FILE_SEPARATOR = "/";//文件分隔符；
    //保存下载到内存卡目录；
    private static final String FILE_Dir = Environment.getExternalStorageDirectory()
            + FILE_SEPARATOR
            + "XSDownLoadFile"
            + FILE_SEPARATOR;

    public  static  void  CreatDir(){//创建下载目录
        File filePath = new File(FILE_Dir);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
    }

    public  static  String getFileName(String url){//通过file的url获得到file的Name,带后缀
        String decodeFileName = "";

        if(url!=null&& url.contains("fileName")){
            int startIndex = url.lastIndexOf("=");
            String  fileName =   url.substring(startIndex+1);//转码前的文件名
            decodeFileName =  UrlUtils.getURLDecoderString(fileName);///转码前的文件名
            return  decodeFileName;
        }else {
            int startIndex = url.lastIndexOf("/");
            String fileName =   url.substring(startIndex+1);//转码前的文件名
            decodeFileName =  UrlUtils.getURLDecoderString(fileName);//转码前的文件名
            return  decodeFileName;
        }
    }

    public  static  String getFilePath(String fileUrl) {//通过附件网址获得全file的全路径（andorid）；
        return  FILE_Dir + getFileName(fileUrl);
    }

}
