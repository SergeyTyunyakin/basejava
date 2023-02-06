package com.urise.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<Organization> organizations = new ArrayList<>();

    public OrganizationSection() {
    }

    public OrganizationSection(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public void addItem(Organization org) {
        organizations.add(org);
    }

    public List<Organization> getItems() {
        return organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return organizations.equals(that.organizations);

    }

    @Override
    public int hashCode() {
        return organizations.hashCode();
    }

    @Override
    public String toString() {
        return organizations.toString();
    }
}
