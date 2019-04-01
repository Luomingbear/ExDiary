package com.bearever.articlememento.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 段落的内容
 * Created by luoming on 2018/3/29.
 */

public class Section {
    private String decorator = ""; //装饰器，负责存储当前段落使用什么装饰器构造
    private String content = ""; //内容
    private int style = STYLE_NORMAL; //风格
    private int gravity = GRIVITY_LEFT; //重心
    private int selection = 0; //指针的位置
    private int index = 0; //标记

    public static final int STYLE_BOLD = 0x10; //粗体
    public static final int STYLE_NORMAL = 0x11; //默认字体
    public static final int GRIVITY_LEFT = 0x12; //靠左
    public static final int GRIVITY_CENTER = 0x13; //居中

    public Section() {
    }

    public Section(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.decorator = jsonObject.getString("decorator");
            this.content = jsonObject.getString("content");
            this.style = jsonObject.getInt("style");
            this.gravity = jsonObject.getInt("gravity");
            this.selection = jsonObject.getInt("selection");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Section(String decorator, String content, int style, int gravity, int selection, int index) {
        this.decorator = decorator;
        this.content = content;
        this.style = style;
        this.gravity = gravity;
        this.selection = selection;
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public String getDecorator() {
        return decorator;
    }

    public void setDecorator(String decorator) {
        this.decorator = decorator;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String toJson() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"decorator\":\"" + getDecorator() + "\",");
        sb.append("\"content\":\"" + getContent() + "\",");
        sb.append("\"gravity\":" + getGravity() + ",");
        sb.append("\"style\":" + getStyle() + ",");
        sb.append("\"selection\":" + getSelection());
        sb.append("}");
        return sb.toString();
    }
}
