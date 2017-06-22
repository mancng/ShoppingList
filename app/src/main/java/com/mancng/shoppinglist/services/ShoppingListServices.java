package com.mancng.shoppinglist.services;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.mancng.shoppinglist.entities.ShoppingList;
import com.mancng.shoppinglist.infrastructure.ServiceResponse;


//Request and Response class to communicate with Otto Bus
public class ShoppingListServices {

    private ShoppingListServices() {
    }

    //Request and Response for AddShoppingListRequest
    public static class AddShoppingListRequest {
        public String shoppingListName;
        public String ownerName;
        public String ownerEmail;

        //Create constructors
        public AddShoppingListRequest(String shoppingListName, String ownerName, String ownerEmail) {
            this.shoppingListName = shoppingListName;
            this.ownerName = ownerName;
            this.ownerEmail = ownerEmail;
        }
    }

    public static class AddShoppingListResponse extends ServiceResponse {

    }

    //Request and Response for DeleteShoppingListRequest
    public static class DeleteShoppingListRequest {
        public String ownerEmail;
        public String shoppingListId;

        //Create constructors
        public DeleteShoppingListRequest(String ownerEmail, String shoppingListId) {
            this.ownerEmail = ownerEmail;
            this.shoppingListId = shoppingListId;
        }
    }

    //Request and Response for ChangeListNameDialogFragment
    public static class ChangeListNameRequest {
        public String newShoppingListName;
        public String shoppingListId;
        public String shoppingListOwnerEmail;

        //Create constructors
        public ChangeListNameRequest(String newShoppingListName, String shoppingListId, String shoppingListOwnerEmail) {
            this.newShoppingListName = newShoppingListName;
            this.shoppingListId = shoppingListId;
            this.shoppingListOwnerEmail = shoppingListOwnerEmail;
        }
    }

    //Need a response to check the response isn't blank
    public static class ChangeListNameResponses extends ServiceResponse {

    }

    //Request and Response to update the title of the ListDetailsActivity when the name is changed
    public static class GetCurrentShoppingListRequest {
        public Firebase reference;

        //Create constructor
        public GetCurrentShoppingListRequest(Firebase reference) {
            this.reference = reference;
        }
    }

    //This is not going to extend ServiceResponse
    public static class GetCurrentShoppingListResponse{
        public ShoppingList shoppingList;
        public ValueEventListener valueEventListener;

    }


}
