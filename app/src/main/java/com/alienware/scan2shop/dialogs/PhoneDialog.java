package com.alienware.scan2shop.dialogs;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.alienware.scan2shop.R;
import com.alienware.scan2shop.activity.ProfileActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
public class PhoneDialog extends DialogFragment {
    private TextInputLayout tilPhone;
    private TextInputEditText etPhone;
    private Button cancelButton;
    private Button saveButton;
    private String phone;
    private boolean errorflag=false;

    public static PhoneDialog newInstance(String phone ) {
        PhoneDialog f = new PhoneDialog();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        f.setArguments(args);
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_phone_layout, container, false);
        etPhone=v.findViewById(R.id.etPhoneNumber);
        tilPhone=v.findViewById(R.id.tilPhoneNumber);
        cancelButton=v.findViewById(R.id.cancelButton);
        saveButton=v.findViewById(R.id.saveButton);
        etPhone.setText(phone.substring(3));
        etPhone.addTextChangedListener(new MyTextWatcher(etPhone));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneno=etPhone.getText().toString().trim();
                if(!phoneno.isEmpty() && !errorflag) {
                    ((ProfileActivity) Objects.requireNonNull(getActivity())).sendPhoneForVerification(phoneno);
                    dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone= getArguments().getString("phone");
    }

    private boolean validatePhoneNumber(){
        String phonenumber = etPhone.getText().toString().trim();
        if (phonenumber.isEmpty()) {
            this.errorflag = true;
            tilPhone.setError("phone number is required");
            return false;
        }else if(phonenumber.length()<9){
            this.errorflag = true;
            tilPhone.setError("phone number must have 9 characters");
            return false;
        }
        this.errorflag = false;

        tilPhone.setErrorEnabled(false);
        return true;
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View paramView)
        {
            this.view = paramView;
        }
        public void afterTextChanged(Editable paramEditable)
        {
            if (this.view.getId() == R.id.etPhoneNumber) {
                validatePhoneNumber();
                return;
            }
        }
        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }

}
