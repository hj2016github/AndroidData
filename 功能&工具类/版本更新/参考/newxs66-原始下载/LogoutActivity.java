package com.sangfor.newxs66;

import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SangforAuth;
import com.sangfor.ssl.service.utils.logger.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LogoutActivity extends Activity implements IVpnDelegate {
	private final static String TAG = LogoutActivity.class.getSimpleName();
	private Button btnLogout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);

		SangforAuth sfAuth = SangforAuth.getInstance();
		if (sfAuth != null) {
			// 取消其它地方的vpnCallback调用，让vpn把回调发送到当前Activity的vpnCallback里面来
			sfAuth.setDelegate(this);
			Log.info(TAG,"Set delegate :"+TAG);
		}

		btnLogout = (Button) findViewById(R.id.btn_logout_test);
		btnLogout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SangforAuth sfAuth = SangforAuth.getInstance();
				
				if (sfAuth.vpnQueryStatus() == VPN_STATUS_AUTH_OK) {
					Log.info("TAG", "Do vpnLogout");
					// vpnLogout是异步接口，在vpnCallback里面等到回调后表示从服务端成功注销用户,
					if(SangforAuth.getInstance().vpnLogout()==false)
					{
						Log.info("TAG","This method should be called in the main thread");
					}
					
				} else {
					Log.info("TAG", "User not logged in, no need to logout");
					displayToast("用户未登陆,无需注销");
					gotoLoginActivity();
				}
			}
		});
	}

	private void displayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	private void gotoLoginActivity(){
		Intent intent = new Intent(this, VpnActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void vpnCallback(int vpnResult, int type) {
		switch (vpnResult) {
		case IVpnDelegate.RESULT_VPN_AUTH_LOGOUT://注销流程只关心这个回调
			/**
			 * 主动注销（自己主动调用logout接口）
			 */
			displayToast("注销VPN成功");
			Log.info(TAG,"VPN logout success!");
			gotoLoginActivity();
			break;
		default:
			/**
			 * other case do not care
			 */
			Log.info(TAG, "other case, do not care, vpn result is " + vpnResult);
			break;
		}
	}

	@Override
	public void reloginCallback(int status, int result) {
		// do nothing
	}

	@Override
	public void vpnRndCodeCallback(byte[] data) {
		// do nothing
	}
}
