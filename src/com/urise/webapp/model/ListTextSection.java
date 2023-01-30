package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class ListTextSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<String> items = new ArrayList<>();

    public ListTextSection() {
    }

    public ListTextSection(List<String> items) {
        this.items = items;
    }

    public void addItem(String text) {
        items.add(text);
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListTextSection that)) return false;

        return getItems() != null ? getItems().equals(that.getItems()) : that.getItems() == null;
    }

    @Override
    public int hashCode() {
        return getItems() != null ? getItems().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "- " + String.join("\n- ", items);
    }
}
