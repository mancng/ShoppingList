package com.mancng.shoppinglist.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.services.ItemServices;

import java.util.ArrayList;

public class DeleteItemDialogFragment extends BaseDialog implements View.OnClickListener {

    public static final String EXTRA_INFO = "EXTRA_INFO";

    public static DeleteItemDialogFragment newInstance(ArrayList<String> extraInfo) {
        Bundle arguements = new Bundle();
        arguements.putStringArrayList(EXTRA_INFO, extraInfo);
        DeleteItemDialogFragment deleteItemDialogFragment = new DeleteItemDialogFragment();
        deleteItemDialogFragment.setArguments(arguements);

        return deleteItemDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_item, null))
                .setPositiveButton("Confirm", null)
                .setNegativeButton("Cancel", null)
                .setTitle("Delete Item?")
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        bus.post(new ItemServices.DeleteItemRequest(getArguments().getStringArrayList(EXTRA_INFO).get(0),
                getArguments().getStringArrayList(EXTRA_INFO).get(1),
                getArguments().getStringArrayList(EXTRA_INFO).get(2)
        ));
        //Since there will be no response for delete, we're going to dismiss
        dismiss();
    }
}
