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
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Frag3 extends Fragment{
    private View view;
    private TextView tv_user, tv_logout, tv_revoke, tv_timetable1, tv_timetable2;
    private LinearLayout linear1, linear2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //초기 화면
        showLinear1();


        //계정 정보 보여줌
        mAuth = FirebaseAuth.getInstance();
        tv_user.setText(mAuth.getCurrentUser().getEmail());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그아웃 하시겠습니까?");
                builder.setMessage("기록한 내용은 사라지지 않습니다.");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        signOut();
                    }
                });
                builder.setNegativeButton("취소",null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //탈퇴 대화상자
        tv_revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("탈퇴 하시겠습니까?");
                builder.setMessage("기록한 내용이 모두 사라집니다.");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        revokeAccess();
                    }
                });
                builder.setNegativeButton("취소", null);

                AlertDialog dialog = builder.create();
                dialog.show();
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

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        getActivity().finishAffinity();
    }

    private void revokeAccess(){
        mAuth.getCurrentUser().delete();
        getActivity().finishAffinity();
    }
}
