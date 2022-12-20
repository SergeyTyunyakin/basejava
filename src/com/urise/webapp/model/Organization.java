package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private final String title;
    private final String description;
    private final Link homePage;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public Organization(String orgName, String title, String description, String url, LocalDate dateFrom, LocalDate dateTo) {
        Objects.requireNonNull(orgName, "orgName must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(dateFrom, "dateFrom must not be null");
        Objects.requireNonNull(dateTo, "dateTo must not be null");
        this.homePage = new Link(orgName, url);
        this.title = title;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;

        if (!title.equals(that.title)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!homePage.equals(that.homePage)) return false;
        if (!dateFrom.equals(that.dateFrom)) return false;
        return dateTo.equals(that.dateTo);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + homePage.hashCode();
        result = 31 * result + dateFrom.hashCode();
        result = 31 * result + dateTo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + homePage.getName() + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
