package com.mancng.shoppinglist.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.R2;
import com.mancng.shoppinglist.services.AccountServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {


    @BindView(R2.id.activity_register_linear_layout)
    LinearLayout linearLayout;

    @BindView(R2.id.activity_register_loginButton)
    Button loginButton;

    @BindView(R2.id.activity_register_userEmail)
    EditText userEmail;

    @BindView(R2.id.activity_register_userName)
    EditText userName;

    @BindView(R2.id.activity_register_registerButton)
    Button registerButton;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        linearLayout.setBackgroundResource(R.drawable.background);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Attempting to Register Account");
        mProgressDialog.setCancelable(false);

    }

    @OnClick(R2.id.activity_register_loginButton)
    public void setLoginButton() {

        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @OnClick(R2.id.activity_register_registerButton)
    public void setRegisterButton() {

        bus.post(new AccountServices.RegisterUserRequest(userName.getText().toString(), userEmail.getText().toString().toLowerCase(), mProgressDialog));
    }

    @Subscribe
    public void RegisterUser(AccountServices.RegisterUserResponse response) {
        if (!response.didSucceed()) {
            userEmail.setError(response.getPropertyError("email"));
            userName.setError(response.getPropertyError("userName"));

            Log.i("Registration error", "response.getException().getMessage()");
        }
    }
}
