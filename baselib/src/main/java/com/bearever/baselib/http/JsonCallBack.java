package com.bearever.baselib.http;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * 请求回调设置
 * created by JiangHua on 2019/2/26
 */
public class JsonCallBack<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallBack() {
    }

    public JsonCallBack(Type type) {
        this.type = type;
    }

    public JsonCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数

    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */


    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    /**
     * 接口返回正确
     *
     * @param response
     */
    @Override
    public void onSuccess(Response<T> response) {

    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        if (null != response) {
            //异常的情况下根据状态显示异常信息
            Throwable throwable = response.getException();

            if (!response.isSuccessful()) {//网络连接失败
                if (throwable instanceof SocketTimeoutException) {
                    onFailed(throwable, "请求超时");
                } else if (throwable instanceof SocketException) {
                    onFailed(throwable, "服务器异常");
                } else if (throwable instanceof JsonSyntaxException) {
                    onFailed(throwable, "数据解析异常");
                } else if (throwable instanceof RepException) {
                    String errorJson = throwable.getMessage();
                    if (!TextUtils.isEmpty(errorJson)) {
                        ResponseBean responseBean = Convert.fromJson(errorJson, ResponseBean.class);
                        onIError(responseBean);
                    } else {
                        onFailed(throwable, "数据异常");
                    }
                } else {
                    onFailed(throwable, "服务异常");
                }
            }
        }
    }

    /**
     * 接口返回的错误的状态码，需要根据业务具体处理
     *
     * @param response
     */
    public void onIError(ResponseBean response) {

    }

    /**
     * 服务器连接异常，如连接超时
     *
     * @param throwable
     * @param message   解析的错误原因
     */
    public void onFailed(Throwable throwable, String message) {

    }
}
