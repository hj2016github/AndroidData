//1.点击后弹出不带action的snackbar
SnackBar snackbar = Snackbar.make(view, "Snack Bar Text", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
//2.带Action的SnackBar				
				Snackbar.make(view, "Snack Bar Text", Snackbar.LENGTH_LONG)
                        .setAction("Go!", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                startActivity(intent);
                            }
                        })
                        .setActionTextColor(Color.BLUE).show()
//修改Snackbar的背景颜色和message字体颜色						
public static void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
    View view = snackbar.getView();//获取Snackbar的view
    if(view!=null){
        view.setBackgroundColor(backgroundColor);//修改view的背景色
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);//获取Snackbar的message控件，修改字体颜色
    }
}
	
 snackbar.dissmiss();
//3.在Snackbar中增加图标    	
public static void SnackbarAddView(Snackbar snackbar,int layoutId,int index) {
    View snackbarview = snackbar.getView();//获取snackbar的View(其实就是SnackbarLayout)

    Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout)snackbarview;//将获取的View转换成SnackbarLayout

    View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId,null);//加载布局文件新建View

    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);//设置新建布局参数

    p.gravity= Gravity.CENTER_VERTICAL;//设置新建布局在Snackbar内垂直居中显示

    snackbarLayout.addView(add_view,index,p);//将新建布局添加进snackbarLayout相应位置
}

//snackbar_addview.xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="center_vertical"
>
<ImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_vertical"
    android:src="@mipmap/ic_launcher"/>
</LinearLayout>
//使用
Snackbar snackbar= Snackbar.make(coordinatorLayout,"这是massage", Snackbar.LENGTH_LONG);
 SnackbarUtil.setSnackbarColor(snackbar,SnackbarUtil.blue);
 SnackbarUtil.SnackbarAddView(snackbar,R.layout.snackbar_addview,0);
snackbar.show();

