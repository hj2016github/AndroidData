final int GOTO_READ = 0 ;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == GOTO_READ) {
				showDialog();
			}
		};
	};
	
	
	public void onClick(View view) {
		handler.removeMessages(GOTO_READ);//在1s内连续点击会触发这个方法;
		Message msg = Message.obtain();
		msg.what = GOTO_READ;
		handler.sendMessageDelayed(msg, 1000);
	}