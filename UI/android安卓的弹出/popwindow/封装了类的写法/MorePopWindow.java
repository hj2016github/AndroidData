package com.soyikeji.work.work.Chart.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.soyikeji.work.R;
import com.soyikeji.work.work.Chart.DisscusionListActivity;
import com.soyikeji.work.work.Chart.SelectFriendsActivity;
import com.soyikeji.work.work.Chart.fragment.ChartContactlistFragment;

/*import cn.rongcloud.im.ui.activity.SearchFriendActivity;
import cn.rongcloud.im.ui.activity.SelectFriendsActivity;*/


public class MorePopWindow extends PopupWindow {
   String realname;
    @SuppressLint("InflateParams") //跳过检查
    public MorePopWindow(final Activity context, final String realname) {//realname是为了传递参数使用;
        this.realname = realname;
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.popupwindow_add, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置点击屏幕其它地方弹出框消失
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作;
        //解决点击外部而popwindo无法消失的问题;
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        //以下设置点击事件
        RelativeLayout re_addfriends = (RelativeLayout) content.findViewById(R.id.re_addfriends);
        RelativeLayout re_modify_GroupName = (RelativeLayout) content.findViewById(R.id.modify_GroupName);//查看群组成员；
       // RelativeLayout re_dissolve_Group = (RelativeLayout) content.findViewById(R.id.dissolve_Group);
        re_addfriends.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectFriendsActivity.class);
                intent.putExtra("createGroup", true);
                intent.putExtra("realname",realname);
                context.startActivity(intent);
                MorePopWindow.this.dismiss();

            }

        });

        re_modify_GroupName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DisscusionListActivity.class));
                MorePopWindow.this.dismiss();
            }
        });


        /*re_dissolve_Group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //context.startActivity(new Intent(context, SearchFriendActivity.class));
                context.startActivity(new Intent(context, DisscusionListActivity.class));

                MorePopWindow.this.dismiss();
            }
        });
*/

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
