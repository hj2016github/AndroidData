
//使用
SnackbarUtil.ShortSnackbar(coordinator,"妹子向你发来一条消息",SnackbarUtil.Info).show();

Snackbar snackbar= SnackbarUtil.ShortSnackbar(coordinator,"妹子删了你发出的消息",SnackbarUtil.Warning).setActionTextColor(Color.RED).setAction("再次发送", new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SnackbarUtil.LongSnackbar(coordinator,"妹子已将你拉黑",SnackbarUtil.Alert).setActionTextColor(Color.WHITE).show();
    }
 });

 SnackbarUtil.SnackbarAddView(snackbar,R.layout.snackbar_addview,0);

 SnackbarUtil.SnackbarAddView(snackbar,R.layout.snackbar_addview2,2);

  snackbar.show();

