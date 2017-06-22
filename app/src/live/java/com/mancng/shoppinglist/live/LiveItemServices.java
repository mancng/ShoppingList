package com.mancng.shoppinglist.live;

import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.mancng.shoppinglist.entities.Item;
import com.mancng.shoppinglist.infrastructure.ShoppingListApplication;
import com.mancng.shoppinglist.infrastructure.Utils;
import com.mancng.shoppinglist.services.ItemServices;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LiveItemServices extends BaseLiveService {
    public LiveItemServices(ShoppingListApplication application) {
        super(application);
    }


    @Subscribe
    public void AddItem (ItemServices.AddItemRequest request) {
        ItemServices.AddItemResponse response = new ItemServices.AddItemResponse();

        if (request.itemName.isEmpty()) {
            response.setPropertyErrors("itemName","Item must have a name.");
        }

        if (response.didSucceed()) {
            Firebase itemReference = new Firebase(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId).push();
            Firebase listReference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.userEmail + "/" + request.shoppingListId);

            Item item = new Item(itemReference.getKey(),request.itemName,request.userEmail,"",false);
            itemReference.child("id").setValue(item.getId());
            itemReference.child("itemName").setValue(item.getItemName());
            itemReference.child("ownerEmail").setValue(item.getOwnerEmail());
            itemReference.child("boughtBy").setValue(item.getBoughtBy());
            itemReference.child("bought").setValue(item.isBought());

            HashMap<String, Object> timeLastChanged = new HashMap<>();
            timeLastChanged.put("date", ServerValue.TIMESTAMP);

            Map newListData = new HashMap();
            newListData.put("dateLastChanged",timeLastChanged);
            listReference.updateChildren(newListData);

            Toast.makeText(application.getApplicationContext(), "Item Added!",Toast.LENGTH_LONG).show();
        }
        bus.post(response);
    }


    @Subscribe
    public void DeleteItem (ItemServices.DeleteItemRequest request) {

        Firebase itemReference = new Firebase(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId + "/" + request.itemId);
        Firebase listReference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.userEmail + "/" + request.shoppingListId);

        itemReference.removeValue();

        //Changes for listReference
        HashMap<String, Object> timeLastChanged = new HashMap<>();
        timeLastChanged.put("date", ServerValue.TIMESTAMP);
        Map newListData = new HashMap();
        newListData.put("dateLastChanged",timeLastChanged);
        listReference.updateChildren(newListData);
    }

    @Subscribe
    public void ChangeItemName (ItemServices.ChangeItemNameRequest request) {
        ItemServices.ChangeItemNameResponse response = new ItemServices.ChangeItemNameResponse();
        if (request.newItemName.isEmpty()) {
            response.setPropertyErrors("itemName", "Item name cannot be blank.");
        }

        if (response.didSucceed()) {
            Firebase itemReference = new Firebase(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId + "/" + request.itemId);
            Firebase listReference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.userEmail + "/" + request.shoppingListId);


            Map newItemData = new HashMap();
            newItemData.put("itemName",request.newItemName);

            itemReference.updateChildren(newItemData);

            HashMap<String, Object> timeLastChanged = new HashMap<>();
            timeLastChanged.put("date", ServerValue.TIMESTAMP);
            Map newListData = new HashMap();
            newListData.put("dateLastChanged",timeLastChanged);
            listReference.updateChildren(newListData);
        }
        bus.post(response);
    }

    @Subscribe
    public void ChangeItemBoughtStatus(ItemServices.ChangeBoughtItemStatusRequest request) {
        Firebase itemReference = new Firebase(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId + "/" + request.item.getId());
        if (!request.item.isBought()) {
            Map newItemData = new HashMap();
            newItemData.put("bought", true);
            itemReference.updateChildren(newItemData);
        } else if (request.item.getBoughtBy().equals(request.currentUserEmail)){
            Map newItemData = new HashMap();
            newItemData.put("bought", false);
            itemReference.updateChildren(newItemData);
        }
    }
}
