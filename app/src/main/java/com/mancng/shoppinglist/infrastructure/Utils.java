package com.mancng.shoppinglist.infrastructure;

//A class will be used globally within the app
public class Utils {
    public static final String FIRE_BASE_URL = "https://shoppinglist-9d9a4.firebaseio.com/";
    public static final String FIRE_BASE_USER_REFERENCE = FIRE_BASE_URL + "users/";
    public static final String FIRE_BASE_SHOPPING_LIST_REFERENCE = FIRE_BASE_URL + "userShoppingList/";
    public static final String FIRE_BASE_LIST_ITEMS_REFERENCE  = FIRE_BASE_URL + "shoppingListItems/";
    public static final String FIRE_BASE_USER_FRIEND_REFERENCE = FIRE_BASE_URL + "usersFriends/";

    public static final String MY_PREFERENCE = "MY_PREFERENCE";
    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";


    //This allows us to save it in shared preferences
    public static final String LIST_ORDER_PREFERENCE = "LIST_ORDER_PREFERENCE";
    //Define the actual default value
    public static final String ORDER_BY_KEY = "orderByPushKey";


    //Because Firebase doesn't allow the url to have the period from an email. We're going to change the period to a comma.
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".",",");
    }

    //Change the comma back to period on emails
    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",",".");
    }

    //Create a method to logout the user
//    public static void logUserOut (Context context, SharedPreferences sharedPreferences, FirebaseAuth auth) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Utils.EMAIL, null).apply();
//        editor.putString(Utils.USERNAME, null).apply();
//        auth.signOut();
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//      }

}
