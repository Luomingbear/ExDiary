package com.bearever.baselib.http;

import java.io.Serializable;

/**
 * 网络响应基类，主要区别类嵌套
 * created by JiangHua on 2019/2/26
 */
public class BaseResponseBean implements Serializable {
    public int status;
    public String msg;

    public ResponseBean toResponseBean() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.status = status;
        responseBean.msg = msg;
        return responseBean;
    }
}
