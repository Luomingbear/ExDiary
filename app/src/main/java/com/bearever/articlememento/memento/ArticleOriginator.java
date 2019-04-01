package com.bearever.articlememento.memento;

import com.bearever.articlememento.model.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章e原发器，用于创建和修改文章备忘录
 * Created by luoming on 2018/3/22.
 */

public class ArticleOriginator {

    private List<Section> sectionList = new ArrayList<>();

    public ArticleMemento createMemento() {
        return new ArticleMemento(this);
    }

    public ArticleOriginator() {
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList.clear();
        for (Section section : sectionList) {
            Section sec = new Section(section.getDecorator(), section.getContent(), section.getStyle(),
                    section.getGravity(), section.getSelection(), section.getIndex());
            this.sectionList.add(sec);
        }
    }

    public void removeSection(Section section) {
        if (sectionList.contains(section))
            sectionList.remove(section);
    }

    public void addSection(Section section) {
        sectionList.add(section);
    }

    public void clearSection() {
        sectionList.clear();
    }
}
