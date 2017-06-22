package com.mancng.shoppinglist.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.dialog.AddItemDialogFragment;
import com.mancng.shoppinglist.dialog.ChangeItemNameDialogFragment;
import com.mancng.shoppinglist.dialog.ChangeListNameDialogFragment;
import com.mancng.shoppinglist.dialog.DeleteItemDialogFragment;
import com.mancng.shoppinglist.dialog.DeleteListDialogFragment;
import com.mancng.shoppinglist.entities.Item;
import com.mancng.shoppinglist.entities.ShoppingList;
import com.mancng.shoppinglist.infrastructure.Utils;
import com.mancng.shoppinglist.services.ItemServices;
import com.mancng.shoppinglist.services.ShoppingListServices;
import com.mancng.shoppinglist.views.ItemListViews.ListItemViewHolder;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListDetailsActivity extends BaseActivity{

    @BindView(R.id.activity_list_details_FAB)
    FloatingActionButton floatingActionButton;

    private FirebaseRecyclerAdapter adapter;

    public static final String SHOPPING_LIST_DETAILS = "SHOPPING_LIST_DETAILS";

    private String mShoppingId;
    private String mShoppingName;
    private String mShoppingOwner;

    private Firebase mShoppingListReference;

    private ValueEventListener mShoppingListListener;

    private ShoppingList mCurrentShoppingList;


    public static Intent newInstance(Context context, ArrayList<String> shoppingListInfo){
        Intent intent = new Intent(context,ListDetailsActivity.class);
        intent.putStringArrayListExtra(SHOPPING_LIST_DETAILS,  shoppingListInfo);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        ButterKnife.bind(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_list_details_RecyclerView);

        mShoppingId = getIntent().getStringArrayListExtra(SHOPPING_LIST_DETAILS).get(0);
        mShoppingName = getIntent().getStringArrayListExtra(SHOPPING_LIST_DETAILS).get(1);
        mShoppingOwner = getIntent().getStringArrayListExtra(SHOPPING_LIST_DETAILS).get(2);

        //Path to the current shopping list on Firebase
        mShoppingListReference = new Firebase(Utils.FIRE_BASE_SHOPPING_LIST_REFERENCE + userEmail + "/" + mShoppingId);
        //List name update
        bus.post(new ShoppingListServices.GetCurrentShoppingListRequest(mShoppingListReference));

        Firebase itemsReference = new Firebase(Utils.FIRE_BASE_LIST_ITEMS_REFERENCE + mShoppingId);

        adapter = new FirebaseRecyclerAdapter<Item, ListItemViewHolder>(Item.class,
                R.layout.list_shopping_item,
                ListItemViewHolder.class,
                itemsReference) {

            @Override
            protected void populateViewHolder(ListItemViewHolder listItemViewHolder, final Item item, int i) {
                listItemViewHolder.populate(item,getApplicationContext(),userEmail);

                //Delete item
                listItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> extraInfo = new ArrayList<>();
                        extraInfo.add(item.getId());
                        extraInfo.add(mShoppingId);
                        extraInfo.add(userEmail);

                        DialogFragment dialogFragment = DeleteItemDialogFragment.newInstance(extraInfo);
                        dialogFragment.show(getFragmentManager(),DeleteItemDialogFragment.class.getSimpleName());

                    }
                });

                //Long click
                listItemViewHolder.itemName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ArrayList<String> extraInfo = new ArrayList<>();
                        extraInfo.add(item.getId());
                        extraInfo.add(mShoppingId);
                        extraInfo.add(userEmail);
                        extraInfo.add(item.getItemName());

                        DialogFragment dialogFragment = ChangeItemNameDialogFragment.newInstance(extraInfo);
                        dialogFragment.show(getFragmentManager(),ChangeItemNameDialogFragment.class.getSimpleName());

                        return true;
                    }
                });

                listItemViewHolder.itemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bus.post(new ItemServices.ChangeBoughtItemStatusRequest(item,userEmail,mShoppingId));
                    }
                });


            }
        };
        getSupportActionBar().setTitle(mShoppingName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_details, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_list_name:
                ArrayList<String> shoppingListInfo = new ArrayList<>();
                shoppingListInfo.add(mShoppingId);
                shoppingListInfo.add(mShoppingName);
                DialogFragment dialogFragment = ChangeListNameDialogFragment.newInstance(shoppingListInfo);
                dialogFragment.show(getFragmentManager(),ChangeListNameDialogFragment.class.getSimpleName());
                return true;

            case R.id.action_delete_list:
                //Need to pass the variables defined in DeleteListDialogFragment.java
                DialogFragment dialogFragment1 = DeleteListDialogFragment.newInstance(mShoppingId,false);
                dialogFragment1.show(getFragmentManager(),DeleteListDialogFragment.class.getSimpleName());
                return true;

        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShoppingListReference.removeEventListener(mShoppingListListener);
        //Clean up the recyclerView
        adapter.cleanup();
        Log.i(ListDetailsActivity.class.getSimpleName(),"On destroy was called");
    }


    @OnClick(R.id.activity_list_details_FAB)
    public void setFloatingActionButton() {
        DialogFragment dialogFragment = AddItemDialogFragment.newInstances(mShoppingId);
        dialogFragment.show(getFragmentManager(),AddItemDialogFragment.class.getSimpleName());
    }


    //Subscribe the service to update the shopping list name on the activity title when it gets edited
    @Subscribe
    public void getCurrentShoppingList(ShoppingListServices.GetCurrentShoppingListResponse response) {
        mShoppingListListener = response.valueEventListener;
        mCurrentShoppingList = response.shoppingList;
        getSupportActionBar().setTitle(mCurrentShoppingList.getListName());
    }

}
