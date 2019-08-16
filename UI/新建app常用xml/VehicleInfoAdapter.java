package com.ygsj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ygsj.R;
import com.ygsj.VehicleInfoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleInfoAdapter extends BaseAdapter {
    private List<VehicleInfoEntity> list_carInfo = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    public VehicleInfoAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<VehicleInfoEntity> list){
        list_carInfo = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list_carInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return list_carInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        /*if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_liner, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }*/
       // viewHolder = (ViewHolder) convertView.getTag();
      /*
        VehicleInfoEntity entity = list_carInfo.get(position);
        if(entity.getKey().equals("提示")) {
            viewHolder.tv_key.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.tv_value.setTextColor(context.getResources().getColor(R.color.red));
        }
        viewHolder.tv_key.setText(entity.getKey());
        viewHolder.tv_value.setText(entity.getValue());*/



        View inflate_view = layoutInflater.inflate(R.layout.item_liner, null);
        TextView tv_key =  inflate_view.findViewById(R.id.tv_key);
        TextView tv_value = inflate_view.findViewById(R.id.tv_value);
        VehicleInfoEntity entity = list_carInfo.get(position);
        if(entity.getKey().equals("提示")) {
            tv_key.setTextColor(context.getResources().getColor(R.color.red));
            tv_value.setTextColor(context.getResources().getColor(R.color.red));
        }
        tv_key.setText(entity.getKey());
        tv_value.setText(entity.getValue());
       return inflate_view;
       // return convertView;
    }

    protected class ViewHolder {
        public TextView tv_key;
        public TextView tv_value;
        public ViewHolder(View view) {
            tv_key =  view.findViewById(R.id.tv_key);
            tv_value = view.findViewById(R.id.tv_value);

        }
    }
}
