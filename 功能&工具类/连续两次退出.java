 /**
     * 连续两次点击后退出程序
     */
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                try {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 5000) { // 如果两次按键时间间隔大于2秒，则不退出
                        Toast.makeText(this, "再按一次退出该系统", Toast.LENGTH_SHORT)
                                .show();
                        firstTime = secondTime;// 更新firstTime
                        return true;
                    } else { // 两次按键小于2秒时
                        finishAffinity();////关闭当前activity所属的activity栈中所有的activity
                    }
                } catch (Exception e) {
                }
                break;
        }
        return true;
    }