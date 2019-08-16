package com.gasplatform.ygsj.mashgasmonitoring.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gasplatform.ygsj.mashgasmonitoring.R;

/**
 * Created by 赵晨璞 on 2016/5/1.
 */
public class SnackbarUtil {

public static final   int Info = 1;
public static final  int Confirm = 2;
public static final  int Warning = 3;
public static final  int Alert = 4;


public static  int red = 0xfff44336;
public static  int green = 0xff4caf50;
public static  int blue = 0xff2195f3;
public static  int orange = 0xffffc107;

/**
 * 短显示Snackbar，自定义颜色
 * @param view
 * @param message
 * @param messageColor
 * @param backgroundColor
 * @return
 */
public static Snackbar ShortSnackbar(View view, String message, int messageColor, int backgroundColor){
    Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
    setSnackbarColor(snackbar,messageColor,backgroundColor);
    return snackbar;
}

/**
 * 长显示Snackbar，自定义颜色
 * @param view
 * @param message
 * @param messageColor
 * @param backgroundColor
 * @return
 */
public static Snackbar LongSnackbar(View view, String message, int messageColor, int backgroundColor){
    Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_LONG);
    setSnackbarColor(snackbar,messageColor,backgroundColor);
    return snackbar;
}

/**
 * 自定义时常显示Snackbar，自定义颜色
 * @param view
 * @param message
 * @param messageColor
 * @param backgroundColor
 * @return
 */
public static Snackbar IndefiniteSnackbar(View view, String message,int duration,int messageColor, int backgroundColor){
    Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
    setSnackbarColor(snackbar,messageColor,backgroundColor);
    return snackbar;
}

/**
 * 短显示Snackbar，可选预设类型
 * @param view
 * @param message
 * @param type
 * @return
 */
public static Snackbar ShortSnackbar(View view, String message, int type){
    Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
    switchType(snackbar,type);
    return snackbar;
}

    /**
     * 设置Snackbar 背景透明度
     * @param alpha
     * @return
     */
    public static Snackbar alpha(View view, String message,int messageColor, int backgroundColor,float alpha){
        alpha = alpha>=1.0f?1.0f:(alpha<=0.0f?0.0f:alpha);
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar,messageColor,backgroundColor);
        snackbar.getView().setAlpha(alpha);
        return snackbar;
    }

/**
 * 长显示Snackbar，可选预设类型
 * @param view
 * @param message
 * @param type
 * @return
 */
public static Snackbar LongSnackbar(View view, String message,int type){
    Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_LONG);
    switchType(snackbar,type);
    return snackbar;
}

    /**
     * 设置Snackbar显示的位置
     * @param gravity_snack,整体显示位置
     * @paran gravity_textview,textview显示位置
     */
    public static  Snackbar locationSnakebar(View view, String message,int messageColor, int backgroundColor,int gravity_snack,int gravity_textview){
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView)snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setGravity(gravity_textview);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        setSnackbarColor(snackbar,messageColor,backgroundColor);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(snackbar.getView().getLayoutParams().width,snackbar.getView().getLayoutParams().height);
        params.gravity = gravity_snack;
        snackbar.getView().setLayoutParams(params);
        return snackbar;
    }

    /**
     * 设置Snackbar显示的位置,当Snackbar和CoordinatorLayout组合使用的时候
     * @param gravity
     */
    public static  Snackbar gravityCoordinatorLayout(View view, String message,int gravity){
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(snackbar.getView().getLayoutParams().width,snackbar.getView().getLayoutParams().height);
        params.gravity = gravity;
        snackbar.getView().setLayoutParams(params);
        return snackbar;
    }


    /**
 * 自定义时常显示Snackbar，可选预设类型
 * @param view
 * @param message
 * @param type
 * @return
 */
public static Snackbar IndefiniteSnackbar(View view, String message,int duration,int type){
    Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
    switchType(snackbar,type);
    return snackbar;
}

//选择预设类型
private static void switchType(Snackbar snackbar,int type){
    switch (type){
        case Info:
            setSnackbarColor(snackbar,blue);
            break;
        case Confirm:
            setSnackbarColor(snackbar,green);
            break;
        case Warning:
            setSnackbarColor(snackbar,orange);
            break;
        case Alert:
            setSnackbarColor(snackbar, Color.YELLOW,red);
            break;
    }
}

/**
 * 设置Snackbar背景颜色
 * @param snackbar
 * @param backgroundColor
 */
public static void setSnackbarColor(Snackbar snackbar, int backgroundColor) {
    View view = snackbar.getView();
    if(view!=null){
        view.setBackgroundColor(backgroundColor);
    }
}

/**
 * 设置Snackbar文字和背景颜色
 * @param snackbar
 * @param messageColor
 * @param backgroundColor
 */
public static void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
    View view = snackbar.getView();
    if(view!=null){
        view.setBackgroundColor(backgroundColor);
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
    }
}

/**
 * 向Snackbar中添加view
 * @param snackbar
 * @param layoutId
 * @param index 新加布局在Snackbar中的位置
 */
public static void SnackbarAddView( Snackbar snackbar,int layoutId,int index) {
    View snackbarview = snackbar.getView();
    Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout)snackbarview;

    View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId,null);

    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    p.gravity= Gravity.CENTER_VERTICAL;

    snackbarLayout.addView(add_view,index,p);
}

    /**
     * 设置Snackbar布局的圆角半径值及边框颜色及边框宽度
     * @param radius
     * @param strokeWidth
     * @param strokeColor
     * @return
     */
    public Snackbar radius(View view, String message,int radius, int strokeWidth, @ColorInt int strokeColor){
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_INDEFINITE);
        //将要设置给mSnackbar的背景
        GradientDrawable background = getRadiusDrawable(snackbar.getView().getBackground());
        if(background != null){
            radius = radius<=0?12:radius;
            strokeWidth = strokeWidth<=0?1:(strokeWidth>=snackbar.getView().findViewById(R.id.snackbar_text).getPaddingTop()?2:strokeWidth);
            background.setCornerRadius(radius);
            background.setStroke(strokeWidth,strokeColor);
           snackbar.getView().setBackgroundDrawable(background);
        }
        return snackbar;
    }
    /**
     * 通过SnackBar现在的背景,获取其设置圆角值时候所需的GradientDrawable实例
     * @param backgroundOri
     * @return
     */
    private GradientDrawable getRadiusDrawable(Drawable backgroundOri){
        GradientDrawable background = null;
        if(backgroundOri instanceof GradientDrawable){
            background = (GradientDrawable) backgroundOri;
        }else if(backgroundOri instanceof ColorDrawable){
            int backgroundColor = ((ColorDrawable)backgroundOri).getColor();
            background = new GradientDrawable();
            background.setColor(backgroundColor);
        }else {
        }
        return background;
    }
}

