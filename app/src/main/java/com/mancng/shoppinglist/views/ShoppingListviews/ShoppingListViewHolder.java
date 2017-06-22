package com.mancng.shoppinglist.views.ShoppingListviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.entities.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_shopping_list_listOwnerName)
    TextView ownerName;

    @BindView(R.id.list_shopping_list_listName)
    TextView listName;

    @BindView(R.id.list_shopping_list_dateCreated)
    TextView dateCreated;

    @BindView(R.id.list_shopping_list_layout)
    public View layout;

    public ShoppingListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void populate (ShoppingList shoppingList) {
        ownerName.setText(shoppingList.getOwnerName());
        listName.setText(shoppingList.getListName());

        //Since dateCreated is in a Hashmap, we'll need to make sure it's not null:
        if (shoppingList.getDateCreated().get("timestamp") != null) {
            dateCreated.setText(convertTime((long)shoppingList.getDateCreated().get("timestamp")));
        }
    }

    //Convert Unix timestamp
    private String convertTime(Long unixTime) {
        Date dateObject = new Date(unixTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy kk:mm");
        return simpleDateFormat.format(dateObject);
    }


}
