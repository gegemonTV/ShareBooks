package com.minerva.sharebooks.auth.sign_in;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.minerva.sharebooks.R;
import com.minerva.sharebooks.util.Utilities;

public class SignInFragment extends Fragment {

    FirebaseAuth mAuth;

    TextInputEditText nickname, email, password;
    TextInputLayout email_l, password_l;

    MaterialButton signInBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();

        email_l = root.findViewById(R.id.email_layout);

        email = root.findViewById(R.id.email_input);
        password = root.findViewById(R.id.password_input);

        signInBtn = root.findViewById(R.id.sign_in_btn);

        MaterialToolbar topBar = root.findViewById(R.id.top_bar);
        topBar.setNavigationOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_l.setError(null);
                email_l.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Utilities.isValidEmail(charSequence)){
                    email_l.setError(null);
                    email_l.setErrorEnabled(false);
                } else {
                    email_l.setError("Invalid Email!");
                    email_l.setErrorEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utilities.isValidEmail(editable.toString())){
                    email_l.setError(null);
                    email_l.setErrorEnabled(false);
                } else {
                    email_l.setError("Invalid Email!");
                    email_l.setErrorEnabled(true);
                }
            }
        });

        signInBtn.setOnClickListener(view -> {
            signInBtn.setEnabled(false);
            if (email.getText().toString().equals("")){
                signInBtn.setEnabled(true);
                Snackbar.make(view, "Email can not be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            if (password.getText().toString().equals("")){
                signInBtn.setEnabled(true);
                Snackbar.make(view, "Password can not be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            mAuth
                    .signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){

                            getParentFragmentManager().popBackStack();
                            signInBtn.setEnabled(true);
                        } else {
                            signInBtn.setEnabled(true);
                            Snackbar.make(view, task.getException().getLocalizedMessage().toString(), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });

        });


        return root;
    }
}
