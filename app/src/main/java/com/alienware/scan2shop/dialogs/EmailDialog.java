package com.alienware.scan2shop.dialogs;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
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
public class EmailDialog extends DialogFragment {
    private TextInputLayout tilEmailAddress;
    private TextInputEditText etEmailAddress;
    private Button cancelButton;
    private Button saveButton;
    private String emailAddress;
    private boolean errorflag=false;
    public static EmailDialog newInstance(String email_address ) {
        EmailDialog f = new EmailDialog();
        Bundle args = new Bundle();
        args.putString("email", email_address);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_email_layout, container, false);
        tilEmailAddress=v.findViewById(R.id.tilEmail);
         etEmailAddress=v.findViewById(R.id.etEmail);
        cancelButton=v.findViewById(R.id.cancelButton);
        saveButton=v.findViewById(R.id.saveButton);
        etEmailAddress.setText(emailAddress);
        etEmailAddress.addTextChangedListener(new MyTextWatcher(etEmailAddress));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmailAddress.getText().toString().trim();
                if(!email.isEmpty() && !errorflag) {
                    ((ProfileActivity) Objects.requireNonNull(getActivity())).updateEmail(email);
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
        emailAddress= getArguments().getString("email");
    }
    private static boolean isValidEmail(String paramString){
        return (!TextUtils.isEmpty(paramString)) && (Patterns.EMAIL_ADDRESS.matcher(paramString).matches());
    }
    private boolean validateEmail() {
        String str = etEmailAddress.getText().toString().trim();
        if ((str.isEmpty()) || (!isValidEmail(str))) {
            errorflag = true;
            tilEmailAddress.setError("invalid email address");
            return false;
        }
        errorflag = false;
        tilEmailAddress.setErrorEnabled(false);
        return true;
    }
    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View paramView){
            this.view = paramView;
        }
        public void afterTextChanged(Editable paramEditable){
            if (this.view.getId() == R.id.etEmail) {
                validateEmail();
                return;
            }
        }
        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }
}
