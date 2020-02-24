package com.alienware.scan2shop.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.SearchActivity;

/**
 * Created by henry cheruiyot on 2/3/2018.
 */
public class SearchQuantityDialog extends DialogFragment {
    private static int Quantity = 1;
    private static String TAG = "SEARCH";
    private EditText item;
    private String name;
    private String description;
    private int price;
    private String image;

    @NonNull
    public Dialog onCreateDialog(Bundle paramBundle)
    {
        Object localObject = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Item Quantity");
        localObject = ((LayoutInflater)localObject).inflate(R.layout.dialog_layout, null);
        this.item = ((View)localObject).findViewById(R.id.noOfItems);
        this.item.setText("1");
        this.item.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable paramAnonymousEditable) {
                if (TextUtils.isEmpty(paramAnonymousEditable)) {
                    ((AlertDialog)SearchQuantityDialog.this.getDialog()).getButton(-1).setEnabled(false);
                    return;
                }
                ((AlertDialog)SearchQuantityDialog.this.getDialog()).getButton(-1).setEnabled(true);
            }

            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}

            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
        });
        builder.setView((View)localObject).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                ((InputMethodManager)SearchQuantityDialog.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchQuantityDialog.this.item.getWindowToken(), 2);
                Quantity=Integer.parseInt(item.getText().toString().trim());
                ((SearchActivity)getActivity()).sendResultToCart(Quantity, name,description, price,image);
            }
        }).setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                SearchQuantityDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    public void setData(String paramString,String description, int paramDouble,String image)
    {
        this.name = paramString;
        this.description=description;
        this.price = paramDouble;
        this.image=image;
    }
}
