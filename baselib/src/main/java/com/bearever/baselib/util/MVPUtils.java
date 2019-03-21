package com.bearever.baselib.util;

import java.lang.reflect.ParameterizedType;

/**
 * 工具类
 * created by JiangHua on 2019/2/25
 */
public class MVPUtils {
    public static  <T> T getT(Object o, int i) {
        try {
            return  ((Class<T>) ( (ParameterizedType)(o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
