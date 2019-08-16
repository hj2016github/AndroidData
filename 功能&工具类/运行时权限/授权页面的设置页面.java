  private void getAppDetailSettingIntent(Context context) {//��Ȩҳ�������ҳ��;
        //vivo�ֻ��Ĵ���:
        Intent appIntent  = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent!=null) context.startActivity(appIntent);

        // oppo �ֻ��Ĵ���:
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent!=null) context.startActivity(appIntent);

        //��ͨ�ֻ�,ͨ����̬intent��action,�����������ҳ��;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//������ջ;
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