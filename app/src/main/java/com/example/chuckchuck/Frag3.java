package com.example.chuckchuck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Frag3 extends Fragment{
    private View view, dialogView;
    private TextView tv_user, tv_logout, tv_revoke;
    private ImageButton btn_addSubject;
    private ListView lv_timetable;
    private AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<String> subjectList;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);

        lv_timetable = view.findViewById(R.id.lv_timetable);
        tv_user = view.findViewById(R.id.tv_user);
        tv_logout = view.findViewById(R.id.tv_logout);
        tv_revoke = view.findViewById(R.id.tv_revoke);
        btn_addSubject = view.findViewById(R.id.btn_addSubject);


        mDatabase = FirebaseDatabase.getInstance().getReference();




        //계정 정보 보여줌
        mAuth = FirebaseAuth.getInstance();
        tv_user.setText(mAuth.getCurrentUser().getEmail());
        //show linear2

        //초기 화면
        subjectList = new ArrayList<>();
        setTimeTableList();

        //로그아웃 대화상자
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getActivity());
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
                builder = new AlertDialog.Builder(getActivity());
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
        btn_addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectDialog();
            }
        });
        return view;

    }

    private void subjectDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_timetable, null);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("과목 추가");
        builder.setView(dialogView);
        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et_name = dialogView.findViewById(R.id.et_subjectName);
                        String subjectname = et_name.getText().toString();
                        if(subjectname.replace(" ", "").equals("")){
                            Toast.makeText(getContext(), "취소되었습니다." , Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CheckBox [] checkboxes =
                                        {dialogView.findViewById(R.id.cb_sun),
                                        dialogView.findViewById(R.id.cb_mon),
                                        dialogView.findViewById(R.id.cb_tue),
                                        dialogView.findViewById(R.id.cb_wed),
                                        dialogView.findViewById(R.id.cb_thu),
                                        dialogView.findViewById(R.id.cb_fri),
                                        dialogView.findViewById(R.id.cb_sat)};
                        String checked = "";
                        for(int i=0; i<7; i++){
                            checked += (checkboxes[i].isChecked())? "1" : "0";
                        }
                        //firebase와 listview에 추가하기
                        addToTimeTableList(subjectname, checked);
                        Toast.makeText(getContext(), subjectname + "추가되었습니다." , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소되었습니다." , Toast.LENGTH_SHORT).show();

                    }
                }).show();
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        getActivity().finishAffinity();
    }

    private void revokeAccess(){
        mAuth.getCurrentUser().delete();
        getActivity().finishAffinity();
    }

    private void addToTimeTableList(String subjectName, String checked){
        mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable").child(subjectName).setValue(checked);
        adapter.notifyDataSetChanged();

    }

    private void setTimeTableList(){
        //firebase에서 읽어오기

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjectList);
        lv_timetable.setAdapter(adapter);

        mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            subjectList.add(0, snapshot.getKey());
                        }
//                        subjectList.add(0,dataSnapshot.getValue().toString());//dataSnapshot.getValue().toString()
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        subjectList.add(0,dataSnapshot.getKey());//dataSnapshot.getValue().toString()
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        subjectList.add(0,dataSnapshot.getKey());//dataSnapshot.getValue().toString()
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

    }

}
