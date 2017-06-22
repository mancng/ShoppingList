package com.mancng.shoppinglist.live;

import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.mancng.shoppinglist.entities.ShoppingList;
import com.mancng.shoppinglist.infrastructure.ShoppingListApplication;
import com.mancng.shoppinglist.infrastructure.Utils;
import com.mancng.shoppinglist.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

//Code to talk to Firebase
public class LiveShoppingListServices extends BaseLiveService {

    public LiveShoppingListServices(ShoppingListApplication application) {
        super(application);
    }

    //Add new data to Firebase
    @Subscribe
    public void AddShoppingList (ShoppingListServices.AddShoppingListRequest request) {
        ShoppingListServices.AddShoppingListResponse response = new ShoppingListServices.AddShoppingListResponse();

        if (request.shoppingListName.isEmpty()) {
            response.setPropertyErrors("listName", "Shopping List must have a name");
        }

        if (response.didSucceed()) {
            //The Push is going to create the random string and use the ID as the shopping list
            Firebase reference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.ownerEmail).push();
            HashMap<String,Object> timeStampedCreated = new HashMap<>();
            timeStampedCreated.put("timestamp", ServerValue.TIMESTAMP);
            ShoppingList shoppingList = new ShoppingList(reference.getKey(),
                    request.shoppingListName,
                    Utils.decodeEmail(request.ownerEmail),
                    request.ownerName,
                    timeStampedCreated);
            //Make new shopping list in the database
            reference.child("id").setValue(shoppingList.getId());
            reference.child("listName").setValue(shoppingList.getListName());
            reference.child("ownerName").setValue(shoppingList.getOwnerName());
            reference.child("ownerEmail").setValue(shoppingList.getOwnerEmail());
            reference.child("dateCreated").setValue(shoppingList.getDateCreated());
            reference.child("dateLastChanged").setValue(shoppingList.getDateLastChanged());

            Toast.makeText(application.getApplicationContext(), "List Created!",Toast.LENGTH_LONG).show();
        }

        bus.post(response);
    }

    //Delete data from Firebase
    @Subscribe
    public void DeleteShoppingList (ShoppingListServices.DeleteShoppingListRequest request) {
        //Path to the shopping list
        Firebase reference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.ownerEmail + "/" + request.shoppingListId);
        //Path to the items (Delete items when shopping list is deleted)
        Firebase itemReference = new Firebase(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + request.shoppingListId);
        //Delete the shopping list
        reference.removeValue();
        //Delete the items within the shopping list
        itemReference.removeValue();
    }


    //Update existing data in Firebase
    @Subscribe
    public void ChangeListName(ShoppingListServices.ChangeListNameRequest request) {
        ShoppingListServices.ChangeListNameResponses response = new ShoppingListServices.ChangeListNameResponses();
        if(request.newShoppingListName.isEmpty()) {
            response.setPropertyErrors("listName", "Shopping list must have a name");
        }

        if(response.didSucceed()) {
            //Need Firebase reference
            Firebase reference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + request.shoppingListOwnerEmail + "/" + request.shoppingListId);

            //Update Child should be used instead of put to make sure changes are updated for everyone that has access to the list since that is an existing field
            //When updating a child record within the list, make sure we're referencing the dateLastChanged timestamp
            HashMap<String, Object> timeLastChanged = new HashMap<>();
            timeLastChanged.put("date",ServerValue.TIMESTAMP);

            Map newListData = new HashMap();
            newListData.put("listName",request.newShoppingListName);
            newListData.put("dateLastChanged",timeLastChanged);

            reference.updateChildren(newListData);
            }
        bus.post(response);

    }

    @Subscribe
    public void getCurrentShoppingList(ShoppingListServices.GetCurrentShoppingListRequest request) {
        final ShoppingListServices.GetCurrentShoppingListResponse response = new ShoppingListServices.GetCurrentShoppingListResponse();
        response.valueEventListener = request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.shoppingList = dataSnapshot.getValue(ShoppingList.class);
                if (response.shoppingList != null) {
                    bus.post(response);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
