package com.ygsj.daningwaag;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.tip.MultiLineBubbleTip;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.utils.DensityUtils;
import com.ygsj.daningwaag.entity.SalesDelivery;

import java.util.ArrayList;
import java.util.List;

public class Table_SalesDeliveryActivitys extends Table_ativity<SalesDelivery> {
    public SmartTable<SalesDelivery> table; //父类中的OnCreat进行初始化;
    private List<SalesDelivery> salesDeliveries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table__sales_delivery);//调用的是父类Table_ativity的方法;
        table = findTable();
        //因为tableStyle需要传的参数最好在子类中获取,所以没有在父类中写抽象方法;
        tableStyle(table,this);//子类继承一部分代码,其余的独立;
    }
    @Override
    protected  SmartTable  findTable(){//调用父类抽象类直接调用子类(实现类)的方法,在父类onCreatView中可以看到;
        table = findViewById(R.id.table_SalesDelivery);
        return table;
    }

    @Override
    public void getData(SmartTable table){
        salesDeliveries = new ArrayList<>();
        for (int i = 0; i <30 ; i++) {
            SalesDelivery salesDelivery  = new SalesDelivery();
            salesDelivery.setBillType("汽车订单");
            salesDelivery.setBillOfLadingCode("2NMX-SAADD-20181007-001");
            salesDelivery.setMakeBillsDay("2018-10-7");
            salesDelivery.setPurchasingUnit("河南金鑫能源有限公司");
            salesDelivery.setGoodsName("大宁无烟沫煤");
            salesDelivery.setBillingNumbers_billing(3000.00);
            salesDelivery.setReturnGoodsNumbers_billing(0.00);
            salesDelivery.setDeliveredQuantity_sendoutGoods(2614.16);
            salesDelivery.setUndeliveredQuantity_sendoutGoods(385.84);
            salesDelivery.setOperator("");
            salesDelivery.setMakeBiller("TMS导入");
            salesDelivery.setAuditor("TMS");
            salesDelivery.setRemark("");
            salesDeliveries.add(salesDelivery);
        }
        table.setData(salesDeliveries);
    }

    @Override
    public  void  tableStyle(SmartTable table,final Context context){
        super.tableStyle(table,context);
        setTips();
    }

    protected void setTips() {//因为参数设置某行T.getXXX需要用反射,显得过于复杂所以就没封装到父类中;
        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        MultiLineBubbleTip<Column> tip = new MultiLineBubbleTip<Column>(this,R.drawable.round_rect,
                R.drawable.triangle,fontStyle) {
            @Override
            public boolean isShowTip(Column column, int position) {
                if (column.getFieldName().equals("billOfLadingCode")) return true;
                else if (column.getFieldName().equals("purchasingUnit")) return true;
                else return false;
            }


            @Override
            public String[] format(Column column, int position) {
                SalesDelivery data = salesDeliveries.get(position);
                String[] strings = null;
                if (column.getFieldName().equals("billOfLadingCode"))
                    strings = new String[]{"详细信息",  data.getBillOfLadingCode()};
                if (column.getFieldName().equals("purchasingUnit"))
                    strings = new String[]{"详细信息",  data.getPurchasingUnit()};
                return strings;
            }
        };
        tip.setColorFilter(Color.parseColor("#FA8072"));//弹出框颜色;
        tip.setAlpha(0.8f);
        table.getProvider().setTip(tip);
    }

}
