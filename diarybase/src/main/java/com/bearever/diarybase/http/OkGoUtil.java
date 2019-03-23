package com.bearever.diarybase.http;

import com.bearever.baselib.http.JsonCallBack;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;

import java.util.Map;

/**
 * 网络框架二次封装
 * created by JiangHua on 2019/2/26
 */
public class OkGoUtil {

    public static OkGoUtil mInstance;

    public OkGoUtil() {
    }

    public synchronized static OkGoUtil getInstance() {
        if (mInstance == null) {
            mInstance = new OkGoUtil();
        }

        return mInstance;
    }

    /**
     * get 请求
     *
     * @param url      请求地址
     * @param map      请求的参数
     * @param callback 请求的回调
     * @param <T>      返回类型
     */
    public <T> void getRequest(String url, Map<String, String> map, JsonCallBack<T> callback) {
        OkGo.<T>get(HttpConstant.HTTP_DEBUG + url)
                .params(map)
                .execute(callback);
    }

    /**
     * get 请求
     *
     * @param url      请求地址
     * @param tag      请求的标记  用于取消请求用
     * @param map      请求的参数
     * @param callback 请求的回调
     * @param <T>      返回类型
     */
    public <T> void getRequest(String url, Object tag, Map<String, String> map, JsonCallBack<T> callback) {
        OkGo.<T>get(HttpConstant.HTTP_DEBUG + url)
                .tag(tag)
                .params(map)
                .execute(callback);
    }


    /**
     * @param url      请求地址
     * @param map      请求的参数
     * @param callback 请求的回调
     * @param <T>      返回类型
     */
    public <T> void postRequest(String url, Map<String, String> map, JsonCallBack<T> callback) {
        OkGo.<T>post(HttpConstant.HTTP_DEBUG + url)
                .params(map)
                .execute(callback);
    }

    /**
     * @param url      请求地址
     * @param tag      请求的标记  用于取消请求用
     * @param map      请求的参数
     * @param callback 请求的回调
     * @param <T>      返回类型
     */
    public <T> void postRequest(String url, Object tag, Map<String, String> map, JsonCallBack<T> callback) {
        OkGo.<T>post(HttpConstant.HTTP_DEBUG + url)
                .tag(tag)
                .params(map)
                .execute(callback);
    }

    /**
     * @param url      请求地址
     * @param map      请求的参数
     * @param callback 请求的回调
     * @param <T>      返回类型
     */
    public <T> void postRequestNoHeader(String url, Map<String, String> map, JsonCallBack<T> callback) {
        OkGo.<T>post(HttpConstant.HTTP_DEBUG + url)
                .params(map)
                .execute(callback);
    }


    /**
     * @param url 请求地址
     * @param <T> 返回类型
     */
    public <T> PostRequest<T> postRequest(String url) {
        return OkGo.<T>post(HttpConstant.HTTP_DEBUG + url);
    }


}
