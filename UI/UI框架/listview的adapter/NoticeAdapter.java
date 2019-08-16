package com.soyikeji.work.work.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soyikeji.work.R;
import com.soyikeji.work.work.Utils.GetURL;
import com.soyikeji.work.work.Utils.OkhttoUtils;
import com.soyikeji.work.work.Utils.ParJson;
import com.soyikeji.work.work.entity.Announcements;
import com.soyikeji.work.work.entity.Information;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * 通知公告
 * Created by Administrator on 2016/10/19.
 */
public class NoticeAdapter extends BaseAdapter{
    private List<Announcements> notices;
    private Context context;
    private LayoutInflater inflater;
    private  ViewHolder holder = null;
    public NoticeAdapter(List<Announcements> notices, Context context) {
        super();
        this.notices = notices;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Announcements getItem(int position) {
        return notices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.read_new, null);
            holder = new ViewHolder();
            holder.tvhour = (TextView) convertView.findViewById(R.id.tv_hour);
            holder.tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvplace = (TextView) convertView.findViewById(R.id.tv_place);
            holder.tvprompt = (TextView) convertView
                    .findViewById(R.id.tv_prompt);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Announcements notice = notices.get(position);
        holder.tvtitle.setText( notice.getTitle());//拿到title；

        //以下2次请求拿发布人；
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                  Information  information = (Information) msg.obj;
                    //拿到发布人信息；
                    holder.tvplace.setText(information.getFieldValue());
                        break;
                    case 1:
                     //拿阅读状态信息；
                    Information information1 = (Information) msg.obj;
                        holder.tvprompt.setText(information1.getFieldValue());

                        break;
                }

            }
        };

        OkhttoUtils.get_getJson(GetURL.InfomationGet(notice.getCreateUserName()), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Information information =  ParJson.getInfomation(json);
                //在发到主线程；
                Message message = new Message();
                message.obj = information;
                message.what = 0;
                handler.sendMessage(message);

            }
        });


        //设置时间；
        holder.tvhour.setText(notice.getCreateDate().substring(0,11));

       //设置阅读状态；
        OkhttoUtils.get_getJson(GetURL.InfomationGet(notice.getIsReadName()), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // TODO: 2016/10/25 如果网络申请不下来应该有个默认值设置；
            }

            // TODO: 2016/10/25 拿数据地方都需要判空；
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Information information = ParJson.getInfomation(json);
                Message message = new Message();
                message.what = 1;
                message.obj = information;
                handler.sendMessage(message);

            }
        });




        return convertView;
    }

    class ViewHolder {
        TextView tvtitle;
        TextView tvplace;
        TextView tvhour;
        TextView tvprompt;
    }

}
