 requestWindowFeature(Window.FEATURE_NO_TITLE);//对话框样式的activity去掉标题栏；

//在Activity对应的java文件中设置窗口的宽高与位置
getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
getWindow().setGravity(Gravity.CENTER);

setFinishOnTouchOutside(true);//在Activity中设置点击区域外消失属性

//开启动画
mDialogRotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotating_once);
 mCloseBtn.startAnimation(mDialogRotateAnim);
