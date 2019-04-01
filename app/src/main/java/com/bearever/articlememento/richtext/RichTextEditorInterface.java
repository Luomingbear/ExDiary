package com.bearever.articlememento.richtext;

import com.bearever.articlememento.memento.ArticleMemento;
import com.bearever.articlememento.model.Section;

import java.util.List;

/**
 * 富文本编辑器开放的方法
 * Created by luoming on 2018/3/29.
 */

public interface RichTextEditorInterface {
    /**
     * 通过文字备忘录恢复view
     *
     * @param memento
     */
    void setContentByArticleMemento(ArticleMemento memento);

    /**
     * 通过section列表恢复view
     *
     * @param sections
     */
    void setContentBySectionList(List<Section> sections);

    /**
     * 获取段落列表
     *
     * @return
     */
    List<Section> getContentSectionList();

    /**
     * 通过字符串恢复view；这个字符串是将sectionList转化为json的结果
     *
     * @param content
     */
    void setContentByString(String content);

    /**
     * 获取段落列表的json字符串
     *
     * @return
     */
    String getContentString();

    /**
     * 添加一个文本控件
     */
    void addTextSection();

    /**
     * 添加一个图片控件
     */
    void addImageSection(String url);

    /**
     * 添加一个段落
     *
     * @param section
     */
    void addSection(Section section);

    /**
     * 添加一个段落
     *
     * @param section
     * @param position
     */
    void addSection(Section section,int position);

}
