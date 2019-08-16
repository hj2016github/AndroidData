package com.gasplatform.ygsj.mashgasmonitoring.widget.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import com.gasplatform.ygsj.mashgasmonitoring.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 图表工具:参考天气的进行封装:使用的时候相对比较方便;
 * Created by yangle on 2016/11/29.
 */
public class ChartUtils {

    public static int dayValue = 0;
    public static int weekValue = 1;
    public static int monthValue = 2;


    /**
     * 初始化图表
     *
     * @param chart 原始图表:设置缩放,图例,x/y轴等;
     * @return 初始化后的图表
     */
    public static LineChart initChart(Context context,LineChart chart,int xPoints) {//xPoints为x轴显示几个坐标;

        // 不可数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 可以缩放
        chart.setScaleEnabled(true);//可放大;
        chart.setPinchZoom(true);//缩小;
        chart.setTouchEnabled(true);//可点击;
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 显示图例
        Legend legend = chart.getLegend();//甚至图例;
        legend.setForm(Legend.LegendForm.LINE);//LegendForm枚举类,控制图样的形状与大小;
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(12);
        legend.setDrawInside(true);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-20);
        //chart.setScaleY(0.8f);

      // chart.moveViewTo(-0.1f,0, YAxis.AxisDependency.LEFT);
        //设置弹出显示;
       /* MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart*/

        XAxis xAxis = chart.getXAxis();
        // 显示x轴
        xAxis.setDrawAxisLine(true);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(-12);//x轴向上飘
        xAxis.setAvoidFirstLastClipping(true);
        // set a custom value formatter
        xAxis.setLabelRotationAngle(50);//x轴旋转角度
        xAxis.setLabelCount(xPoints,true);//设置显示个数;

        //重新绘制量x轴量程;
       // chart.setScaleMinima(1.0f, 1.0f);
        //chart.getViewPortHandler().refresh(new Matrix(), chart, true);


        YAxis yAxis = chart.getAxisLeft();
        // 显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(25);//y轴向右飘

        yAxis.setAxisMinimum(-0.1f);
       // chart.setExtraLeftOffset(0.2f);



       /* Matrix matrix = new Matrix();
         //x轴缩放1.5倍
        matrix.postScale(1.5f, 1f);
       //  在图表动画显示之前进行缩放
        chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        chart.animateX(2000);*/
        //chart.invalidate();
        return chart;
    }

    /**
     * 设置曲线:曲线颜色,坐标点的大小颜色位置,点击后的弹出框
     *
     * @param chart  图表
     * @param values 数据
     */
    public static  LineDataSet setChartData(Context context,LineChart chart, List<Entry> values,String label,LineDataSet.Mode modle) {
        LineDataSet lineDataSet;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(values, label);
            // 设置曲线颜色
            lineDataSet.setColor(Color.parseColor("#FFFFFF"));
            // 设置平滑曲线
            lineDataSet.setMode(modle);
            // 显示坐标点的小圆点
            lineDataSet.setDrawCircles(true);
            lineDataSet.setValueTextSize(12);
            lineDataSet.setValueTextColor(Color.WHITE);
            // 显示坐标点的数据
            lineDataSet.setDrawValues(true);
            // 不显示定位线
            //设置highlight
            lineDataSet.setHighlightEnabled(true);
            lineDataSet.setDrawHighlightIndicators(true);
            lineDataSet.setHighLightColor(Color.YELLOW);

            LineData data = new LineData(lineDataSet);
            //chart.setScaleMinima(1.0f, 1.0f);
           // chart.getViewPortHandler().refresh(new Matrix(), chart, true);
            chart.setData(data);
            chart.invalidate();
        }

            MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view,chart);
            mv.setChartView(chart); // For bounds control
            chart.setMarker(mv); // Set the marker to the chart

        //设置弹出显示;

        return lineDataSet;
    }

    /**
     * 更新图表
     *1,设置x轴的格式,2,调用曲线的样式;
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
    public static void notifyDataSetChanged(Context context, final LineChart chart, List<Entry> values,
                                            final int valueType, String label, final List<String> format_realtimes,
                                            LineDataSet.Mode mode) {
        /*chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValuesProcess(valueType)[(int) value];
            }
        });*/

        //设置x轴 :如果是0的情况需要区分;value一开始并不强转成0,而是对应成小数,如果是小数应该做相应的处理;

        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;

             //  Log.e("index&size","index"+value+" size"+format_realtimes.size());
                // if (index > 3) //:因为刷新是在拿到的数组内进行循环,%4的时候会返回到这个数组较小的元素,而不是像数组自动增加,自动减掉头;
                //  Log.e("index%4", "index:"+index + " index%4: "+ index%4+"-->"+format_realtimes.get(index%4) );
                //value小于0或者index>=format_realtimes.size()会出现数组越界的情况;
                //由于float强转,强转成int的,负值会转成0;所以条件去掉index<0
               // if( (index <=4 && index >= format_realtimes.size())){//< = 4的时候防止出现数组越界;
                    if( index< 0 || index >= format_realtimes.size()){//防止出现数组越界;
                    return "";
                }/*else if (index < 4 ){
                    return format_realtimes.get(index);
                }else {
                    return  format_realtimes.get(index % 4);
                }*/
                else {
                    //  Log.e("index&&value", "value: "+ value +" index"+index+" time"+format_realtimes.get(index));
                    if (index >= format_realtimes.size()) return "";
                        return format_realtimes.get(index);
                }
               //return "";
            }
        });

        chart.invalidate();
        setChartData(context,chart, values,label,mode);
    }

    /**
     * gehj
     * x轴数据处理
     *
     * @return x轴数据
     */

    /*private  static String setXValue(String inputTime){
        String xTime = com.gasplatform.ygsj.mashgasmonitoring.utils.TimeUtils.chartTimeFarmat(inputTime);

        return xTime;
    }*/

    /**
     * 以下是源demo对,x轴数据处理,因为项目是动态变化,所以用不到,以后扩展可以尝试使用;
     *
     * @param valueType 数据类型
     * @return x轴数据
     */
    private static String[] xValuesProcess(int valueType) {
       // String[] week = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        //传入8个点
        if (valueType == dayValue) { // 日:时分秒
            /*String[] dayValues = new String[7];
            long currentTime = System.currentTimeMillis();
            for (int i = 6; i >= 0; i--) {
                dayValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_day);
                currentTime -= (3 * 60 * 60 * 1000);
            }
            return dayValues;*/

        } else if (valueType == weekValue) { // 周:日,时
           /* String[] weekValues = new String[7];
            Calendar calendar = Calendar.getInstance();
            int currentWeek = calendar.get(Calendar.DAY_OF_WEEK);

            for (int i = 6; i >= 0; i--) {
                weekValues[i] = week[currentWeek - 1];
                if (currentWeek == 1) {
                    currentWeek = 7;
                } else {
                    currentWeek -= 1;
                }
            }
            return weekValues;*/

        } else if (valueType == monthValue) { // 年:月,日,时;
            /*String[] monthValues = new String[7];
            long currentTime = System.currentTimeMillis();
            for (int i = 6; i >= 0; i--) {
                monthValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_month);
                currentTime -= (4 * 24 * 60 * 60 * 1000);
            }
            return monthValues;*/
        }
           return new String[]{};
    }
}
