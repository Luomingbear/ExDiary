package com.bearever.bean;

import android.text.TextUtils;

import com.bearever.articlememento.model.Section;
import com.bearever.articlememento.richtext.RichTextEditor;
import com.bearever.baselib.http.Convert;
import com.bearever.diarybase.database.sql.DiaryDB;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * 创建日记的数据
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryDO implements Serializable {
    private int did = -1;
    private String time;
    private String content;
    private boolean exchange = false;

    public CreateDiaryDO() {
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExchange() {
        return exchange;
    }

    public void setExchange(boolean exchange) {
        this.exchange = exchange;
    }

    public String getTitle() {
        String title = "";
        Type type = new TypeToken<List<Section>>() {
        }.getType();
        List<Section> sections = Convert.fromJson(getContent(), type);
        if (sections != null && !sections.isEmpty()) {
            for (Section section : sections) {
                if (section.getDecorator().equals(RichTextEditor.CLASS_TEXT_SECTION_DECORATOR)) {
                    title = section.getContent();
                    break;
                }
            }
        }

        if (TextUtils.isEmpty(title)) {
            title = "[图片]";
        }
        return title;
    }

    /**
     * 判断内容是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        Type type = new TypeToken<List<Section>>() {
        }.getType();
        List<Section> sections = Convert.fromJson(getContent(), type);
        if (sections != null && !sections.isEmpty()) {
            for (Section section : sections) {
                if (!TextUtils.isEmpty(section.getContent())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static CreateDiaryDO from(DiaryDB.DiaryDatabaseDO from) {
        CreateDiaryDO to = new CreateDiaryDO();
        to.setDid(from.getDid());
        to.setExchange(from.isExchange());
        to.setContent(from.getContent());
        to.setTime(from.getTime());
        return to;
    }
}
