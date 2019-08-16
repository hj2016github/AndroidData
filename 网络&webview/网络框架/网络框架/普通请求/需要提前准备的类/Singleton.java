package com.soyikeji.work.work.Utils;

import android.content.Context;

import com.soyikeji.work.work.Chart.fragment.GrouplistFragment;
import com.soyikeji.work.work.Chart.fragment.TaitieConversationlistFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.OkHttpClient;

import static com.soyikeji.work.work.Chart.fragment.GrouplistFragment.*;

/**
 * Created by Administrator on 2016/8/3.
 */
public class Singleton {
    private static OkHttpClient client = null;
    private static ConversationListFragment conversationListFragment = null;
    private static TaitieConversationlistFragment taitieConversationlistFragment = null;
    private static List<String> disNameList = null;
    private static List<String> userids = null;
    private static GrouplistFragment.DisscusionAdapert disscusionAdapert = null;
    private static  List<Conversation> conversationList = null;
    public static  GrouplistFragment grouplistFragment = null;
    public  static HashMap<String,String> membersMap =null;
    private Singleton() {
    }

    public static OkHttpClient getInstance() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }


    public static ConversationListFragment getConvercationIntance() {
        if (conversationListFragment == null) {
            conversationListFragment = new ConversationListFragment();

        }
        RongIMClient.getInstance().getConversationList();
        return conversationListFragment;
    }

    public static TaitieConversationlistFragment getTaitieConversationlistFragment() {
        if (taitieConversationlistFragment == null) {
            taitieConversationlistFragment = new TaitieConversationlistFragment();
        }
        return taitieConversationlistFragment;
    }

    public static List<String> getDisNameList() {
        if (disNameList == null) {
            disNameList = new ArrayList<>();
        }
        return disNameList;
    }

    public static List<String> getUserids() {
        if (userids == null) {
            userids = new ArrayList<>();
        }
        return userids;
    }

    public static GrouplistFragment.DisscusionAdapert getDisscusionAdapert(Context context,
                                                                           List<Conversation> list) {

        if(disscusionAdapert ==null){
            GrouplistFragment grouplistFragment = new GrouplistFragment();
            disscusionAdapert = grouplistFragment.new DisscusionAdapert(context,list);
        }
        return  disscusionAdapert;
    }

    public static List<Conversation> getConversationList(){
        if (conversationList == null) {
            conversationList = new ArrayList<>();
        }
        return  conversationList;
    }

    public  static GrouplistFragment getGrouplistFragment() {
        if(grouplistFragment == null){
            grouplistFragment = new GrouplistFragment();
        }
        return  grouplistFragment;
    }

    public static HashMap<String,String> getMembersMap(){
        if(membersMap == null){
            membersMap = new HashMap<>();
        }
        return membersMap;
    }

}
