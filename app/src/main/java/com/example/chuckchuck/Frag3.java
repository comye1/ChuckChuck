package com.example.chuckchuck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Frag3 extends Fragment{
    private View view;
    private TextView tv_user, tv_logout, tv_revoke, tv_timetable1, tv_timetable2;
    private LinearLayout linear1, linear2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);

        linear1 = view.findViewById(R.id.linear1);
        linear2 = view.findViewById(R.id.linear2);
        tv_timetable1 = view.findViewById(R.id.tv_timetable1);
        tv_timetable2 = view.findViewById(R.id.tv_timetable2);
        tv_user = view.findViewById(R.id.tv_user);
        tv_logout = view.findViewById(R.id.tv_logout);
        tv_revoke = view.findViewById(R.id.tv_revoke);

        //초기 화면
        showLinear1();


        //계정 정보 보여줌
        tv_user.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //show linear2
        tv_timetable1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinear2();
            }
        });
        //show linear1
        tv_timetable2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinear1();
            }
        });
        //로그아웃 대화상자
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //탈퇴 대화상자
        tv_revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }

    private void showLinear1(){
        linear1.setVisibility(View.VISIBLE);
        linear2.setVisibility(View.INVISIBLE);
    }

    private void showLinear2(){
        linear2.setVisibility(View.VISIBLE);
        linear1.setVisibility(View.INVISIBLE);
    }
}
