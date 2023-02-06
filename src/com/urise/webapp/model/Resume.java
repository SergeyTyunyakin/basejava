package com.urise.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.*;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Comparator<Resume> COMPARE_BY_NAME = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    private String uuid;
    private String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "UUID must be not null");
        Objects.requireNonNull(fullName, "fullName must be not null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public Resume() {
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public AbstractSection getSection(SectionType type) {
        return sections.get(type);
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, AbstractSection> getSections() {
        return sections;
    }

    public void addContact(ContactType contactType, String val) {
        contacts.put(contactType, val);
    }

    public void addSection(SectionType sectionType, AbstractSection abstractSection) {
        sections.put(sectionType, abstractSection);
    }


    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume resume)) return false;

        if (getUuid() != null ? !getUuid().equals(resume.getUuid()) : resume.getUuid() != null) return false;
        if (getFullName() != null ? !getFullName().equals(resume.getFullName()) : resume.getFullName() != null)
            return false;
        if (getContacts() != null ? !getContacts().equals(resume.getContacts()) : resume.getContacts() != null)
            return false;
        return getSections() != null ? getSections().equals(resume.getSections()) : resume.getSections() == null;
    }

    @Override
    public int hashCode() {
        int result = getUuid() != null ? getUuid().hashCode() : 0;
        result = 31 * result + (getFullName() != null ? getFullName().hashCode() : 0);
        result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
        result = 31 * result + (getSections() != null ? getSections().hashCode() : 0);
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }
}
