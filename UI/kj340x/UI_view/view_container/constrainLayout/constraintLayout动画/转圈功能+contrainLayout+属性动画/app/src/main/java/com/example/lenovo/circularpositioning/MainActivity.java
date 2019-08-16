package com.example.lenovo.circularpositioning;

import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView earthImage = findViewById(R.id.earth_image);
        ImageView marsImage = findViewById(R.id.mars_image);
        ImageView saturnImage = findViewById(R.id.saturn_image);

        ValueAnimator earthAnimator = animatePlanet(earthImage, TimeUnit.SECONDS.toMillis(2));
        ValueAnimator marsAnimator = animatePlanet(marsImage, TimeUnit.SECONDS.toMillis(6));
        ValueAnimator saturnAnimator = animatePlanet(saturnImage, TimeUnit.SECONDS.toMillis(12));

        earthAnimator.start();
        marsAnimator.start();
        saturnAnimator.start();

    }

    private ValueAnimator animatePlanet(final ImageView planet, long orbitDuration) {//走的还是属性动画
        ValueAnimator anim = ValueAnimator.ofInt(0, 359);//// 从某个值变化到某个值
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {// 启动动画之后, 会不断回调此方法来获取最新的值
                int val = (int) valueAnimator.getAnimatedValue();// 获取最新的高度值
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) planet.getLayoutParams();//获取控件
                layoutParams.circleAngle = val;//最新变化的高度给了其角度值
                planet.setLayoutParams(layoutParams);//重新设置角度值;

            }
        });

        anim.setDuration(orbitDuration);//周期
        anim.setInterpolator(new LinearInterpolator());//动画的变化速率:线性
        anim.setRepeatMode(ValueAnimator.RESTART);//回头
        anim.setRepeatCount(ValueAnimator.INFINITE);//无限
        return anim;
    }
}
