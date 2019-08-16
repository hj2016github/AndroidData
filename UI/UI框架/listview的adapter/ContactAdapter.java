package com.soyikeji.work.work.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

//import com.android.volley.RequestQueue;
import com.soyikeji.work.R;
import com.soyikeji.work.work.Utils.GetURL;
import com.soyikeji.work.work.Utils.PicassoUtils;
import com.soyikeji.work.work.addresslistUtils.CharacterParser;
import com.soyikeji.work.work.app.MyApplication;
import com.soyikeji.work.work.entity.ContactUrl;

import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

/**
 * confirm通讯录的适配器
 * Created by Administrator on 2016/10/25.
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<ContactUrl> contacts;
    private LayoutInflater inflater;
    //private ImageLoader imageLoader;
    //private RequestQueue queue = MyApplication.getqueue();
    CharacterParser characterParser = CharacterParser.getInstance();//汉字转拼音的工具类；
    private String pinyin;
    private ViewHolder holder;

    public ContactAdapter(Context context, List<ContactUrl> contacts) {
        super();
        this.context = context;
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
      /*  imageLoader = new ImageLoader(queue, new BitmapCache());*/
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<ContactUrl> list) {
        this.contacts = list;
        notifyDataSetChanged();
    }


    /*public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }*/

    @Override
    public int getCount() {
        // 获取数据的数量，即：有多少数据需要被显示
        return contacts.size();
    }

    @Override
    public ContactUrl getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

   /* @SuppressLint("InflateParams")*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_contact, null);
            holder = new ViewHolder();
            holder.tv_contact_item_section = (TextView) convertView.findViewById(R.id.tv_contact_item_section);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.imageView_name = (ImageView) convertView.findViewById(R.id.imageView_name);
            holder.tv_Mobile = (TextView) convertView.findViewById(R.id.tv_Mobile);
            holder.tv_part = (TextView) convertView.findViewById(R.id.tv_part);
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder) convertView.getTag();
        }
        final ContactUrl contact = contacts.get(position);
        pinyin = characterParser.getSelling(contact.getRelativeName());
        holder.tv_contact_item_section.setText("  "+pinyin.toUpperCase(Locale.CHINA).charAt(0) + "");
        // 批量下载图片
       /* ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.imageView_name, R.mipmap.sy_03,
                R.mipmap.sy_03);*/
        // imageLoader.get(contact.getImageView(), listener);
        //为了防止报空进行处理，contactUrl类中没有这个字段；
        //imageLoader.get("1001", listener);
        holder.tv_name.setText(contact.getRelativeName());
        String phone = contact.getMobilePhone();
        if(phone != null&& phone != "null"){
            holder.tv_Mobile.setText(contact.getMobilePhone());
        }else {
            holder.tv_Mobile.setText("");
        }

        holder.tv_part.setText(contact.getDeptName());
        PicassoUtils.getPicFromPicasso(GetURL.getPicUrl(contact.getImageUrl()),holder.imageView_name,context);

        convertView.setOnClickListener(new View.OnClickListener() {
            //点击整体拿到textvie的信息之后跳转电话界面；
            @Override
            public void onClick(View v) {
                // TODO: 2016/11/14 为何不能直接从tv中拿东西；
                String mobilePhone =  contact.getMobilePhone();
                if(mobilePhone != null && mobilePhone != "null"){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobilePhone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                }

            }
        });


//如果判断是不是能排到第一个；
        if (position == getPositionForSection(getSectionForPosition(position))) {
            holder.tv_contact_item_section.setVisibility(View.VISIBLE);
        } else {
            holder.tv_contact_item_section.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_contact_item_section;
        ImageView imageView_name;
        TextView tv_name;
        TextView tv_Mobile;
        TextView tv_part;

    }

    @Override
    public int getPositionForSection(int section) {
        // 【功能目标】获取首字母section应该显示的position位置
        // 循环遍历所有联系人的数据
        for (int i = 0; i < contacts.size(); i++) {
            // -- 找到当次循环时，联系人名字的首字母
            String pinyin =  characterParser.getSelling(contacts.get(i).getRelativeName());
            int ch = pinyin.toUpperCase(Locale.CHINA).charAt(0);
            // -- 将获取到的当次首字母与参数section对比
            if (ch == section) {
                // -- -- 如果相同，则是需要得到的答案：position，则跳出循环，返回结果
                return i;
            } else {
                // -- -- 如果不相同，则不作任何处理，继续下一次循环
            }
        }
        return NO_SUCH_SECTION;
    }

    public static final int NO_SUCH_SECTION = -998;

    @Override
    public int getSectionForPosition(int position) {
        // 【功能目标】获取position位置对应的首字母section
        //汉字转拼音

        String pinyin =  characterParser.getSelling(contacts.get(position).getRelativeName())  ;
        return  pinyin.toUpperCase(Locale.CHINA).charAt(0);
       // return contacts.get(position).pinyin.toUpperCase(Locale.CHINA).charAt(0);
    }

    @Override
    public Object[] getSections() {
        // 1. 创建TreeSet集合，用于存储每一个联系人的拼音首字母
        // -- 基于TreeSet的特性，存储的多个首字母一定是不重复的，且按照字典排序法排序的
        TreeSet<String> set = new TreeSet<String>();

        // 2. 遍历所有联系人的数据
        for (int i = 0; i < contacts.size(); i++) {
            // 2.1. 将当次循环到的联系人的首字母添加到TreeSet集合中
            set.add("" + (char) getSectionForPosition(i));
        }

        // 3. 基于TreeSet集合的长度，创建String[]数组
        String[] sections = new String[set.size()];

        // 4. 遍历TreeSet集合，将各元素依次赋值给String[]数组的各元素
        int i = 0;
        for (String section : set) {
            sections[i] = section;
            i++;
        }
        return sections;
    }


}
