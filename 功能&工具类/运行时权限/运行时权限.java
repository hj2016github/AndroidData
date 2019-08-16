img_share.setOnClickListener(new View.OnClickListener() {//分享;
            @Override
            public void onClick(View v) {//调出运行时权限;
                if (ContextCompat.checkSelfPermission(ChartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED) {//安装时没有授权的情况下,则运行运行时权限;
                    ActivityCompat.requestPermissions(ChartActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE  },1);//回调运行时权限的对话框; 1为requestCode;
                }else {
                    showShare();//一键分享;
                }

            }
        });


 //运行时权限:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//运行时dialog回调;
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//如果运行时候允许的情况下;点击总是允许;
                    showShare();
                }else {
                    //跳转到系统的授权页面;
                    Toast.makeText(this, "请手添加存储权限", Toast.LENGTH_LONG).show();
                    getAppDetailSettingIntent(ChartActivity.this);
                }
        }
    }

    private void getAppDetailSettingIntent(Context context) {//授权页面的设置
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
}

//一键分享;
    private void showShare() {
        //chart.saveToGallery("实时曲线数据",50);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享图片");
        // titleUrl QQ和QQ空间跳转链接
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        //分享出去的字段

        oks.setImageData(screenShort());//分享图片
        //oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
       //oks.setImagePath(screenShort());//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
       // oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
       // oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }


private   Bitmap bitmap;
    //因系统自带的截屏不好用,采用自己截屏;
    private  Bitmap  screenShort(){
        String imagePath = null;
        View mView = getWindow().getDecorView();
        mView.setDrawingCacheEnabled(true);
        mView.buildDrawingCache();
        bitmap = mView.getDrawingCache();
        if(bitmap!= null){
            String sbCardPath = Environment.getExternalStorageDirectory().getPath();
            imagePath = sbCardPath+ File.separator+"KJ340xRT"+new Date().getTime()+".png";

            File file = new File(imagePath);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,60,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return  bitmap;
    }