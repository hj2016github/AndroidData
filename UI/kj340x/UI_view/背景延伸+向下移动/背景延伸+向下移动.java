 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        if (Build.VERSION.SDK_INT >= 21) {
            /*�������쵽������*/
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            View viewMain = findViewById(R.id.mainView);
           // viewMain.setTranslationY(200);//������mainView�����ƶ�
            viewMain.setScrollY(-105);//��mainView�����������ƶ�,���Բ�Ӱ�����mainView��͸������������;
        }
    }