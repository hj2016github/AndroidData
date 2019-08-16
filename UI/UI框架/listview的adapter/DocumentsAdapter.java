package com.soyikeji.work.work.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soyikeji.work.R;
import com.soyikeji.work.work.entity.Contact;
import com.soyikeji.work.work.entity.Document;
import com.soyikeji.work.work.entity.Watch;

/**
 * 我的文档的适配器
 */
public class DocumentsAdapter extends BaseAdapter {
    private Context context;
    private List<Document> documents;
    private LayoutInflater inflater;

    public DocumentsAdapter(Context context, List<Document> documents) {
        super();
        this.context = context;
        this. documents = documents;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return documents.size();
    }

    @Override
    public Document getItem(int position) {
        return documents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.documents, null);
            holder = new ViewHolder();
            holder.tvuser = (TextView) convertView.findViewById(R.id.tv_user);
            holder.tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvdata = (TextView) convertView.findViewById(R.id.tv_data);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Document document = documents.get(position);
        holder.tvtitle.setText(document.getTitle());
        holder.tvuser.setText(document.getTypeName());
        holder.tvdata.setText(document.getCreateDate());
        return convertView;
    }

    class ViewHolder {
        TextView tvtitle;
        TextView tvuser;
        TextView tvdata;


    }
}
