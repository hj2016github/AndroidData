

/**
     * ����Ƿ����ӵ�������?
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
     * �ж��Ƿ�����û����֪ͨ�������ʾ
     */
    public void checkFinish() {
        if (!checkWifi()) {
            lay01.setVisibility(View.VISIBLE);
            // ����builder
            AlertDialog.Builder builders = new AlertDialog.Builder(
                    IntranetActivity.this);
            builders.setTitle("��Ǹ����������ʧ�ܣ��Ƿ������������");
            builders.setPositiveButton("ȷ��",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // ���������������ý���
                            IntranetActivity.this.startActivity(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS));
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            startActivity(new
                                    Intent(Settings.ACTION_WIFI_SETTINGS));
                            // //�����ֻ��е�wifi�������ý���
                            finish();
                        }
                    });
            builders.setNegativeButton("ȡ��",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // �رյ�ǰactivity
                            IntranetActivity.this.finish();
                        }
                    });
            builders.setCancelable(false);
            builders.show();

        }
        return;
    }
	
	
	
	/**
     * ����Ƿ����ӵ�������
     */
    public boolean  isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()){// ��ǰ���������ӵ�
            if (info.getState() == NetworkInfo.State.CONNECTED){ // ��ǰ�����ӵ��������
                return true;
            }
        }
        return false;
    }
	
	
	
   public void onReceive(Context context, Intent intent) {
        Boolean isConnect =  isNetworkAvailable(context);
        if (!isConnect){
            //Snackbar��ʹ��
            //LayoutInflater inflater = LayoutInflater.from(context);
           // View view = inflater.inflate(R.layout.activity_main,null);
            //���ϻ�ȡview��������;
           View  view =  ((Activity)context).getWindow().getDecorView();
           // Gravity.TOP��snackbar��������ʾλ��,������textview����ʾλ��;
           SnackbarUtil.locationSnakebar(view,"�������Ӳ�����", Color.WHITE,
                   context.getResources().getColor(R.color.blue), Gravity.TOP,Gravity.CENTER_HORIZONTAL).show();
        }
        //throw new UnsupportedOperationException("Not yet implemented");


    }	