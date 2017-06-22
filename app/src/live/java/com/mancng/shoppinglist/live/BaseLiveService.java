package com.mancng.shoppinglist.live;

import com.google.firebase.auth.FirebaseAuth;
import com.mancng.shoppinglist.infrastructure.ShoppingListApplication;
import com.squareup.otto.Bus;

public class BaseLiveService {
    protected Bus bus;
    protected ShoppingListApplication application;
    protected FirebaseAuth auth;

    public BaseLiveService(ShoppingListApplication application) {
        this.application = application;
        bus = application.getBus();
        bus.register(this);
        auth = FirebaseAuth.getInstance();
    }
}
