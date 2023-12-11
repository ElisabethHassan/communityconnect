package com.example.finalproject;

public class Organization {
    private String organizationId;
    private String name;
    private String contactInformation;

    public Organization() {
    }

    public Organization(String organizationId, String name, String contactInformation) {
        this.organizationId = organizationId;
        this.name = name;
        this.contactInformation = contactInformation;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}
