package com.sangfor.newxs66.tool.FileUpLoad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.webkit.WebChromeClient;

/**
 * ReWebChomeClient
 *
 * @Author KenChung
 */
public class ReWebChomeClient extends WebChromeClient {
    public  static  Activity context;
    public static AlertDialog dlg;
    public static void setContext(Activity context) {
        ReWebChomeClient.context = context;
    }


    /**
     * 上传图片选择
     */

    public  static  void changeHeadIcon() {

        final CharSequence[] items = {"相册", "拍照","取消"};
         dlg = new AlertDialog.Builder(context).setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                /**
                 * 相册上传
                 */
                if (item == 0) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, i);
                    context.startActivityForResult(chooserIntent, 3);

                } if (item==1){
                    /**
                     * 拍照
                     */
                //此方法会由Camera直接产生照片回传给应用程序，但是返回的是压缩图片，显示不清晰
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        context.startActivityForResult(intent2, 2);
                }
                if (item==2){
                    //IntranetActivity.mUploadMessageForAndroid5.onReceiveValue(null);
                }
            }
        }).create();
        dlg.show();
    }





}
