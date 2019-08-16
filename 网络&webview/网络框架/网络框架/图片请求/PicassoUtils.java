package com.soyikeji.work.work.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.soyikeji.work.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * Created by lenovo on 2016/8/5.
 */
public class PicassoUtils {
    public static void getPicFromPicasso(String url, ImageView imageView, Context context) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.sy_03)
                .error(R.mipmap.sy_03)
                .resize(700, 700)
                .config(Bitmap.Config.RGB_565)
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Bitmap bitmap_transform = toRoundCorner(source, 60);//这个参数需要从外部传入;
                        if (bitmap_transform != null) {
                            source.recycle();
                        }
                        return bitmap_transform;
                    }

                    @Override
                    public String key() {
                        //此方法可以忽略
                        return "";
                    }
                })
                .into(imageView);
    }


    //圆角化处理；
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}