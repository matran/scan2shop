package com.alienware.scan2shop.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.MainActivity;
import com.alienware.scan2shop.helpers.ItemHelper;

/**
 * Created by henry cheruiyot on 3/26/2018.
 */
public class ConfirmDialog extends DialogFragment {

    private TextView codeView;
    private int position;
    private boolean status=false;
    ItemHelper itemHelper;
    public void setPosition(int position){
        this.position=position;
    }

    public void setFlag(boolean status){
        this.status=status;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle paramBundle) {
        Object localObject = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         itemHelper=new ItemHelper(getContext());
        View view= ((LayoutInflater)localObject).inflate(R.layout.sure_delete, null);
        codeView= view.findViewById(R.id.suredelete);
        codeView.setText("Are you sure you want to delete?");
        builder.setTitle("Scan2Shop");
        builder.setView(view).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                if(status){
                    itemHelper.deleteAllItems();
                    ((MainActivity) getActivity()).refreshFragment();
                    status=false;
                }else {
                    itemHelper.deleteItem(position);
                    ((MainActivity) getActivity()).refreshFragment();
                }
            }
        }).setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                getDialog().cancel();
            }
        });
        return builder.create();
    }
}
