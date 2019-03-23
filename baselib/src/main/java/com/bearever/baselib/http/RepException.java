package com.bearever.baselib.http;

import com.google.gson.Gson;

/**
 * created by JiangHua on 2019/2/26
 */
public class RepException extends IllegalStateException {

    private ResponseBean errorBean;

    public RepException(String s) {
        super(s);
        errorBean = new Gson().fromJson(s, ResponseBean.class);
    }

    public ResponseBean getErrorBean() {
        return errorBean;
    }
}