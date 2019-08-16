 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        if (Build.VERSION.SDK_INT >= 21) {
            /*背景延伸到标题栏*/
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            View viewMain = findViewById(R.id.mainView);
           // viewMain.setTranslationY(200);//把整个mainView向下移动
            viewMain.setScrollY(-105);//把mainView的内容向下移动,所以不影响这个mainView的透明背景的延伸;
        }
    }