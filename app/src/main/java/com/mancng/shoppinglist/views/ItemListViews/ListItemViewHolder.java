package com.mancng.shoppinglist.views.ItemListViews;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.entities.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_shopping_item_itemName)
    public TextView itemName;

    @BindView(R.id.list_shopping_item_itemView)
    public ImageView itemView;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void populate(Item item, Context context, String currentUserEmail) {
        itemView.setTag(item);
        itemName.setText(item.getItemName());

        if (item.isBought()) {
            itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemView.setImageResource(R.mipmap.ic_check);

        } else {
            itemName.setPaintFlags(itemName.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG));
            itemView.setImageResource(R.mipmap.ic_trashcan);
        }
    }
}
