package com.soyikeji.work.work.Utils;

import android.util.Log;

import com.soyikeji.work.work.ul.GlobalContsts;

/**
 * Created by Administrator on 2016/10/25.
 */

public class GetURL {

    public static  String  InfomationGet(String id ){
        String BaseURL = GlobalContsts.BASEURL_INFO;
        return BaseURL+"{'appid':'101','fieldid':'"+id+"','fieldname':'NAME'}";
    }

    public  static String GetReadURL(String userid, String isReadID){
        String BaseNoticeURL = GlobalContsts.BASEURL_NoticeURL;
        return  BaseNoticeURL + "{'appid':'101','userid':'"+ userid+"','isreadid':'"+isReadID+"'}";
    }


   public  static  String GetAllContacts(String appid){//拿所有联系人；
         return "http://60.205.150.100/Service/GetServiceInfo.ashx?Action=GetPersonList&content={'appid':'"+appid+"'}";
    }

    public static  String GetSentionAndContacts(String appid){//拿所有联系人加部门；
        return "http://60.205.150.100/Service/GetServiceInfo.ashx?Action=GetDeptList&content={'appid':'"+appid+"'}";
    }

    public static String getPicUrl(String url){
        String IP = GlobalContsts.IP;
        return IP+url;
    }

    public  static  String getToken(String id,String username){
        String IP = GlobalContsts.IP;
        String append = GlobalContsts.RongCloud;
        String json = "{'appid':'101','userid':'"+id+"','username':'"+username+"'}";
        String url = IP + append +json;

        return url;

    }

    public  static  String getDisscuName(String useridlistString) {
        String url = "http://60.205.150.100/Service/GetServiceInfo.ashx?Action=GetFieldValue&content={'appid':'101','fieldid':'"+useridlistString+"','fieldname':'REAL_NAME'}  ";
        return url;
    }

}
