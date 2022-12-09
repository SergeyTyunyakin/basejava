package com.urise.webapp.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume {
    public static final Comparator<Resume> COMPARE_BY_NAME = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    private final String uuid;
    private final String fullName;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "UUID must be not null");
        Objects.requireNonNull(fullName, "fullName must be not null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!Objects.equals(uuid, resume.uuid)) return false;
        return Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }
}
