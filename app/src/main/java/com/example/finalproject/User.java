package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    private String email;
    private String profileImageUrl;
    private String accountType; //volunteer or organization
    private List<String> myEvents;

    public User(String userId, String name, String email, String accountType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountType() {
        return accountType;
    }

    public List<String> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<String> myEvents) {
        this.myEvents = myEvents;
    }
}