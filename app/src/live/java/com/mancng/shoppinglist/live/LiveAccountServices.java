package com.mancng.shoppinglist.live;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.mancng.shoppinglist.activities.LoginActivity;
import com.mancng.shoppinglist.activities.MainActivity;
import com.mancng.shoppinglist.entities.User;
import com.mancng.shoppinglist.infrastructure.ShoppingListApplication;
import com.mancng.shoppinglist.infrastructure.Utils;
import com.mancng.shoppinglist.services.AccountServices;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class LiveAccountServices extends BaseLiveService {
    public LiveAccountServices(ShoppingListApplication application) {
        super(application);
    }

    @Subscribe
    public void RegisterUser(final AccountServices.RegisterUserRequest request) {
        AccountServices.RegisterUserResponse response = new AccountServices.RegisterUserResponse();

        if (request.userEmail.isEmpty()) {
            response.setPropertyErrors("email", "Please put in your email.");
        }

        if (request.userName.isEmpty()) {
            response.setPropertyErrors("userName", "Please put in your username.");
        }

        if (response.didSucceed()) {
            request.progressDialog.show();

            //Create a random password for new accounts since the registration interface does NOT have a password field
            SecureRandom random = new SecureRandom();
            final String randomPassword = new BigInteger(32, random).toString();

            auth.createUserWithEmailAndPassword(request.userEmail.toString().toLowerCase(), randomPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                auth.sendPasswordResetEmail(request.userEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    request.progressDialog.dismiss();
                                                    Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                } else {
                                                    Firebase reference = new Firebase(Utils.FIRE_BASE_USER_REFERENCE + Utils.encodeEmail(request.userEmail.toString().toLowerCase()));

                                                    HashMap<String, Object> timeJoined = new HashMap<>();
                                                    timeJoined.put("timeJoined", ServerValue.TIMESTAMP);

                                                    reference.child("email").setValue(request.userEmail.toString().toLowerCase());
                                                    reference.child("userName").setValue(request.userName);
                                                    reference.child("hasLoggedInWithPassword").setValue(false);
                                                    reference.child("timeJoined").setValue(timeJoined);

                                                    Toast.makeText(application.getApplicationContext(), "Please Check Your Email", Toast.LENGTH_LONG).show();

                                                    request.progressDialog.dismiss();

                                                    //Send user to Login class once registration completes
                                                    Intent intent = new Intent(application.getApplicationContext(), LoginActivity.class);
                                                    //Don't let user go back. Similar to calling finish().
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    application.startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }

        bus.post(response);
    }

    @Subscribe
    public void LogInUser(final AccountServices.LogUserInRequest request) {
        AccountServices.LogUserInResponse response = new AccountServices.LogUserInResponse();

        if (request.userEmail.isEmpty()) {
            response.setPropertyErrors("email", "Please enter your email");
        }

        if (request.userPassword.isEmpty()) {
            response.setPropertyErrors("password", "Please enter your password");
        }

        if (response.didSucceed()) {
            request.progressDialog.show();
            auth.signInWithEmailAndPassword(request.userEmail.toString().toLowerCase(), request.userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                final Firebase userLocation = new Firebase(Utils.FIRE_BASE_USER_REFERENCE + Utils.encodeEmail(request.userEmail));
                                //
                                userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //Get user
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user != null) {
                                            userLocation.child("hasLoggedInWithPassword").setValue(true);
                                            SharedPreferences sharedPreferences = request.sharedPreferences;
                                            sharedPreferences.edit().putString(Utils.EMAIL, Utils.encodeEmail(user.getEmail())).apply();
                                            sharedPreferences.edit().putString(Utils.USERNAME, user.getUserName()).apply();

                                            request.progressDialog.dismiss();

                                            //Send user to MainActivity after logged in
                                            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                                            //Don't let user go back. Similar to calling finish().
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            application.startActivity(intent);

//                                            Toast.makeText(application.getApplicationContext(), "Hello " + request.userName + "!", Toast.LENGTH_LONG).show();


                                        } else {
                                            request.progressDialog.dismiss();
                                            Toast.makeText(application.getApplicationContext(), "Failed to connect to server: Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        request.progressDialog.dismiss();
                                        Toast.makeText(application.getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
        }
        bus.post(response);
    }

    //This is how Firebase handles FB logins
    @Subscribe
    public void FacebookLogin(final AccountServices.LogUserInFacebookRequest request) {
        request.progressDialog.show();

        AuthCredential authCredential = FacebookAuthProvider.getCredential(request.accessToken.getToken());

        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            request.progressDialog.dismiss();
                            Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            //This logs users in and also save the user into our Firebase database
                            final Firebase reference = new Firebase(Utils.FIRE_BASE_USER_REFERENCE + Utils.encodeEmail(request.userEmail.toString().toLowerCase()));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //See if we have this user in our Firebase database. If not, perform the following action
                                    if (dataSnapshot.getValue() == null) {

                                        HashMap<String, Object> timeJoined = new HashMap<>();
                                        timeJoined.put("timeJoined", ServerValue.TIMESTAMP);

                                        reference.child("email").setValue(request.userEmail.toString().toLowerCase());
                                        reference.child("userName").setValue(request.userName);
                                        reference.child("hasLoggedInWithPassword").setValue(true);
                                        reference.child("timeJoined").setValue(timeJoined);

                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    request.progressDialog.dismiss();
                                    Toast.makeText(application.getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });

                            SharedPreferences sharedPreferences = request.sharedPreferences;
                            sharedPreferences.edit().putString(Utils.EMAIL, Utils.encodeEmail(request.userEmail)).apply();
                            sharedPreferences.edit().putString(Utils.USERNAME, request.userName).apply();

                            request.progressDialog.dismiss();

                            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                            //Don't let user go back. Similar to calling finish().
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(intent);

                            Toast.makeText(application.getApplicationContext(), "Hello " + request.userName + "!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
