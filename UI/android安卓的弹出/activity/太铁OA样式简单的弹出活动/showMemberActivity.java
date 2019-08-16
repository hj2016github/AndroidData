package com.soyikeji.work.work.Chart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soyikeji.work.R;
import com.soyikeji.work.work.Chart.ChartAdapter.ItemShowmemberactivityAdapter;
import com.soyikeji.work.work.Utils.GetURL;
import com.soyikeji.work.work.Utils.OkhttoUtils;
import com.soyikeji.work.work.Utils.ParJson;
import com.soyikeji.work.work.Utils.Singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class showMemberActivity extends Activity {

    //private android.widget.TextView tvallGroupMember;
    String title;
    String discuMembers;
    String targetID;
    private List<String> useridsList;
    private ListView discuMember_lv;
    private List<String> discuMemberList;
    private String useridlistString;
   // private TextView tv_allDiscuMembers;
    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        /*if(Members!= null && Members != "null"){
                            tv_allDiscuMembers.setText(Members);
                        }*/

                         List<String>   discuMemberList = (List<String>) msg.obj;
                         if(discuMemberList != null){
                             ItemShowmemberactivityAdapter adapter = new ItemShowmemberactivityAdapter(showMemberActivity.this,discuMemberList);
                             discuMember_lv.setAdapter(adapter);
                         }
                        break;
                }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//对话框样式的activity去掉标题栏；
        setContentView(R.layout.popwindow_showallmenber);
        discuMember_lv = (ListView) findViewById(R.id.lv_allGroupMember);
       // title = getIntent().getStringExtra("title");
       // discuMembers  = getIntent().getStringExtra("discuMember");
       // targetID = getIntent().getStringExtra("TargetID");
       // Log.e("tID", "onCreate: "+targetID );
       // tv_allDiscuMembers = (TextView) findViewById(R.id.tv_allDiscuMember);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        useridsList = bundle.getStringArrayList("discuMemberlist");
        discuMemberList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for(String member: useridsList){
            sb.append(member);
            sb.append(",");
        }
        String str = sb.toString();
        useridlistString = str.substring(0,str.length()-1);
       // Log.e("discussMembers",useridlistString.toString());
        getDisscusionNames();
        //尝试用hashmap失败。只能看自己创建的东西，别人的不能看；
       /* HashMap<String,String> membersMap = Singleton.getMembersMap();
        String members = membersMap.get(targetID);
        Log.e("members", "onCreate: "+members);*/
       /* if(title != null && title != "null")
        tvallGroupMember.setText(title);*/

        /*
        if (discuMembers != null && discuMembers != "null") {
            tvallGroupMember.setText(members);
        }*/


        /*if (members != null && members != "null") {
            tvallGroupMember.setText(members);
        }*/

        /*if(useridsList != null){
            tvallGroupMember.setText(useridsList.toString());
        }*/


    }

    private void getDisscusionNames(){
        String url= GetURL.getDisscuName(useridlistString);
       // String url = "http://60.205.150.100/Service/GetServiceInfo.ashx?Action=GetFieldValue&content={'appid':'101','fieldid':'"+useridlistString+"','fieldname':'REAL_NAME'}  ";
            OkhttoUtils.get_getJson(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(showMemberActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    String members =   ParJson.getDiscuMember(json);
                    Message message = Message.obtain();
                    message.what =0;
                    List<String> membersList = convertStrToArray(members);
                    message.obj = membersList;
                    handler.sendMessage(message);
                }
            });
    }

        public static List<String> convertStrToArray(String str){
        List<String> stringArrayList = null;
        String[] strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        stringArrayList =  Arrays.asList(strArray);
        return stringArrayList;
    }


}
