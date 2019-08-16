package com.soyikeji.work.work.Utils;

import com.soyikeji.work.work.entity.Announcements;
import com.soyikeji.work.work.entity.ContactUrl;
import com.soyikeji.work.work.entity.DeptAndPerson;
import com.soyikeji.work.work.entity.Document;
import com.soyikeji.work.work.entity.Information;
import com.soyikeji.work.work.entity.Person;
import com.soyikeji.work.work.entity.Watch;
import com.soyikeji.work.work.ul.GlobalContsts;
import com.soyikeji.work.work.ul.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/24.
 */

public class ParJson {


    public static Map<String,Object> getLogin(String json){//登录注册；
        Map<String,Object> map = new HashMap();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int returnCode = jsonObject.optInt("returnCode");
            map.put("returnCode",returnCode);
            JSONObject content =  jsonObject.optJSONObject("content");
            if(content != null){
                String userid = content.optString("id");
                String username = content.optString("name");
                String realName = content.optString("realName");
                String deptName = content.optString("deptName");
                String picUrl = content.optString("picUrl");
                map.put("username",username);
                map.put("realName",realName);
                map.put("deptName",deptName);
                map.put("picUrl",picUrl);
                map.put("id",userid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }


        public  static List<Announcements> getAnnouncements(String json){
            //拿到已读未读信息；
            List<Announcements> lists = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code=jsonObject .getInt("returnCode");
                if(code== GlobalContsts.RESPONSE_CODE_SUCCESS){
                    JSONArray array = jsonObject.getJSONArray("content");
                    if( array != null){
                      lists = JSONParser.parseBookList(array);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  lists;
        }

        public  static Information getInfomation(String json){//根据字典拿东西；
            Information information = new Information();

            try {
                JSONObject object = new JSONObject(json);
                JSONObject inforobjet = object.getJSONObject("content");
                if (object!= null && inforobjet !=null){
                    information.setFieldID(inforobjet.getString("fieldID"));
                    information.setFieldName(inforobjet.getString("fieldName"));
                    information.setFieldValue(inforobjet.getString("fieldValue"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  information;

        }

        public static  List<ContactUrl> getAllContact(String json){//拿到所有联系人；
            List<ContactUrl> list  = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length() ; i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        JSONArray jsonArray2 = jsonObject.optJSONArray("content");

                    for (int j = 0; j <jsonArray2.length() ; j++) {
                        ContactUrl contactUrl = new ContactUrl();
                        JSONObject jsonObjectConent = jsonArray2.optJSONObject(j);
                        if(jsonObjectConent != null){
                            contactUrl.setUserid(jsonObjectConent.optString("id"));
                            contactUrl.setDeptName(jsonObjectConent.optString("deptName"));
                            contactUrl.setMobilePhone(jsonObjectConent.optString("mobilePhone"));
                            contactUrl.setRelativeName(jsonObjectConent.optString("name"));
                            contactUrl.setImageUrl(jsonObjectConent.optString("picUrl"));
                            list.add(contactUrl);
                        }

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

    public static  List<ContactUrl> getAllRongContact(String json){//拿到所有联系人；
        List<ContactUrl> list  = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                JSONArray jsonArray2 = jsonObject.optJSONArray("content");

                for (int j = 0; j <jsonArray2.length() ; j++) {
                    ContactUrl contactUrl = new ContactUrl();
                    JSONObject jsonObjectConent = jsonArray2.optJSONObject(j);
                    if(jsonObjectConent != null){
                        contactUrl.setUserid(jsonObjectConent.optString("id"));
                        contactUrl.setDeptName(jsonObjectConent.optString("deptName"));
                        contactUrl.setMobilePhone(jsonObjectConent.optString("mobilePhone"));
                        contactUrl.setRelativeName(jsonObjectConent.optString("realName"));
                        contactUrl.setImageUrl(jsonObjectConent.optString("picUrl"));
                        list.add(contactUrl);
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }





    public static List<Document> getdocument(String json) {
        List<Document> documents  = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                JSONArray jsonArray3 = jsonObject.optJSONArray("content");
                for (int j = 0; j <jsonArray3.length() ; j++) {
                    Document document = new Document();
                    JSONObject jsonObjectConent = jsonArray3.optJSONObject(j);
                    if(jsonObjectConent != null){
                        document.setId(jsonObjectConent.getLong("id"));
                        document.setTitle(jsonObjectConent.getString("title"));
                        document.setNoticeUrl(jsonObjectConent.getString("noticeUrl"));
                        document.setTypeName(jsonObjectConent.getString("typeName"));
                        document.setCreateUserName(jsonObjectConent.getString("createUserName"));
                        document.setCreateDate(jsonObjectConent.getString("createDate"));
                        documents.add(document);
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return documents;
    }
    public static List<Watch> getwatch(String json) {
        List<Watch> watches = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("returnCode");
            if (code == GlobalContsts.RESPONSE_CODE_SUCCESS) {
                JSONArray array = jsonObject.getJSONArray("content");
                if (array != null) {
                    watches = JSONParser.parseWhatch(array);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return watches;
    }

    public  static  List<DeptAndPerson> getDeptAndPerson(String json){//拿到部门加所有联系人；
      List<DeptAndPerson> list_DeptAndPerson = new ArrayList<>();

        try {
            JSONArray jsonArray  =  new JSONArray(json);
            JSONObject JObj_DeptAndPerson = jsonArray.getJSONObject(0);
            JSONArray JArray_content = JObj_DeptAndPerson.getJSONArray("content");


            for (int i = 0; i < JArray_content.length(); i++) {
                DeptAndPerson dap = new DeptAndPerson();
                JSONObject jsonObject = JArray_content.getJSONObject(i);
                String depname = jsonObject.optString("name");
                dap.setDept(depname);


                JSONArray JArray_Person = jsonObject.optJSONArray("person");
                List<Person> list_Person = new ArrayList<>();
                for (int j = 0; j < JArray_Person.length(); j++) {
                    Person person = new Person();
                    JSONObject JObj_person = JArray_Person.optJSONObject(j);

                    if(JObj_person != null){
                        String Pername = JObj_person.optString("name");
                        String mobilePhone = JObj_person.optString("mobilePhone");
                        String picUrl = JObj_person.optString("picUrl");
                        person.setName(Pername);
                        person.setMobliePhone(mobilePhone);
                        person.setPicUrl(picUrl);
                        list_Person.add(person);

                    }
                }
                dap.setList(list_Person);//应该在第一层循环
                list_DeptAndPerson.add(dap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  list_DeptAndPerson;
    }

    //从useid拿username;
    public static String getDiscuMember(String json){
        String member = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
             int returnCode = jsonObject.optInt("returnCode");
            if(returnCode == 10002){
                JSONObject contentObject = jsonObject.optJSONObject("content");
                if(contentObject != null){
                    member = contentObject.optString("fieldValue");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return member;
    }





}
