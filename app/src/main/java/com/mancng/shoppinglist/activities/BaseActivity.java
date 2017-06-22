package com.mancng.shoppinglist.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mancng.shoppinglist.infrastructure.ShoppingListApplication;
import com.mancng.shoppinglist.infrastructure.Utils;
import com.squareup.otto.Bus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BaseActivity extends AppCompatActivity{
    protected ShoppingListApplication application;
    protected Bus bus;
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;
    protected String userEmail, userName;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (ShoppingListApplication) getApplication();
        bus = application.getBus();
        bus.register(this);
        printKeyHash();

        sharedPreferences = getSharedPreferences(Utils.MY_PREFERENCE, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(Utils.USERNAME, "");
        userEmail = sharedPreferences.getString(Utils.EMAIL, "");

        auth = FirebaseAuth.getInstance();

        //Verify if any user has logged in
        if (!((this instanceof LoginActivity) || (this instanceof RegisterActivity))) {
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Utils.EMAIL, null).apply();
                        editor.putString(Utils.USERNAME, null).apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        //User cannot click "back"
                        finish();
                    }
                }
            };

            if (userEmail.equals("")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Utils.EMAIL, null).apply();
                editor.putString(Utils.USERNAME, null).apply();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                //User cannot click "back"
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!((this instanceof LoginActivity) || (this instanceof RegisterActivity))) {
            auth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);

        if (!((this instanceof LoginActivity) || (this instanceof RegisterActivity))) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    //Get KeyHash for FB
    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("jk", "Exception(NameNotFoundException) : " + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
        }
    }





}
