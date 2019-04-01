package com.bearever.articlememento.memento;

import com.bearever.articlememento.model.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章的备忘录
 * Created by luoming on 2018/3/22.
 */

public class ArticleMemento {
    private List<Section> sectionList = new ArrayList<>();

    public ArticleMemento(ArticleOriginator originator) {
        sectionList.clear();
        for (Section section : originator.getSectionList()) {
            sectionList.add(section);
        }
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("一共有%d个段落", sectionList.size()));
        sb.append("---------");
        for (Section section : sectionList) {
            sb.append(section.getContent());
            sb.append(",");
        }
        sb.append("\n");
        return sb.toString();
    }
}
