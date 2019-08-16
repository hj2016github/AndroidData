private long exitTime = 0;
    @Override
    public void onBackPressed() {
        // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 将系统当前的时间赋值给exitTime
            exitTime = System.currentTimeMillis();
        } else {
            finishi();
        }
    }