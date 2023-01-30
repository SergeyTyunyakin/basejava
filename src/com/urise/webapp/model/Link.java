package com.urise.webapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Link implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String url;

    public Link(String name, String url) {
        Objects.requireNonNull(name, "Link name must not be null");
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Link(" + name + ", " + url + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link link)) return false;

        if (getName() != null ? !getName().equals(link.getName()) : link.getName() != null) return false;
        return getUrl() != null ? getUrl().equals(link.getUrl()) : link.getUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        return result;
    }
}
