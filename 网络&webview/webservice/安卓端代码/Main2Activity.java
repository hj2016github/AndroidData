package com.example.lenovo.testwebservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    private android.widget.TextView textView;
    private android.widget.Button button;
    Handler handler = new Handler(){//主线程接受回信并给tx设置值;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SoapObject result = (SoapObject) msg.obj;
                    textView.setText(result.toString());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //允许使用webervice同时启用网络访问
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.button = (Button) findViewById(R.id.button);
        this.textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRemoteInfo();

            }
        });
    }

    public void getRemoteInfo() {
        //以下远程地址;
        String namespace = "http://WebXml.com.cn/";
        String transUrl = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";//请求服务的地址:asmx就是.net的请求服务;
        String method = "getSupportCity";//调用webservice的方法;服务器前面用注解标注;
        String soapAction = "http://WebXml.com.cn/getSupportCity";//命名空间+方法名;SoapEnvelope.VER12参数可以省去;
        SoapObject rpc = new SoapObject(namespace, method);//SoapObject:请求体==信件;
        rpc.addProperty("byProvinceName", "湖北");//给方法传递参数;

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;// 设置是否调用的是dotNet开发的WebService
        envelope.setOutputSoapObject(rpc);//信封;

        HttpTransportSE transport = new HttpTransportSE(transUrl);//邮寄
        transport.debug=true;
        // 以下调用服务端WebService
        try {
            transport.call(null, envelope);//邮局发信,SoapEnvelope.VER12第一个参数soapAction可以为空;
            if (envelope.getResponse() != null) {
                
                SoapObject response = (SoapObject) envelope.bodyIn;//邮寄回信
               
                SoapObject o = (SoapObject)  response.getProperty(0);//回信发送到主线程;
                Message message = Message.obtain();
                message.what = 0;
                message.obj = o;
                handler.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
}
