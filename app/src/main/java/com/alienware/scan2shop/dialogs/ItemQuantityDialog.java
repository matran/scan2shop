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
import com.alienware.scan2shop.activity.MainActivity;
import com.alienware.scan2shop.helpers.ItemHelper;

/**
 * Created by henry cheruiyot on 2/3/2018.
 */
public class ItemQuantityDialog extends DialogFragment {
    private static int Quantity = 1;
    private EditText item;
    private int position;
    private int price;
    private String barcode;
    private boolean status=false;

    public void setStatus(boolean status){
          this.status=status;
      }
      public void setData(int position,int price,int quantity){
        this.position=position;
        this.price=price;
        Quantity=quantity;
      }
      public void setBarcode(String barcode){
          this.barcode=barcode;
      }



    @NonNull
    public Dialog onCreateDialog(Bundle paramBundle) {
        Object localObject = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Item Quantity");
        View view= ((LayoutInflater)localObject).inflate(R.layout.dialog_layout, null);
        this.item = view.findViewById(R.id.noOfItems);
        this.item.setText(String.valueOf(Quantity));
        this.item.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable paramAnonymousEditable) {
                if (TextUtils.isEmpty(paramAnonymousEditable))
                {
                    ((AlertDialog)ItemQuantityDialog.this.getDialog()).getButton(-1).setEnabled(false);
                    return;
                }
                ((AlertDialog)ItemQuantityDialog.this.getDialog()).getButton(-1).setEnabled(true);
            }

            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}

            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
        });
        builder.setView(view).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                if(status){

                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ItemQuantityDialog.this.item.getWindowToken(), 2);
                    Quantity=Integer.parseInt(item.getText().toString().trim());
                    int total2=price * Quantity;
                    ItemHelper itemHelper=new ItemHelper(getContext());
                    itemHelper.updateQuantity(Quantity,total2,position);
                    ((MainActivity)getActivity()).refreshFragment();
                    dismiss();
                    status=false;
                }else {
                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ItemQuantityDialog.this.item.getWindowToken(), 2);
                    Quantity=Integer.parseInt(item.getText().toString().trim());
                    ((MainActivity)getActivity()).fetchProduct(barcode,Quantity);
                    dismiss();
                }
            }
        }).setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                getDialog().cancel();

            }
        });

        return builder.create();
    }
}