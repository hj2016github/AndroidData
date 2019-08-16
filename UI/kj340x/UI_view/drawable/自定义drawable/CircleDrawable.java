
public class CircleDrawable extends Drawable
{
    private int mWidth;
    private Paint mPaint;
    private Bitmap mBitmap;
 
    public CircleDrawable(Bitmap bitmap){
        this.mBitmap=bitmap;
        BitmapShader bitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);//后两个参数表示平铺;
        mPaint=new Paint();
        mPaint.setAntiAlias(true);//抗锯齿,设置为平滑;
        mPaint.setShader(bitmapShader);//设置着色器;
        mWidth=Math.min(mBitmap.getWidth(),mBitmap.getHeight());//得到半径;
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mWidth/2,mWidth/2,mWidth/2,mPaint);
    }
 
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }
 
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }
 
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;//获得系统的透明度
    }
 
    @Override
    public int getIntrinsicHeight() {//返回实际宽高
        return mWidth;
    }
 
    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }
}