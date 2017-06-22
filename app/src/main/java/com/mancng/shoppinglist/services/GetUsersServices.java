package com.mancng.shoppinglist.services;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.mancng.shoppinglist.entities.Users;

public class GetUsersServices {

    private GetUsersServices() {
    }

    public static class GetUserFriendsRequest {
        public Firebase reference;

        public GetUserFriendsRequest(Firebase reference) {
            this.reference = reference;
        }
    }

    public static class GetUserFriendsResponse {
        public ValueEventListener listener;
        public Users usersFriends;
    }

}
