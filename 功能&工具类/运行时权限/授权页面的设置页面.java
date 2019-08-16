  private void getAppDetailSettingIntent(Context context) {//授权页面的设置页面;
        //vivo手机的处理:
        Intent appIntent  = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent!=null) context.startActivity(appIntent);

        // oppo 手机的处理:
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent!=null) context.startActivity(appIntent);

        //普通手机,通过静态intent与action,吊起相关设置页面;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启用新栈;
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }