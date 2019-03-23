package com.bearever.baselib.util;

import android.net.Uri;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author: X_Meteor
 * @description: 用于Uri的反序列化
 * @version: V_1.0.0
 * @date: 2019/3/9 11:14
 * @company:
 * @email: lx802315@163.com
 */
public class UriDeserializer implements JsonDeserializer<Uri> {
    @Override
    public Uri deserialize(final JsonElement src, final Type srcType,
                           final JsonDeserializationContext context) throws JsonParseException {
        return Uri.parse(src.getAsString());
    }
}
