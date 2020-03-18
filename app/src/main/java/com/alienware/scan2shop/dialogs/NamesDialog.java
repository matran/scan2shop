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

public class NamesDialog  extends DialogFragment {
private TextInputLayout tilFirstName;
private TextInputLayout tilLastName;
private TextInputEditText etFirstName;
private TextInputEditText etLastName;
private Button cancelButton;
private Button saveButton;
private String firstName;
private String lastName;
private boolean errorFlag=false;
     public static NamesDialog newInstance(String firstName,String lastname) {
        NamesDialog f = new NamesDialog();
        Bundle args = new Bundle();
        args.putString("first_name", firstName);
        args.putString("last_name",lastname);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_names_layout, container, false);
        tilFirstName=v.findViewById(R.id.tilFirstName);
        tilLastName=v.findViewById(R.id.tilLastName);
        etFirstName=v.findViewById(R.id.etFirstName);
        etLastName=v.findViewById(R.id.etLastName);
        cancelButton=v.findViewById(R.id.cancelButton);
        saveButton=v.findViewById(R.id.saveButton);
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etFirstName.addTextChangedListener(new MyTextWatcher(etFirstName));
        etLastName.addTextChangedListener(new MyTextWatcher(etLastName));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname=etFirstName.getText().toString().trim();
                String lastname=etLastName.getText().toString().trim();
                if(!firstname.isEmpty()&&!lastname.isEmpty() && !errorFlag) {
                    ((ProfileActivity) Objects.requireNonNull(getActivity())).updateNames(firstname, lastname);

                }
                dismiss();
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
        firstName= getArguments().getString("first_name");
        lastName=getArguments().getString("last_name");
    }
    private boolean validateNameFirst()
    {
        String firstname= etFirstName.getText().toString().trim();
        if (firstname.isEmpty())
        {
            errorFlag=true;
            tilFirstName.setError("Empty field first name");
            return false;
        }
        errorFlag = false;
        tilFirstName.setErrorEnabled(false);
        return true;
    }

    private boolean validateNameLast()
    {
        String lastname = etLastName.getText().toString().trim();
        if (lastname.isEmpty())
        {
            errorFlag = true;
            tilLastName.setError("Empty field last name");
            return false;
        }
        errorFlag = false;
        tilLastName.setErrorEnabled(false);
        return true;
    }
    private class MyTextWatcher implements TextWatcher
    {
        private View view;

        private MyTextWatcher(View paramView)
        {
            this.view = paramView;
        }

        public void afterTextChanged(Editable paramEditable)
        {
            switch (this.view.getId())
            {

                case R.id.etFirstName:
                    validateNameFirst();
                    return;
                case R.id.etLastName:
                    validateNameLast();
                    return;
                default:
            }

        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    }
}
