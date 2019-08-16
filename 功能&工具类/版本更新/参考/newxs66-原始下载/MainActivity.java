package com.sangfor.newxs66;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * sp选择
 */
public class MainActivity extends Activity {
    private int id;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
        id = preferences.getInt("start", 1);

        if (id != 1) {
//            setContentView(R.layout.activity_main);
            time();
        } else {
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
            MainActivity.this.finish();
        }
     

    }

    /**
     * 两秒后跳转方法
     */
    private void time() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
                id = preferences.getInt("start", 1);
                switch (id) {
                    case 1:
                        if (flag == false) {
                            startActivity(new Intent(MainActivity.this, Main2Activity.class));
                            MainActivity.this.finish();
                        }

                        break;
                    case 2:
                        if (flag == false) {
                            startActivity(new Intent(MainActivity.this, Main3Activity.class));
                            MainActivity.this.finish();
                        }

                        break;
                }

            }
        };
        timer.schedule(task, 1000);
    }




}

