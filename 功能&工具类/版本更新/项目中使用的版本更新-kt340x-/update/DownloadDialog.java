package com.gasplatform.ygsj.mashgasmonitoring.activity.update;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gasplatform.ygsj.mashgasmonitoring.R;


/**
 * Created by Administrator on 2016/11/8.
 */

public class DownloadDialog extends AlertDialog {
    /**
     * 设置点击ok的接口
     *
     * @author Administrator
     */

    Context context;
    Button btn_download, btn_cancel;
   /*----------设置回调接口-------------*/
    private Clic_download clic_download;
    private Clic_cancel clic_cancel;
    public interface Clic_download {
        public void onclick_download();
    }

    public interface Clic_cancel {
        public void onclick_cancel();
    }

    public void setClic_download(Clic_download clic_download) {
        this.clic_download = clic_download;
    }

    public void setClic_cancel(Clic_cancel clic_cancel) {
        this.clic_cancel = clic_cancel;
    }
 /*----------设置回调接口--end-------------*/

    public DownloadDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pop_update);
        initview();
        initEvent();
    }

    private void initEvent() {
        btn_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clic_download.onclick_download();//接口传递事件;
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clic_cancel.onclick_cancel();
            }
        });

    }

    private void initview() {
        btn_download = (Button) findViewById(R.id.btn_download);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }



}

