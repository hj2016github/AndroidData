package com.sangfor.newxs66.updata_apk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sangfor.newxs66.R;

/**
 * 版本更新提示框
 */
public class MyDialog extends AlertDialog {
	/**
	 * 设置点击ok的接口
	 * @author Administrator
	 *
	 */
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	public interface Clic_ok {
		public void onclick_ok();
	}
	public interface Clic_no {
		public void onclick_no();
	}
	private Clic_ok clic_ok;
	public void setClic_ok(Clic_ok clic_ok) {
		this.clic_ok = clic_ok;
	}

	public void setClic_no(Clic_no clic_no) {
		this.clic_no = clic_no;
	}

	private Clic_no clic_no;
	Context context;
	TextView title_dialog,content_dialog,btn_name_yes,btn_name_no;
	TextView btn_ok,btn_no;
	public MyDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_dialog);
		initview();
		initEvent();

	}

	private void initEvent() {



		// TODO Auto-generated method stub
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clic_ok.onclick_ok();


			}
		});
		btn_no.setOnClickListener(new View.OnClickListener() {



			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clic_no.onclick_no();

				SharedPreferences preferences = context
						.getSharedPreferences(SHAREDPREFERENCES_NAME,
								Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				// 存入数据
				editor.putBoolean("isFirstIn", true);
				// 提交修改
				editor.commit();
			}
		});

	}

	private void initview() {
		// TODO Auto-generated method stub
		btn_ok = (TextView) findViewById(R.id.btn_ok);
		btn_no = (TextView) findViewById(R.id.btn_no);
		title_dialog = (TextView) findViewById(R.id.title_dialog);
		content_dialog = (TextView) findViewById(R.id.content_dialog);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		title_dialog.setText(title);
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		content_dialog.setText(message);
	}


}
