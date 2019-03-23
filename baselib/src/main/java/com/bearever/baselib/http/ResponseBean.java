package com.bearever.baselib.http;

import java.io.Serializable;

/**
 * 网络相应基类
 * created by JiangHua on 2019/2/26
 */
public class ResponseBean<T> implements Serializable {
    public int status;
    public String msg;
    public T data;
}
