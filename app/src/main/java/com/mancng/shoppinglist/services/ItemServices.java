package com.mancng.shoppinglist.services;

import com.mancng.shoppinglist.entities.Item;
import com.mancng.shoppinglist.infrastructure.ServiceResponse;

public class ItemServices {

    private ItemServices() {
    }


    //Create request and response to talk to the database
    public static class AddItemRequest {
        public String shoppingListId;
        public String itemName;
        public String userEmail;

        public AddItemRequest(String shoppingListId, String itemName, String userEmail) {
            this.shoppingListId = shoppingListId;
            this.itemName = itemName;
            this.userEmail = userEmail;
        }
    }

    //Response
    public static class AddItemResponse extends ServiceResponse {

    }


    //Request for DeleteItemDialogFragment. No response is needed
    public static class DeleteItemRequest {
        public String itemId;
        public String shoppingListId;
        public String userEmail;

        //Create constructors
        public DeleteItemRequest(String itemId, String shoppingListId, String userEmail) {
            this.itemId = itemId;
            this.shoppingListId = shoppingListId;
            this.userEmail = userEmail;
        }
    }


    //Request and Response for ChangeItemNameDialogFragment
    public static class ChangeItemNameRequest {
        public String itemId;
        public String shoppingListId;
        public String userEmail;
        public String newItemName;

        //Create constructors
        public ChangeItemNameRequest(String itemId, String shoppingListId, String userEmail, String newItemName) {
            this.itemId = itemId;
            this.shoppingListId = shoppingListId;
            this.userEmail = userEmail;
            this.newItemName = newItemName;
        }
    }

    //Response to check the response isn't blank
    public static class ChangeItemNameResponse extends ServiceResponse {

    }

    //Request for boughtBy. No response needed
    public static class ChangeBoughtItemStatusRequest {
        public Item item;
        public String currentUserEmail;
        public String shoppingListId;

        public ChangeBoughtItemStatusRequest(Item item, String currentUserEmail, String shoppingListId) {
            this.item = item;
            this.currentUserEmail = currentUserEmail;
            this.shoppingListId = shoppingListId;
        }
    }


}
