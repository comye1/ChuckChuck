package com.example.chuckchuck;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Frag3 extends Fragment {
    private View view;
    private TextView tv_user;
    private TextView tv_account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        tv_user = view.findViewById(R.id.tv_user);
        tv_account = view.findViewById(R.id.tv_account);
        tv_user.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
