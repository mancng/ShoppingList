package com.mancng.shoppinglist.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddListDialogFragment extends BaseDialog implements View.OnClickListener{

    @BindView(R.id.dialog_add_list_editText)
    EditText newListName;

    public static AddListDialogFragment newInstance() {
        return new AddListDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View rootView = layoutInflater.inflate(R.layout.dialog_add_list,null);
        ButterKnife.bind(this,rootView);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton("Create",null)
                .setNegativeButton("Cancel",null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        bus.post(new ShoppingListServices.AddShoppingListRequest(newListName.getText().toString(),userName,userEmail));
    }

    //Response
    @Subscribe
    public void AddShoppingList(ShoppingListServices.AddShoppingListResponse response) {
        if (!response.didSucceed()) {
            newListName.setError(response.getPropertyError("listName"));
        } else {
            dismiss();
        }
    }
}
