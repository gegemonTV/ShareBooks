package com.minerva.sharebooks.ui.share_books;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.minerva.sharebooks.R;
import com.minerva.sharebooks.auth.sign_in.SignInFragment;
import com.minerva.sharebooks.auth.sign_up.SignUpFragment;

public class ShareBooksFragment extends Fragment {

    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        View root;
        if (mAuth.getCurrentUser() != null) {

            root = inflater.inflate(R.layout.fragment_share_books, container, false);
        } else {
            root = inflater.inflate(R.layout.fragment_signed_out, container, false);

            Button signIn = root.findViewById(R.id.sign_in_btn);
            Button signUp = root.findViewById(R.id.sign_up_btn);

            signIn.setOnClickListener(view -> {
                Log.i("B", "Started fragment transition");
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, new SignInFragment())
                        .addToBackStack(null)
                        .commit();
            });

            signUp.setOnClickListener(view -> {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, new SignUpFragment())
                        .addToBackStack(null)
                        .commit();
            });
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}