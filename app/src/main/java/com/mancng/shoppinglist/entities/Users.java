package com.mancng.shoppinglist.entities;

import java.util.HashMap;

public class Users {
    private HashMap<String, User> usersFriends;

    public Users() {
    }

    public Users(HashMap<String, User> usersFriends) {
        this.usersFriends = usersFriends;
    }

    public HashMap<String, User> getUsersFriends() {
        return usersFriends;
    }
}
