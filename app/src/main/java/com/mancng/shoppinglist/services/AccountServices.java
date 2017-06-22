package com.mancng.shoppinglist.services;


import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.mancng.shoppinglist.infrastructure.ServiceResponse;

//Request and Response class to communicate with Otto Bus
public class AccountServices {

    private AccountServices() {

    }

    //Make a User registration respond to the bus
    public static class RegisterUserRequest{
        public String userName;
        public String userEmail;
        public ProgressDialog progressDialog;

        //Create Constructor
        public RegisterUserRequest(String userName, String userEmail, ProgressDialog progressDialog) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.progressDialog = progressDialog;
        }
    }

    //Create a response
    public static class RegisterUserResponse extends ServiceResponse {

    }

    //Make a User login respond to the bus
    public static class LogUserInRequest {
        public String userEmail;
        public String userPassword;
        public ProgressDialog progressDialog;
        public SharedPreferences sharedPreferences;

        //Create Constructor

        public LogUserInRequest(String userEmail, String userPassword, ProgressDialog progressDialog, SharedPreferences sharedPreferences) {
            this.userEmail = userEmail;
            this.userPassword = userPassword;
            this.progressDialog = progressDialog;
            this.sharedPreferences = sharedPreferences;
        }
    }

    //Create a response
    public static class LogUserInResponse extends ServiceResponse {

    }


    //Make a FB login respond bus
    public static class LogUserInFacebookRequest {
        public AccessToken accessToken;
        public ProgressDialog progressDialog;
        public String userName;
        public String userEmail;
        public SharedPreferences sharedPreferences;

        public LogUserInFacebookRequest(AccessToken accessToken, ProgressDialog progressDialog, String userName, String userEmail, SharedPreferences sharedPreferences) {
            this.accessToken = accessToken;
            this.progressDialog = progressDialog;
            this.userName = userName;
            this.userEmail = userEmail;
            this.sharedPreferences = sharedPreferences;
        }

//No need a response for this because user is going to login via FB. FB is going to check the property errors and not us.

    }


}
