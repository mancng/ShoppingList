package com.mancng.shoppinglist.entities;

import java.util.HashMap;

public class User {
    private String email;
    private String userName;
    private HashMap<String, Object> timeJoined;
    private boolean hasLoggedInWithPassword;

    public User() {
    }

    public User(String email, String userName, HashMap<String, Object> dateJoined, boolean hasLoggedInWithPassword) {
        this.email = email;
        this.userName = userName;
        this.timeJoined = dateJoined;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public HashMap<String, Object> getTimeJoined() {
        return timeJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }
}
