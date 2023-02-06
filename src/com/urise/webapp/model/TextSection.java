package com.urise.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.io.Serial;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class TextSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;

    public void setText(String text) {
        this.text = text;
    }

    private String text;

    public TextSection() {
    }

    public TextSection(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextSection that)) return false;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
