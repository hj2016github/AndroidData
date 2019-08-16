package com.gehj.fastec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gehj.general_core.Latte;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Latte.init(this).withActivity(this).configure(); //类似于build模式
    }
}
