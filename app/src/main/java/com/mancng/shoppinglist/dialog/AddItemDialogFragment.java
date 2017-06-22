package com.mancng.shoppinglist.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.services.ItemServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddItemDialogFragment extends BaseDialog implements View.OnClickListener{


    @BindView(R.id.dialog_add_item_editText)
    EditText itemName;

    public static final String SHOPPING_LIST_ID = "SHOPPING_LIST_ID";

    private String mShoppingListId;

    public static AddItemDialogFragment newInstances(String shoppingListId) {
        Bundle arguements = new Bundle();
        arguements.putString(SHOPPING_LIST_ID, shoppingListId);
        AddItemDialogFragment dialogFragment = new AddItemDialogFragment();
        dialogFragment.setArguments(arguements);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingListId = getArguments().getString(SHOPPING_LIST_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_item,null);
        ButterKnife.bind(this, rootView);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton("Add Item", null)
                .setNegativeButton("Cancel", null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        bus.post(new ItemServices.AddItemRequest(mShoppingListId,itemName.getText().toString(),userEmail));
    }

    //Response
    @Subscribe
    public void AddItem(ItemServices.AddItemResponse response) {
        if (!response.didSucceed()) {
            itemName.setError(response.getPropertyError("itemName"));
        } else {
            dismiss();
        }
    }
}
