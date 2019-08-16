package com.ygsj.daningwaag;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.utils.DensityUtils;


public abstract class Table_ativity<T> extends AppCompatActivity {//T表示smarttable对应的实体
    private LinearLayout contentView ;
    private LinearLayout contentLayout ;
    private TextView tv_headerTitle;
    private TextView tv_footerDate;
    private TextView tv_footerRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_table);
    }

    @Override
    public void setContentView(int layoutResID) {

        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.removeAllViews();
        contentLayout=new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        content.addView(contentLayout);
        LayoutInflater.from(this).inflate(layoutResID, contentLayout, true);
        getData(findTable()); //调用抽象方法,直接调用实现类的方法,有点类似于接口的方法;

        /* if (contentView == null && R.layout.activity_table == layoutResID){
            super.setContentView(R.layout.activity_table);
            contentView = findViewById(R.id.ll_table);
            tv_headerTitle = findViewById(R.id.table_header_tv_title);
            tv_footerDate = findViewById(R.id.tableFooter_tv_date);
            tv_footerRefresh = findViewById(R.id.tv_footer_refresh);
            contentView.removeAllViews();
        }else if(layoutResID != R.layout.activity_table) {
            View addView = LayoutInflater.from(this).inflate(layoutResID, null);
            contentView.addView(addView, new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }*/

    }

    protected abstract void getData(SmartTable table);

    protected abstract SmartTable  findTable();


    //公用的代码;
    protected  void tableStyle(SmartTable table,final Context context ){
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        //奇偶行变色;
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {//内容奇数行变灰色
                    return ContextCompat.getColor(context, R.color.white);
                }
                return TableConfig.INVALID_COLOR;
            }
        });

        //y轴变色;
        ICellBackgroundFormat<Integer> backgroundFormat_y = new BaseCellBackgroundFormat<Integer>() {
            @Override
            public int getBackGroundColor(Integer position) {
                if (position % 2 == 0) {
                    return ContextCompat.getColor(context, R.color.blue);
                }
                return TableConfig.INVALID_COLOR;

            }

            @Override
            public int getTextColor(Integer position) {
                if (position % 2 == 0) {
                    return ContextCompat.getColor(context, R.color.white);
                }
                return TableConfig.INVALID_COLOR;
            }
        };

        table.getConfig().setYSequenceCellBgFormat(backgroundFormat_y);//设置Y轴颜色
        table.getConfig().setShowXSequence(false).setShowTableTitle(false);//不显示x轴跟标题;
        table.setZoom(true,2,0.2f);//设置缩放;
    }

    public void goback(View view) {
        finish();
    } //点击toolbar的imageview回退的公用事件;
}
