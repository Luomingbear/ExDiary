package com.bearever.util;

import android.net.Uri;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * @author: X_Meteor
 * @description: 用于Uri的序列化
 * @version: V_1.0.0
 * @date: 2019/3/9 11:13
 * @company:
 * @email: lx802315@163.com
 */
public class UriSerializer  implements JsonSerializer<Uri> {
    public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
