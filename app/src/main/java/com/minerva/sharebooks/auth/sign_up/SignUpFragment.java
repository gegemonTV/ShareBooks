package com.minerva.sharebooks.auth.sign_up;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.minerva.sharebooks.R;
import com.minerva.sharebooks.util.Utilities;

public class SignUpFragment extends Fragment {

    FirebaseAuth mAuth;

    TextInputEditText nickname, email, password;
    TextInputLayout email_l, password_l;

    MaterialButton signUpBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        email_l = root.findViewById(R.id.email_layout);
        password_l = root.findViewById(R.id.password_layout);

        email = root.findViewById(R.id.email_input);
        password = root.findViewById(R.id.password_input);
        nickname = root.findViewById(R.id.nickname_input);

        signUpBtn = root.findViewById(R.id.sign_up_btn);

        MaterialToolbar topBar = root.findViewById(R.id.top_bar);
        topBar.setNavigationOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_l.setErrorEnabled(false);
                password_l.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 6){
                    password_l.setError("Password must have length more than 6 chars!");
                    password_l.setErrorEnabled(true);
                } else{
                    password_l.setErrorEnabled(false);
                    password_l.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 6){
                    password_l.setError("Password must have length more than 6 chars!");
                    password_l.setErrorEnabled(true);
                } else{
                    password_l.setErrorEnabled(false);
                    password_l.setError(null);
                }
            }
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

        signUpBtn.setOnClickListener(view -> {
            signUpBtn.setEnabled(false);
            if (nickname.getText().toString().equals("")){
                signUpBtn.setEnabled(true);
                Snackbar.make(view, "Nickname can not be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            if (email.getText().toString().equals("")){
                signUpBtn.setEnabled(true);
                Snackbar.make(view, "Email can not be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            if (password.getText().toString().equals("")){
                signUpBtn.setEnabled(true);
                Snackbar.make(view, "Password can not be empty!", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }
            if (password.getText().toString().length() < 6){
                signUpBtn.setEnabled(true);
                Snackbar.make(view, "Password can not be less then 6 chars length!", BaseTransientBottomBar.LENGTH_LONG).show();
                return;
            }

            mAuth
                .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        task
                                .getResult()
                                .getUser()
                                .updateProfile(new UserProfileChangeRequest
                                        .Builder()
                                .setDisplayName(nickname.getText().toString())
                                .build());
                        getParentFragmentManager().popBackStack();
                        signUpBtn.setEnabled(true);
                    } else {
                        signUpBtn.setEnabled(true);
                        Snackbar.make(view, task.getException().getLocalizedMessage().toString(), BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });

        });


        return root;
    }
}
