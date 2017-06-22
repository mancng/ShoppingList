package com.mancng.shoppinglist.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.services.ShoppingListServices;

public class DeleteListDialogFragment extends BaseDialog implements View.OnClickListener{

    public static final String EXTRA_SHOPPING_LIST_ID = "EXTRA_SHOPPING_LIST_ID";
    public static final String EXTRA_BOOLEAN = "EXTRA BOOLEAN";

    private String mShoppingListId;
    private Boolean mIsLongClicked;

    public static DeleteListDialogFragment newInstance(String shoppingListId, boolean isLongClicked) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_SHOPPING_LIST_ID,shoppingListId);
        arguments.putBoolean(EXTRA_BOOLEAN, isLongClicked);

        DeleteListDialogFragment dialogFragment = new DeleteListDialogFragment();
        dialogFragment.setArguments(arguments);

        return dialogFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingListId = getArguments().getString(EXTRA_SHOPPING_LIST_ID);
        mIsLongClicked = getArguments().getBoolean(EXTRA_BOOLEAN);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_list, null))
                .setPositiveButton("Yes",null)
                .setNegativeButton("Cancel",null)
                .setTitle("Delete Shopping List?")
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return dialog;

    }

    @Override
    public void onClick(View view) {
        if (mIsLongClicked) {
            dismiss();
            bus.post(new ShoppingListServices.DeleteShoppingListRequest(userEmail,mShoppingListId));
        } else {
            dismiss();
            getActivity().finish();
            bus.post(new ShoppingListServices.DeleteShoppingListRequest(userEmail,mShoppingListId));
        }
    }
}
