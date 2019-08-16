package com.sangfor.newxs66;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SFException;
import com.sangfor.ssl.SangforAuth;
import com.sangfor.ssl.common.VpnCommon;
import com.sangfor.newxs66.tool.FileDownLoad.FileUtils;
import com.sangfor.newxs66.tool.OkHttpUtils.OkHttpUtils;
import com.sangfor.newxs66.tool.OpenFile.OpenFile;
import com.sangfor.newxs66.tool.UrlUtils;
import com.sangfor.newxs66.updata_apk.UpdateAppManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/*
 * android SDK　DEMO说明，内网资源配置, 接口说明，使用过程中出现的常见问题等，请参考
 * 同文件夹目录下的说明文档。
 */
public class VpnActivity extends Activity implements View.OnClickListener, IVpnDelegate {
    private static final String TAG = "SFSDK_" + VpnActivity.class.getSimpleName();

    // 认证所需信息
    private static final String VPN_IP = "218.26.97.15"; // VPN设备地址　（也可以使用域名访问）
    private static final int VPN_PORT = 443; // vpn设备端口号，一般为443
    // 用户名密码认证；用户名和密码
    private static final String USER = "OA手机app";
    private static final String PASSWD = "sooyie123";
    //    private static final String USER = "测试索1";
//    private static final String PASSWD = "sygs123";
    // 证书认证；导入证书路径和证书密码（如果服务端没有设置证书认证此处可以不设置）
    private static final String CERT_PATH = "/sdcard/daib1.pfx";
    private static final String CERT_PASS = "neteye";

    // 测试内网服务器地址。（在vpn服务器上，配置的内网资源）
    private static final String HTTP_RES = Connector.OA_Url;


    private InetAddress m_iAddr = null;
    private ImageView image1;
    // View
    private RememberEditText edt_ip = null;
    private RememberEditText edt_user = null;
    private RememberEditText edt_passwd = null;
    private RememberEditText edt_certName = null;
    private RememberEditText edt_certPasswd = null;
    private RememberEditText edt_sms = null;
    private RememberEditText edt_challenge = null;
    private RememberEditText edt_url = null;
    private WebView webView = null;
    private final int TEST_URL_TIMEOUT_MILLIS = 8 * 1000;// 测试vpn资源的超时时间
    private LinearLayout lay01;//连接网络失败显示界面
    private UpdateAppManager updateManager;// 自主更新判断
    public static String filePath;
    public File file;
    private ProgressDialog pd;
    private ProgressBar bar;

    /**
     * 检查是否连接到了网络?
     */
    public boolean checkWifi() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        if (wifi | internet) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有网没网后，通知后给出提示
     */
    public void checkFinish() {
        if (!checkWifi()) {
            lay01.setVisibility(View.VISIBLE);
            // 创建builder
            AlertDialog.Builder builders = new AlertDialog.Builder(
                    VpnActivity.this);
            builders.setTitle("抱歉，网络连接失败，是否进行网络设置");
            builders.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 进入无线网络配置界面
                            VpnActivity.this.startActivity(new Intent(
                                    Settings.ACTION_WIRELESS_SETTINGS));
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            startActivity(new
                                    Intent(Settings.ACTION_WIFI_SETTINGS));
                            // //进入手机中的wifi网络设置界面
                            finish();
                        }
                    });
            builders.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // 关闭当前activity
                            VpnActivity.this.finish();
                        }
                    });
            builders.setCancelable(false);
            builders.show();

        }
        return;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        try {
            com.sangfor.ssl.service.utils.logger.Log.init(getApplicationContext());
            com.sangfor.ssl.service.utils.logger.Log.LEVEL = com.sangfor.ssl.service.utils.logger.Log.DEBUG;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vpn);

        initView();
        image1 = (ImageView) findViewById(R.id.image1);
        new Thread(new MyThread()).start();
        lay01 = (LinearLayout) findViewById(R.id.lay01);

        checkFinish();
        updateManager = new UpdateAppManager(this);
        updateManager.checkUpdateInfo(null);

//		// android 6.0 动态权限申请
//		if (shouldRequestPermissions()) {
//			showRequestPermissionsReasonDialog();
//			return;// 直接返回，后续操作在onRequestPermissionsResult这个函数中继续
//		} else {
//			// 已经有权限了，进行后面的操作
//		}

        SangforAuth sfAuth = SangforAuth.getInstance();
        try {
            // SDK模式初始化，easyapp 模式或者是l3vpn模式，两种模式区别请参考文档。
            sfAuth.init(this, this, SangforAuth.AUTH_MODULE_EASYAPP);
//    		sfAuth.init(this, this, SangforAuth.AUTH_MODULE_L3VPN);
            sfAuth.setLoginParam(AUTH_CONNECT_TIME_OUT, String.valueOf(5));
        } catch (SFException e) {
            e.printStackTrace();
        }

    }

    /**
     * 睡眠3秒同时进行网络请求
     */

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            switch (msg.what) {
                case 1:
                    image1.setVisibility(View.GONE);
                    LoadPageByWebView(HTTP_RES);
                    webView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    pd.dismiss();
                    break;
            }
        }
    };


    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);// 发送消息
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        SangforAuth sfAuth = SangforAuth.getInstance();
        if (sfAuth != null) {
            //从其它界面回到这个界面时，需重新设置代理，让vpn把回调发送到当前Activity的vpnCallback里面来
            sfAuth.setDelegate(this);
            Log.i(TAG, "set delegate :" + TAG);
        }
        initSslVpn();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView = null;
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initView() {
        edt_ip = RememberEditText.bind(this, R.id.edt_ip, "IP");
        edt_user = RememberEditText.bind(this, R.id.edt_user, "USER");
        edt_passwd = RememberEditText.bind(this, R.id.edt_passwd, "PASSWD");
        edt_certName = RememberEditText.bind(this, R.id.edt_certName, "CERTNAME");
        edt_certPasswd = RememberEditText.bind(this, R.id.edt_certPasswd, "CERTPASSWD");
        edt_sms = RememberEditText.bind(this, R.id.edt_sms, "SMS");
        edt_challenge = RememberEditText.bind(this, R.id.edt_challenge, "CHALLENGE");
        edt_url = RememberEditText.bind(this, R.id.edt_url, "URL");
        edt_ip.setText(VPN_IP);
        edt_user.setText(USER);
        edt_passwd.setText(PASSWD);
        edt_url.setText(HTTP_RES);
        edt_certName.setText(CERT_PATH);
        edt_certPasswd.setText(CERT_PASS);
        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                initSslVpn();
                break;
            case R.id.btn_logout:
                //SangforAuth.getInstance().vpnLogout();
                Intent intent = new Intent(this, LogoutActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_cancel:
                SangforAuth.getInstance().vpnCancelLogin();
                break;
            case R.id.btn_send_sms:
                doVpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
                break;
            case R.id.btn_send_challenge:
                doVpnLogin(IVpnDelegate.AUTH_TYPE_RADIUS);
                break;
            case R.id.btn_reget_sms:
                doVpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
                break;
            case R.id.btn_test_http:
                String url = edt_url.getText().toString();
                Log.i(TAG, "user set test url do not have Protocol header, add http://");
                if (!url.contains("http")) {
                    edt_url.setText("http://" + url);
                }
                new TestThread().start();// 将测试结果写到logcat中去，需要分析日志
//			 new TestHttpsThread().start();//这个是测试下载文件
                loadPage();// 将测试结果展示到界面上，直观展示
                break;
            default:
                Log.w(TAG, "onClick no process");
        }
    }

    private void loadPage() {
        LoadPageByWebView(edt_url.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * 开始初始化VPN，该初始化为异步接口，后续动作通过回调函数vpncallback通知结果
     *
     * @return 成功返回true，失败返回false，一般情况下返回true
     */
    private boolean initSslVpn() {
        SangforAuth sfAuth = SangforAuth.getInstance();

        m_iAddr = null;
        final String ip = edt_ip.getText().toString().trim();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    m_iAddr = InetAddress.getByName(ip);
                    Log.i(TAG, "ip Addr is : " + m_iAddr.getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (m_iAddr == null || m_iAddr.getHostAddress() == null) {
            Log.d(TAG, "vpn host error");
            return false;
        }
        long host = VpnCommon.ipToLong(m_iAddr.getHostAddress());
        int port = VPN_PORT;

        if (sfAuth.vpnInit(host, port) == false) {
            Log.d(TAG, "vpn init fail, errno is " + sfAuth.vpnGeterr());
            return false;
        }
        return true;
    }

    /**
     * 处理认证，通过传入认证类型（需要的话可以改变该接口传入一个hashmap的参数用户传入认证参数）.
     * <p>
     * 也可以一次性把认证参数设入，这样就如果认证参数全满足的话就可以一次性认证通过，可见下面屏蔽代码
     *
     * @param authType 认证类型
     * @throws SFException
     */

    private void doVpnLogin(int authType) {
        Log.d(TAG, "doVpnLogin authType " + authType);

        boolean ret = false;
        SangforAuth sfAuth = SangforAuth.getInstance();

		/*
        // session共享登陆：主APP封装时走原认证流程，子APP认证时使用TWFID（SessionId）认证方式
		boolean isMainApp = true;
		//子APP,isMainApp = false;
		if(!isMainApp){
			authType = IVpnDelegate.AUTH_TYPE_TWFID;
		}
		*/
        switch (authType) {
            case IVpnDelegate.AUTH_TYPE_CERTIFICATE:
                String certPasswd = edt_certPasswd.getText().toString();
                String certName = edt_certName.getText().toString();
                sfAuth.setLoginParam(IVpnDelegate.CERT_PASSWORD, certPasswd);
                sfAuth.setLoginParam(IVpnDelegate.CERT_P12_FILE_NAME, certName);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_CERTIFICATE);
                break;
            case IVpnDelegate.AUTH_TYPE_PASSWORD:
                String user = edt_user.getText().toString();
                String passwd = edt_passwd.getText().toString();
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_USERNAME, user);
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_PASSWORD, passwd);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS:
                // 进行短信认证
                String smsCode = edt_sms.getText().toString();
                sfAuth.setLoginParam(IVpnDelegate.SMS_AUTH_CODE, smsCode);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS1:
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
                break;

            case IVpnDelegate.AUTH_TYPE_TOKEN:
                String token = "123321";
                sfAuth.setLoginParam(IVpnDelegate.TOKEN_AUTH_CODE, token);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TOKEN);
                break;
            case IVpnDelegate.AUTH_TYPE_RADIUS:
                //进行挑战认证
                String challenge = edt_challenge.getText().toString();
                sfAuth.setLoginParam(IVpnDelegate.CHALLENGE_AUTH_REPLY, challenge);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_RADIUS);
                break;
            case IVpnDelegate.AUTH_TYPE_TWFID:
                // session共享登陆--子APP登陆：子APP使用TWFID登陆。这两个APP就共享VPN隧道，占用同一个授权。
                String twfid = "11438E3C617095C50D28BA337133872730CBAB0D64F98B53F5105221B9D937E8";
                if (twfid != null && !twfid.equals("")) {
                    Log.i(TAG, "do TWFID Auth, TwfId:" + twfid);
                    sfAuth.setLoginParam(IVpnDelegate.TWF_AUTH_TWFID, twfid);
                    ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TWFID);
                } else {
                    Log.e(TAG, "You hasn't written TwfId");
                    Toast.makeText(this, "You hasn't written TwfId", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.w(TAG, "default authType " + authType);
                break;
        }
        if (ret == true) {
            Log.i(TAG, "success to call login method");
        } else {
            Log.i(TAG, "fail to call login method");
        }
    }

    @Override
    /*
     * l3vpn模式（SangforAuth.AUTH_MODULE_L3VPN）必须重写这个函数：
	 * 注意：当前Activity的launchMode不能设置为 singleInstance，否则L3VPN服务启动会失败。
	 * 原因：
	 * L3VPN模式需要通过startActivityForResult向系统申请使用L3VPN权限，{@link VpnService#prepare}
	 * 但startActivityForResult有限制：
	 * You cannot use startActivityForResult() if the activity being started is not running
	 * in the same task as the activity that starts it.
	 * This means that neither of the activities can have launchMode="singleInstance"
	 * 也就是说当前Activity的launchMode不能设置为 singleInstance
	 *
	 *
	 *EASYAPP模式 (SangforAuth.AUTH_MODULE_EASYAPP）： 请忽略。
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SangforAuth.getInstance().onActivityResult(requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /*
     * VPN 初始化和认证过程的回调结果通知
     */
    @Override
    public void vpnCallback(int vpnResult, int authType) {
        SangforAuth sfAuth = SangforAuth.getInstance();

        switch (vpnResult) {
            case IVpnDelegate.RESULT_VPN_INIT_FAIL:
                /**
                 * 初始化vpn失败
                 */

                break;

            case IVpnDelegate.RESULT_VPN_INIT_SUCCESS:
                /**
                 * 初始化vpn成功，接下来就需要开始认证工作了
                 */
                Log.i("eeee", "加载中");
                LoadPageByWebView(HTTP_RES);
                //设置后台不自动登陆,true为off,即取消自动登陆.默认为false,后台自动登陆.
                // 	sfAuth.setLoginParam(AUTO_LOGIN_OFF_KEY, "true");
                // 初始化成功，进行认证操作　（此处采用“用户名密码”认证）
                doVpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);

                break;

            case IVpnDelegate.RESULT_VPN_AUTH_FAIL:
                /**
                 * 认证失败，有可能是传入参数有误，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                String errString = sfAuth.vpnGeterr();

                break;

            case IVpnDelegate.RESULT_VPN_AUTH_SUCCESS:
                /**
                 * 认证成功，认证成功有两种情况，一种是认证通过，可以使用sslvpn功能了，
                 *
                 * 另一种是 前一个认证（如：用户名密码认证）通过，但需要继续认证（如：需要继续证书认证）
                 */
                if (authType == IVpnDelegate.AUTH_TYPE_NONE) {

				/*
                // session共享登陆--主APP保存：认证成功 保存TWFID（SessionId），供子APP使用
				String twfid = sfAuth.getTwfid();
				Log.i(TAG, "twfid = "+twfid);
				*/


                    LoadPageByWebView(HTTP_RES);
                    // 若为L3vpn流程，认证成功后会自动开启l3vpn服务，需等l3vpn服务开启完成后再访问资源
                    if (SangforAuth.getInstance().getModuleUsed() == SangforAuth.AUTH_MODULE_EASYAPP) {
                        // EasyApp流程，认证流程结束，可访问资源。
                        doResourceRequest();
                    }
                } else if (authType == IVpnDelegate.VPN_TUNNEL_OK) {
                    // l3vpn流程，l3vpn服务通道建立成功，可访问资源
                    doResourceRequest();
                } else {
                    if (authType == IVpnDelegate.AUTH_TYPE_SMS) {
                        // 下一次认证为短信认证，获取相关的信息
                        String phoneNum = SangforAuth.getInstance().getSmsPhoneNum();
                        String countDown = SangforAuth.getInstance().getSmsCountDown();
                        String toastStrsg = "sms code send to [" + phoneNum + "]\n"
                                + "reget code count down [" + countDown + "]\n";
                        Toast.makeText(this, toastStrsg, Toast.LENGTH_SHORT).show();
                    } else if (authType == IVpnDelegate.AUTH_TYPE_RADIUS) {
                        Toast.makeText(this, "start radius challenge auth", Toast.LENGTH_SHORT).show();
                    } else {
                        doVpnLogin(authType);
                    }
                }
                break;
            case IVpnDelegate.RESULT_VPN_AUTH_CANCEL:

                break;
            case IVpnDelegate.RESULT_VPN_AUTH_LOGOUT:
                /**
                 * 主动注销（自己主动调用logout接口）
                 */
                Log.i(TAG, "RESULT_VPN_AUTH_LOGOUT");
                displayToast("RESULT_VPN_AUTH_LOGOUT");
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_FAIL:
                /**
                 * L3vpn启动失败，有可能是没有l3vpn资源，具体信息可通过sfAuth.vpnGeterr()获取
                 */

                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_SUCCESS:
                /**
                 * L3vpn启动成功
                 */

                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_RELOGIN:
                /**
                 *  L3vpn服务端注销虚拟IP,一般是私有帐号在其他设备同时登录造成的
                 */

                break;
            default:
                /**
                 * 其它情况，不会发生，如果到该分支说明代码逻辑有误
                 */
                Log.i(TAG, "default result, vpn result is " + vpnResult);
                displayToast("default result, vpn result is " + vpnResult);
                break;
        }
    }

    private void doResourceRequest() {
        // 认证结束，可访问资源。
    }

    /**
     * 认证过程若需要图形校验码，则回调通告图形校验码位图，
     *
     * @param data 图形校验码位图
     */
    @Override
    public void vpnRndCodeCallback(byte[] data) {
        Log.d(TAG, "vpnRndCodeCallback data: " + Boolean.toString(data == null));
        if (data != null) {
            Log.i(TAG, "vpnRndCodeCallback RndCo we not support RndCode now");
        }
    }

    @Override
    public void reloginCallback(int status, int result) {
        switch (status) {

            case IVpnDelegate.VPN_START_RELOGIN:

                break;
            case IVpnDelegate.VPN_END_RELOGIN:


                if (result == IVpnDelegate.VPN_RELOGIN_SUCCESS) {

                } else {

                }
                break;
        }

    }

    /**
     * 测试资源，打开浏览器
     */
    private void luanchWebBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            displayToast("Luanch Web Browser is error. " + url);
        }
    }

    /**
     * 测试HTTP/HTTPS资源
     */
    private class TestThread extends Thread {
        @Override
        public void run() {
            SangforAuth sfAuth = SangforAuth.getInstance();
            Log.i(TAG, "vpn status ===================== " + sfAuth.vpnQueryStatus());

            String urlStr = edt_url.getText().toString();
            Log.i(TAG, "url =======" + urlStr);

            try {
                if (!urlStr.isEmpty()) {
                    SentHttpAndHttpsPost(urlStr);
                } else {
                    Log.i(TAG, "url is empty!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试HTTPS资源
     */
    private class TestHttpsThread extends Thread {
        @Override
        public void run() {
            String path = "/sdcard/tmp/Test.apk";
            Log.i("download", path);
            // String url = "https://" + "200.200.75.38" + "/com/" + "EasyConnectPhone.apk";
            String url = HTTP_RES;
            Log.i("aaa", url + 2);
            AsyncTask task = new DownloadApkTask(url, path);
            task.execute((Void) null);
        }
    }

    public static class CrashHandler implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            // 打印未捕获的异常堆栈
            Log.d(TAG, "UnHandledException: ");
            ex.printStackTrace();
        }
    }

    private void SentHttpAndHttpsPost(final String url) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "SentHttpAndHttpsPost url param error");
            return;
        }

        try {
            if (!getHostByName(url)) {
                return;
            }
            Log.i(TAG, "test url= " + url);
            if (url.toLowerCase().contains("https://".toLowerCase())) {
                sendHttpsPost(url);
            } else {
                sendHttpPost(url);
            }
        } catch (Exception e) {
            Log.i(TAG, "sendHttpsPost throw exception");
            e.printStackTrace();
        }
        return;
    }

    // 测试dns解析
    public class DNSLookupThread extends Thread {
        private InetAddress addr;
        private String hostname;

        public DNSLookupThread(String hostname) {
            this.hostname = hostname;
        }

        public void run() {
            try {
                InetAddress add = InetAddress.getByName(hostname);
                addr = add;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        public synchronized String getIP() {
            if (null != this.addr) {
                return addr.getHostAddress();
            }
            return null;
        }
    }

    // 设置dns解析超时方法
    public boolean getHostByName(String urlStr) throws Exception {
        if (urlStr == null || urlStr.isEmpty()) {
            return false;
        }
        URL url = new URL(urlStr);
        String ipAddress = url.getHost();

        DNSLookupThread dnsThread = new DNSLookupThread(ipAddress);
        dnsThread.start();
        dnsThread.join(TEST_URL_TIMEOUT_MILLIS);
        String ipStr = dnsThread.getIP();
        if (ipStr == null) {
            Log.e(TAG, "host=" + ipAddress + " DNSLookup failed!");
            return false;
        } else {
            Log.i(TAG, "host=" + ipAddress + " ip=" + ipStr);
            return true;
        }
    }

    class MyX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            // No need to implement.
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            // No need to implement.
        }
    }

    class MyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    @SuppressLint("TrulyRandom")
    private void sendHttpsPost(String url) throws Exception {
        // Create a trust manager that does not validate certificate chains
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "sendHttpsPost url is wrong");
            return;
        }
        TrustManager[] trustAllCerts = new TrustManager[]{new MyX509TrustManager()};
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new MyHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        URL obj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TEST_URL_TIMEOUT_MILLIS); // set timeout to 5 seconds
        conn.setReadTimeout(TEST_URL_TIMEOUT_MILLIS);
        conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        String urlParameters = obj.getQuery();
        if (urlParameters == null) {
            urlParameters = "";
        }
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.i(TAG, "Sending 'POST' request to URL : " + url);
        Log.i(TAG, "Post parameters : " + urlParameters);
        Log.i(TAG, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String result = response.toString();
        Log.i(TAG, result);
    }

    // HTTP POST request
    private void sendHttpPost(String url) throws Exception {
        if (url == null || url.isEmpty()) {
            Log.i(TAG, "sendHttpPost url is wrong");
            return;
        }

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TEST_URL_TIMEOUT_MILLIS);
        conn.setReadTimeout(TEST_URL_TIMEOUT_MILLIS);
        conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        String urlParameters = obj.getQuery();
        if (urlParameters == null) {
            urlParameters = "";
        }

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.i(TAG, "Sending 'POST' request to URL : " + url);
        Log.i(TAG, "Post parameters : " + urlParameters);
        Log.i(TAG, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String result = response.toString();
        Log.i(TAG, result);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPageByWebView(String url) {
        if (url == null || url.equals("")) {
            Log.i(TAG, "LoadPageByWebView url is wrong!");
            return;
        }

        if (webView == null) {
            webView = (WebView) findViewById(R.id.webView);
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        webView.setWebViewClient(new MyWebViewClient(lay01));

        //防止乱码
        webView.loadData(url, "text/html", "utf-8");
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        WebSettings webSettings = webView.getSettings();
        // 不使用缓存，只从网络获取数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        // 设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
        // 网页中包含JavaScript内容需调用以下方法，参数为true

        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                readHtmlFormAssets();

            }
        });


        webView.addJavascriptInterface(new JavascriptInterface(), "Android");
        SharedPreferences preferences = getSharedPreferences("liu", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String name = preferences.getString("uName", null);
        String pass = preferences.getString("uPass", null);
        editor.commit();
        if (name != null && name != "" && pass != null && pass != "") {
            String url1 = url + "txtName=" + name + "&" + "txtPass=" + pass;
            webView.loadUrl(url1);
            Log.i("ceshi","url1:"+url1);
        } else {
            webView.loadUrl(url);
            Log.i("ceshi","url:"+url);
        }
    }

    private void readHtmlFormAssets() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果
        webView.loadUrl("file:///android_asset/errornews.html");
    }

    /**
     * 后台获取用户名密码
     */
    class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void downUrl(String url) {
            final String downUrl = url;
            SharedPreferences preferences = VpnActivity.this.getSharedPreferences("liu", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("downUrl", downUrl);
            editor.commit();
            Log.i("downIp","down2:"+url);
        }
        @android.webkit.JavascriptInterface
        public void login(String txtName, String txtPass) {
            final String name1 = txtName;
            final String pwd1 = txtPass;
            SharedPreferences preferences = VpnActivity.this.getSharedPreferences("liu", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("uName", name1);
            editor.putString("uPass", pwd1);
            editor.commit();
        }


        @android.webkit.JavascriptInterface
        public void clickOnAndroid() {        // 清空账号密码

            SharedPreferences preferences = VpnActivity.this.getSharedPreferences("liu", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("uName");
            editor.remove("uPass");
            editor.commit();

        }

        @android.webkit.JavascriptInterface
        //打电话
        public void CallFunc(String numberStr) {
            String t = "tel:" + numberStr;
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(t));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 下载附件
         *
         * @param url
         */
        @android.webkit.JavascriptInterface
        public void DownLoad(String url) {
            SharedPreferences preferences = getSharedPreferences("liu", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String IPurl = preferences.getString("downUrl", null);
            editor.commit();
            Log.i("downIp","down2:"+IPurl);
//            if (IPurl==null&&IPurl.equals("null")&&IPurl.equals(" null")&&!IPurl.equals("")&&!IPurl.equals("\"\"")){
            if (IPurl==null){
                final String downUrl1 = Connector.FileDownLoadUrl + url;
                Log.i("downUrl1","downUrl1"+downUrl1);
                final String url1 = UrlUtils.encodeUrl(downUrl1);
                filePath = FileUtils.getFilePath(url1);
                file = new File(filePath);
                pd = ProgressDialog.show(VpnActivity.this, "", "加载中，请稍后……",false,false);
                downloadFiles(url1);
            }
            else {
                final String downUrl1 = IPurl+ url;
                Log.i("downUrl1","downUrl2:"+downUrl1);
                final String url1 = UrlUtils.encodeUrl(downUrl1);
                filePath = FileUtils.getFilePath(url1);
                file = new File(filePath);

                pd = ProgressDialog.show(VpnActivity.this, "", "加载中，请稍后……",false,false);
                downloadFiles(url1);

            }

//            }
        }

        public void UpPic() {

            //调用弹出对话框；
//            ReWebChomeClient.setContext(VpnActivity.this);
//            ReWebChomeClient.changeHeadIcon();//弹出对话框进行选择图片或者拍照；
        }
    }

    public void openFile() {

        startActivity(OpenFile.openFile(filePath));
    }


    private Boolean ifDownLoadFinish = false;


    public boolean downloadFiles(final String fileUrl) {//参数为下载文件的地址；

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(fileUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    conn.setConnectTimeout(20000);
                    if (fileUrl != null && fileUrl != "" && conn.getResponseCode() == 200) {
                        //fileUrl非空并且连接成功；

                        in = conn.getInputStream();
                        long fileLength = conn.getContentLength();//文件长度；

                        //保存下载文件到指定文件夹中；
                        FileUtils.CreatDir();
                        String FilePath = FileUtils.getFilePath(fileUrl);//file的全路径
                        Log.i("ccccc",FilePath+"FilePath");

                        //以下获得输出流；

                        out = new FileOutputStream(new File(FilePath));
                        //以下从输入流写到输出流写到指定的文件；
                        byte[] buffer = new byte[1024];
                        int len = 0;//每次循环读取的字节数；
                        long readedLength = 0l;//累加需要读取的总的字节数；

                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                            readedLength += len;
                        }
                        if (readedLength >= fileLength) {
                            ifDownLoadFinish = true;//当读取的字节的总数大于文件的长度，
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);// 发送消息
                            openFile();
                            Log.i("ccccc", "成功");

                            //则标志文件下载完，ifDownLoadFinish变为true；

                        }
                        out.flush();

                    } else {
                        ifDownLoadFinish = false;

                        Log.i("cs", "失败");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
        return ifDownLoadFinish;
    }

    //上传文件的方法：
    private void upLoadFiles_test(File file) {
        String url = Connector.UpPicUrl;
        OkHttpUtils.MultipartRequset_file(url, file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i(TAG, "onFailure:" + e.toString());//错误的原因
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(VpnActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.message());
                Log.i(TAG, "onResponse: " + response.body().string());
                Log.i(TAG, "onResponse: " + response.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VpnActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private class MyWebViewClient extends WebViewClient {
        LinearLayout lay01;

        public MyWebViewClient(LinearLayout lay01) {
            this.lay01 = lay01;
        }

        @Override
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.i(TAG, "onPageStarted url = " + url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler,
                    error);//这样会调用到handler.cancel()

//            isPageError = true;
            handler.proceed();// 忽略证书错误
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                try {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    if (secondTime - firstTime > 1000) {
                        firstTime = secondTime;// 更新firstTime
                        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT)
                                .show();
                        return true;

                    } else {

                        System.exit(0);
                    }
                } catch (Exception e) {
                }
                break;
        }
        return true;
    }


////////////////////////////////android 6.0 动态权限 begin//////////////////////////////////
//	private static final int PERMISSION_REQUEST_CODE = 1;
//	private static final String[] ALL_PERMISSIONS_WE_NEED = { Manifest.permission.INTERNET,
//			Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE,
//			Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, };
//
//	private List<String> mPermissionsNotGranted;
//
//	/**
//	 * 是否需要申请权限
//	 *
//	 * @return false表示不需要申请，true表示需要申请
//	 */
//	@SuppressLint("NewApi")
//	private boolean shouldRequestPermissions() {
//		// Android 6.0以下没有运行时权限
//		if (Build.VERSION.SDK_INT < 23) {
//			return false;
//		}
//
//		ApplicationInfo ai = getApplicationInfo();
//		// targetSdkVersion < 23 不会启用运行时权限
//		if (ai.targetSdkVersion < 23) {
//			return false;
//		}
//
//		if (mPermissionsNotGranted == null) {
//			mPermissionsNotGranted = new ArrayList<String>();
//		}
//		mPermissionsNotGranted.clear();
//		for (int i = 0; i < ALL_PERMISSIONS_WE_NEED.length; ++i) {
//			// Your targetSdkVersion needs to be 23 or higher
//			if (checkSelfPermission(ALL_PERMISSIONS_WE_NEED[i]) != PackageManager.PERMISSION_GRANTED) {
//				mPermissionsNotGranted.add(ALL_PERMISSIONS_WE_NEED[i]);
//			}
//		}
//
//		if (mPermissionsNotGranted.isEmpty()) {
//			return false; // 如果已经获得权限，则不必继续申请
//		}
//
//		return true; // 需要申请权限
//	}
//
//	/**
//	 * 通知用户要进行权限申请
//	 */
//	private void showRequestPermissionsReasonDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("需要授权");
//		builder.setMessage("VPN需要相关权限，请在授权页面进行授权");
//		builder.setPositiveButton("申请权限", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				startRequestPermissions();// 开始申请权限
//			}
//		});
//		builder.setCancelable(false);
//		builder.show();
//	}
//
//	@SuppressLint("NewApi")
//	private void startRequestPermissions() {
//		String[] permissions = new String[mPermissionsNotGranted.size()];
//		mPermissionsNotGranted.toArray(permissions);
//		requestPermissions(permissions, PERMISSION_REQUEST_CODE);
//	}
//
//	/**
//	 * 通知用户权限申请失败
//	 */
//	private void showRequestPermissionFailedDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("需要授权").setMessage("VPN申请权限失败，请前往系统设置的“权限”页面进行授权")
//				.setPositiveButton("前往权限页面", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						gotoAppPermissionManageActivity();
//						dialog.dismiss();
//						exitApp();
//					}
//				}).setNegativeButton("退出", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						exitApp();
//					}
//				}).setCancelable(false).show();
//	}
//
//	/**
//	 * 跳转到系统的权限配置页面
//	 */
//	private void gotoAppPermissionManageActivity() {
//		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//		intent.setData(Uri.parse("package:" + getPackageName()));
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);
//	}
//
//	private void exitApp() {
//		System.exit(0);
//	}
//
//	/**
//	 * 权限申请结果回调函数
//	 */
//	@SuppressLint("NewApi")
//	@Override
//	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		switch (requestCode) {
//		case PERMISSION_REQUEST_CODE: {
//			boolean allPermissionsGranted = true;
//			for (int i = 0; i < grantResults.length; ++i) {
//				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//					allPermissionsGranted = false;
//					break;
//				}
//			}
//			if (!allPermissionsGranted) {
//				Toast.makeText(this, "Permission Request Failed", Toast.LENGTH_SHORT).show();
//				showRequestPermissionFailedDialog();
//			} else {
//				Toast.makeText(this, "Permission Request Success", Toast.LENGTH_SHORT).show();
//			}
//			break;
//		}
//		default: {
//			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//			return;
//		}
//
//		}
//	}
////////////////////////////////android 6.0 动态权限 end//////////////////////////////////

}
