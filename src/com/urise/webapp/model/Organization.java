package com.urise.webapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organization implements Comparable<Organization>, Serializable {
    private final Link link;
    private final List<Period> periods = new ArrayList<>();

    public Organization(String name, String url, Period... periods) {
        this(new Link(name, url), new ArrayList<>(List.of(periods)));
    }

    public Organization(Link link, List<Period> periodList) {
        Objects.requireNonNull(link);
        Objects.requireNonNull(periodList);
        this.link = link;
        this.periods.addAll(periodList);
    }

    @Override
    public int compareTo(Organization o) {
        Objects.requireNonNull(o);
        return link.getName().compareTo(o.link.getName());
    }

    public Link getLink() {
        return link;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;

        if (!Objects.equals(link, that.link)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = link != null ? link.hashCode() : 0;
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        var stringList = new ArrayList<String>(6);
        stringList.add(link.getName());
        if (link.getUrl() != null) {
            stringList.add(link.getUrl());
        }
        for (var period : periods) {
            var dateInterval = new StringBuilder();
            dateInterval.append(String.format("С %s", YearMonth.from(period.getDateFrom())));
            if (period.getDateTo() != LocalDate.MAX) {
                dateInterval.append(String.format(" по %s", YearMonth.from(period.getDateTo())));
            } else {
                dateInterval.append(" по сейчас");
            }
            if (!dateInterval.isEmpty()) {
                stringList.add(dateInterval.toString());
            }
            if (period.getTitle() != null) {
                stringList.add(period.getTitle());
            }
            if (period.getDescription() != null) {
                stringList.add(period.getDescription());
            }
        }
        return String.join("\n", stringList);
    }

    public static class Period implements Comparable<Period>, Serializable {

        private final LocalDate dateFrom;
        private final LocalDate dateTo;
        private final String title;
        private final String description;

        public Period(LocalDate dateFrom, LocalDate dateTo, String title, String description) {
            Objects.requireNonNull(dateFrom);
            Objects.requireNonNull(dateTo);
            Objects.requireNonNull(title);
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.title = title;
            this.description = description;
        }

        public Period(LocalDate dateFrom, String title, String description) {
            this(dateFrom, LocalDate.MAX, title, description);
        }

        public Period(LocalDate dateFrom, LocalDate dateTo, String title) {
            this(dateFrom, dateTo, title, null);
        }

        public LocalDate getDateFrom() {
            return dateFrom;
        }

        public LocalDate getDateTo() {
            return dateTo;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Period period)) return false;
            return Objects.equals(dateFrom, period.dateFrom) && Objects.equals(dateTo, period.dateTo) && Objects.equals(title, period.title) && Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dateFrom, dateTo, title, description);
        }

        @Override
        public String toString() {
            return "dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", title='" + title + '\'' + ", description='" + description + '\'';
        }

        @Override
        public int compareTo(Period period) {
            return period.dateFrom.compareTo(dateFrom);
        }
    }
}
