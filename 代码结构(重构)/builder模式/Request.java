package cn.com.gehj.httpframework.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.com.gehj.httpframework.http.HttpHeader;
import cn.com.gehj.httpframework.http.HttpMethod;
/*1,mQueryParams没有封;2encodeParam需要改*/
public class Request {
    private String mUrl;
    private HttpMethod mMethod;
    private HttpHeader mHeader;
    private byte[] mData;
    private static final String ENCODING = "utf-8";


    public Request(Builder builder) {
        mUrl = builder.mUrl;
        mMethod = builder.mMethod;
        mHeader = builder.mHeader;
        mData = encodeParam(builder.mFormParams);
        if (builder.mQueryParams.size() > 0) {
            this.mUrl = mUrl+"?"+new String(encodeParam(builder.mQueryParams));
        }

    }

    public static class Builder {
        private String mUrl;
        private HttpMethod mMethod;
        private HttpHeader mHeader;
        private Map<String, String> mFormParams = new HashMap<>();
        private Map<String, String> mQueryParams = new HashMap<>();

        public Builder httpMethod(HttpMethod mMethod) {
            this.mMethod = mMethod;
            return this;
        }

        public Builder addFormParam(String key, String value) {
            mFormParams.put(key, value);
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            mQueryParams.put(key, value);
            return this;
        }

        public Builder addHeader(HttpHeader mHeader) {
            this.mHeader = mHeader;
            return this;
        }

        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }


    public byte[] encodeParam(Map<String, String> value) {//对post参数map转成byte[];

        if (value == null || value.size() == 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        try {

            for (Map.Entry<String, String> entry : value.entrySet()) {//把map转成string:xxx=xxx&xxx=xxx的形式,
                //方便进行post请求;

                buffer.append(URLEncoder.encode(entry.getKey(), ENCODING))
                        .append("=").append(URLEncoder.encode(entry.getValue(), ENCODING));
                if (count != value.size() - 1) {
                    buffer.append("&");
                }
                count++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buffer.toString().getBytes();
    }

    public String getmUrl() {
        return mUrl;
    }

    public HttpMethod getmMethod() {
        return mMethod;
    }

    public HttpHeader getmHeader() {
        return mHeader;
    }

    public byte[] getmData() {
        return mData;
    }
}
