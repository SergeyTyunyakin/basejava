package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class TextItemsSection extends AbstractSection {

    private List<String> textItems = new ArrayList<>();

    public TextItemsSection() {
    }

    public TextItemsSection(List<String> items) {
        this.textItems = items;
    }

    public void addItem(String text) {
        textItems.add(text);
    }

    public List<String> getTextItems() {
        return textItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextItemsSection that)) return false;

        return getTextItems() != null ? getTextItems().equals(that.getTextItems()) : that.getTextItems() == null;
    }

    @Override
    public int hashCode() {
        return getTextItems() != null ? getTextItems().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "- " + String.join("\n- ", textItems);
    }
}
