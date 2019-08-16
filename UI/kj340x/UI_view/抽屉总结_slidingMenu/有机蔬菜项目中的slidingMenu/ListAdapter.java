package com.example.administrator.organic_greens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.organic_greens.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/4.
 */
public class ListAdapter extends BaseAdapter {
    private List<Map<String,Object>> list;
    private Context context;
    private LayoutInflater inflater;

    public ListAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        if(convertView ==null){
            convertView=inflater.inflate(R.layout.menu_item_list,null);
            viewHolder=new MyViewHolder();
            viewHolder.menuImage= (ImageView) convertView.findViewById(R.id.menu_image_left);
            viewHolder.menuText= (TextView) convertView.findViewById(R.id.menu_text_right);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (MyViewHolder) convertView.getTag();
        }
        viewHolder.menuImage.setImageResource((Integer) list.get(position).get("image"));
        viewHolder.menuText.setText(list.get(position).get("content")+"");
        return convertView;
    }
    class MyViewHolder{
        private ImageView menuImage;
        private TextView menuText;
    }
}
