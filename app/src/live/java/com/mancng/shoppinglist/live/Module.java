package com.mancng.shoppinglist.live;

import com.mancng.shoppinglist.infrastructure.ShoppingListApplication;

public class Module {

    public static void Register(ShoppingListApplication application) {
        new LiveAccountServices(application);
        new LiveShoppingListServices(application);
        new LiveItemServices(application);

    }



}
