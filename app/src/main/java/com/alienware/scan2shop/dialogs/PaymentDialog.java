package com.alienware.scan2shop.dialogs;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.TransparentActivity;
import com.alienware.scan2shop.helpers.ItemHelper;
import com.alienware.scan2shop.helpers.UserSqliteHandler;

import java.util.Objects;

/**
 * Created by henry cheruiyot on 3/27/2018.
 */
public class PaymentDialog extends DialogFragment {
    private TextView phoneno;
    private TextView amount;
    String txPhoneno;
    String txAmount;
    @NonNull
    public Dialog onCreateDialog(Bundle paramBundle){
        Object localObject = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= ((LayoutInflater)localObject).inflate(R.layout.confirm_payment, null);
        phoneno= view.findViewById(R.id.c2);
        amount= view.findViewById(R.id.c3);
        ItemHelper bproductHelper = new ItemHelper(getActivity());
        UserSqliteHandler user=new UserSqliteHandler(getActivity());
        txAmount=String.valueOf(bproductHelper.getDbTotal());
        txPhoneno=user.getPhoneNo();
        amount.setText("ksh." + txAmount +".00");
        phoneno.setText(txPhoneno);
        builder.setTitle("MPESA Payment Confirmation");
        builder.setView(view).setPositiveButton(R.string.proceed_payment, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                Intent intent=new Intent(getActivity(), TransparentActivity.class);
                startActivity(intent);

            }
        }).setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
            Objects.requireNonNull(getDialog()).cancel();
        }
    });
        return builder.create();
    }
}
