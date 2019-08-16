

/**
     * 检查是否连接到了网络?
     */
    public boolean checkWifi() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        if (wifi | internet) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有网没网后，通知后给出提示
     */
    public void checkFinish() {
        if (!checkWifi()) {
            lay01.setVisibility(View.VISIBLE);
            // 创建builder
            AlertDialog.Builder builders = new AlertDialog.Builder(
                    IntranetActivity.this);
            builders.setTitle("抱歉，网络连接失败，是否进行网络设置");
            builders.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 进入无线网络配置界面
                            IntranetActivity.this.startActivity(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS));
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            startActivity(new
                                    Intent(Settings.ACTION_WIFI_SETTINGS));
                            // //进入手机中的wifi网络设置界面
                            finish();
                        }
                    });
            builders.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // 关闭当前activity
                            IntranetActivity.this.finish();
                        }
                    });
            builders.setCancelable(false);
            builders.show();

        }
        return;
    }
	
	
	
	/**
     * 检查是否连接到了网络
     */
    public boolean  isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()){// 当前网络是连接的
            if (info.getState() == NetworkInfo.State.CONNECTED){ // 当前所连接的网络可用
                return true;
            }
        }
        return false;
    }
	
	
	
   public void onReceive(Context context, Intent intent) {
        Boolean isConnect =  isNetworkAvailable(context);
        if (!isConnect){
            //Snackbar的使用
            //LayoutInflater inflater = LayoutInflater.from(context);
           // View view = inflater.inflate(R.layout.activity_main,null);
            //以上获取view方法错误;
           View  view =  ((Activity)context).getWindow().getDecorView();
           // Gravity.TOP是snackbar的整体显示位置,后面是textview的显示位置;
           SnackbarUtil.locationSnakebar(view,"网络连接不可用", Color.WHITE,
                   context.getResources().getColor(R.color.blue), Gravity.TOP,Gravity.CENTER_HORIZONTAL).show();
        }
        //throw new UnsupportedOperationException("Not yet implemented");


    }	