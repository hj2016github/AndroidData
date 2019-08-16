package lab.sodino.statelist;  
  
import android.app.Activity;  
import android.content.Context;  
import android.content.res.ColorStateList;  
import android.graphics.drawable.Drawable;  
import android.graphics.drawable.StateListDrawable;  
import android.os.Bundle;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.TextView;  
  
/** 
 * 对TextView设置ColorStateList使其在Normal、Pressed、Focused、Unable四种状态下显示不同的颜色。<br/> 
 * StateListDrawable可直接使用图片应用在相似场合。 
 */  
public class ActColorStateList extends Activity implements OnClickListener {  
    private TextView txtShow;  
  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        txtShow = (TextView) findViewById(R.id.txtShow);  
        txtShow.setText("Sodino\nNormal:0xffffffff\nPressed:0xffffff00\nFocused:0xff0000ff\nUnable:0xffff0000");  
        txtShow.setTextColor(createColorStateList(0xffffffff, 0xffffff00, 0xff0000ff, 0xffff0000));  
        txtShow.setOnClickListener(this);  
    }  
  
    /** 对TextView设置不同状态时其文字颜色。 */  
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {  
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };  
        int[][] states = new int[6][];  
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };  
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };  
        states[2] = new int[] { android.R.attr.state_enabled };  
        states[3] = new int[] { android.R.attr.state_focused };  
        states[4] = new int[] { android.R.attr.state_window_focused };  
        states[5] = new int[] {};  
        ColorStateList colorList = new ColorStateList(states, colors);  
        return colorList;  
    }  
  
    /** 设置Selector。 */  
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,  
            int idUnable) {  
        StateListDrawable bg = new StateListDrawable();  
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);  
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);  
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);  
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);  
        // View.PRESSED_ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);  
        // View.ENABLED_FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);  
        // View.ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);  
        // View.FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_focused }, focused);  
        // View.WINDOW_FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);  
        // View.EMPTY_STATE_SET  
        bg.addState(new int[] {}, normal);  
        return bg;  
    }  
  
    @Override  
    public void onClick(View v) {  
        if (v == txtShow) {  
            txtShow.setEnabled(false);  
        }  
    }  
}  