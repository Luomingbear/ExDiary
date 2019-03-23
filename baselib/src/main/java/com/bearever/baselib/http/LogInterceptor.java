package com.bearever.baselib.http;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 网络请求拦截器
 * created by JiangHua on 2019/2/28
 */
public class LogInterceptor implements Interceptor {
    private static final String TAG = "LogInterceptor";
    private String cookieString = "";

    public void setCookieString(String cookie) {
        cookieString = cookie;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        if (!TextUtils.isEmpty(cookieString)) { //SessionManager只是一个简易Session管理，就不贴代码了
            builder.header("Set-Cookie", cookieString);
        }

        long t1 = System.nanoTime();
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();

        StringBuffer sb = new StringBuffer();
        sb.append(request.method()).append("\n");
        String url[] = request.url().toString().split("\\?");
        sb.append(url[0]).append("\n");
        if (url.length == 2) {
            String params[] = url[1].split("&");
            for (String param : params) {
                sb.append(Uri.decode(param)).append("\n");
            }
        }

        if (request.body() instanceof FormBody) {
            FormBody postParams = ((FormBody) request.body());
            if (postParams != null) {
                sb.append("post:").append("\n");
                int size = postParams.size();
                for (int i = 0; i < size; i++) {
                    sb.append(postParams.encodedName(i) + "=" + java.net.URLDecoder.decode(postParams.encodedValue(i), "utf-8")).append("\n");
                }
            }
        }

        Log.i(TAG,"Interceptor request url == " + request.url().toString());

        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        Log.i(TAG,"Interceptor response data == " + String.format(Locale.getDefault(), "%s cost %.1fms%n%s", sb.toString(), (t2 - t1) / 1e6d, format(content)));
        //格式化打印json
        //       Log.v(TAG, String.format(Locale.getDefault(), "%s cost %.1fms%n%s", sb.toString(), (t2 - t1) / 1e6d, format(content)));
//        Log.v(TAG, String.format(Locale.getDefault(), "%s cost %.1fms%n%s", sb.toString(), (t2 - t1) / 1e6d, content));
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    public static String format(String jsonStr) {

        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }
}
