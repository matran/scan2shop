package com.alienware.scan2shop.dialogs;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.MyListActivity;

/**
 * Created by henry cheruiyot on 2/3/2018.
 */
public class MyListDialog extends DialogFragment {
    private Button mylistDelete;
    private EditText name;
    public String nameI=" ";
    public String priceM="0.00";
    private int position;
    private int position2;
    private EditText quantityEdit;
    public String quantityM="1";
    public String total2;
    private EditText unitpriceEdit;
    @NonNull
    public Dialog onCreateDialog(Bundle paramBundle) {
        Object localObject = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = ((LayoutInflater)localObject).inflate(R.layout.mylist_dialog, null);
        this.mylistDelete = view.findViewById(R.id.myListDeleteButton);
        this.quantityEdit = view.findViewById(R.id.quantityEditText);
        this.unitpriceEdit = view.findViewById(R.id.editTextUnitPrice);
        this.name = view.findViewById(R.id.mylistName);
        String q=quantityM.replace("(","");
        String x=q.replace(")","");
        String h=priceM.replace("ksh.","");
        String f=h.replace(".0","");
        if( !priceM.isEmpty() && !quantityM.isEmpty()) {
            this.name.setText(nameI);
            this.quantityEdit.setText(x);
            this.unitpriceEdit.setText(f);
        }else {

            name.setText(nameI);
            quantityEdit.setText("1");
            unitpriceEdit.setText("0");
        }
        this.name.addTextChangedListener(new MyTextWatcher(this.name));
        this.quantityEdit.addTextChangedListener(new MyTextWatcher(this.quantityEdit));
        this.unitpriceEdit.addTextChangedListener(new MyTextWatcher(this.unitpriceEdit));
        this.mylistDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView)
            {
              ((MyListActivity)MyListDialog.this.getActivity()).deleteData(position,position2);
                getDialog().cancel();
            }
        });
        builder.setView(view).setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                quantityM = quantityEdit.getText().toString().trim();
                priceM = unitpriceEdit.getText().toString().trim();
                nameI = name.getText().toString().trim();
                ((MyListActivity)getActivity()).updateListView(nameI, priceM, quantityM,position,position2);
            }
        }).setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                getDialog().cancel();
            }
        });
        return builder.create();
    }

    public void setData(String paramString1, String paramString2, String paramString3,int position,int position2,String total)
    {
        this.quantityM = paramString2;
        this.priceM = paramString3;
        this.nameI = paramString1;
        this.position=position;
        this.position2=position2;
        total2=total;
    }

    class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View paramView)
        {
            this.view = paramView;
        }

        public void afterTextChanged(Editable paramEditable)
        {
            if (TextUtils.isEmpty(paramEditable))
            {
                ((AlertDialog)MyListDialog.this.getDialog()).getButton(-1).setEnabled(false);
                return;
            }
            ((AlertDialog)MyListDialog.this.getDialog()).getButton(-1).setEnabled(true);
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }
}
