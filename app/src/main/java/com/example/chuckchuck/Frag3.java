package com.example.chuckchuck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Frag3 extends Fragment{
    private View view;
    private TextView tv_user, tv_logout, tv_revoke;
    private ImageButton btn_addSubject;
    private ListView lv_timetable;
    private AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<String> subjectList;
    private List<String> dayList; //요일 배열 저장
    private List<String> keyList; //key 배열 저장
    private ArrayAdapter<String> adapter;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

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
        dayList = new ArrayList<>();
        keyList = new ArrayList<>();

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
                subjectAdd();
            }
        });
        return view;

    }
    //시간표 과목 추가 함수
    private void subjectAdd(){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_timetable, null);
        final EditText et_name = dialogView.findViewById(R.id.et_subjectName);
        final CheckBox [] checkboxes =
                {dialogView.findViewById(R.id.cb_sun),
                        dialogView.findViewById(R.id.cb_mon),
                        dialogView.findViewById(R.id.cb_tue),
                        dialogView.findViewById(R.id.cb_wed),
                        dialogView.findViewById(R.id.cb_thu),
                        dialogView.findViewById(R.id.cb_fri),
                        dialogView.findViewById(R.id.cb_sat)};
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("과목" + " 추가하기");
        builder.setView(dialogView);
        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subjectname = et_name.getText().toString();
                if(subjectname.replace(" ", "").equals("")){
                    Toast.makeText(mContext, "취소되었습니다." , Toast.LENGTH_SHORT).show();
                    return ;
                }

                String checked = "";
                for(int i=0; i<7; i++){
                    checked += (checkboxes[i].isChecked())? "1" : "0";
                }
                //firebase와 listview에 추가하기
                Toast.makeText(mContext, "추가되었습니다", Toast.LENGTH_SHORT).show();
                addToList(subjectname, checked);
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext,"취소되었습니다." , Toast.LENGTH_SHORT).show();

                    }
                }).show();
    }

    //시간표 과목 수정, 삭제 함수
    private void subjectRevise(final String name, final String array, final int position){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_timetable, null);
        final EditText et_name = dialogView.findViewById(R.id.et_subjectName);
        et_name.setText(name);
        final CheckBox [] checkboxes =
                {dialogView.findViewById(R.id.cb_sun),
                        dialogView.findViewById(R.id.cb_mon),
                        dialogView.findViewById(R.id.cb_tue),
                        dialogView.findViewById(R.id.cb_wed),
                        dialogView.findViewById(R.id.cb_thu),
                        dialogView.findViewById(R.id.cb_fri),
                        dialogView.findViewById(R.id.cb_sat)};
        for(int i=0; i<7; i++){
            if(array.charAt(i)=='1'){
                checkboxes[i].setChecked(true);
            }
        }
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("과목 수정");
        builder.setView(dialogView);
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subjectname = et_name.getText().toString();
                        if(subjectname.replace(" ", "").equals("")){ //공백 여부 검사
                            Toast.makeText(mContext, "취소되었습니다." , Toast.LENGTH_SHORT).show();
                            return ;
                        }

                        String checked = "";
                        for(int i=0; i<7; i++){
                            checked += (checkboxes[i].isChecked())? "1" : "0";
                        }
                        //firebase와 listview에 추가하기
                        modifyList(subjectname, checked, position);//
                        Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromList(position);
                        Toast.makeText(mContext,"삭제되었습니다." , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext,"취소되었습니다." , Toast.LENGTH_SHORT).show();

                    }
                }).show();
    }

    //로그아웃
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        getActivity().finishAffinity();
    }
    //계정삭제(탈퇴)
    private void revokeAccess(){
        mDatabase.child("Users").child(mAuth.getUid()).removeValue();
        mAuth.getCurrentUser().delete();
        FirebaseAuth.getInstance().signOut();
        getActivity().finishAffinity();
    }
    //새로 추가
    private void addToList(String subjectName, String days){
        String key ;
        DatabaseReference reference = mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable").push();

        SubjectInfo subjectInfo = new SubjectInfo(subjectName, days);
        key = reference.getKey();
        reference.setValue(subjectInfo);
//        reference.child("subject").setValue(subjectName);
//        reference.child("days").setValue(days);
        subjectList.add(subjectName);
        dayList.add(days);
        keyList.add(key);

    }
    //수정
    private void modifyList(String subjectName, String days, int position){
        String key = keyList.get(position);
        DatabaseReference reference = mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable").child(key);

        SubjectInfo subjectInfo = new SubjectInfo(subjectName, days);
        reference.setValue(subjectInfo);

        subjectList.set(position, subjectName);
        dayList.set(position, days);
        adapter.notifyDataSetChanged();

    }
    //삭제
    private void deleteFromList(int position){
        String key = keyList.get(position);
        DatabaseReference reference = mDatabase.child("Users").child(mAuth.getUid());

        reference.child("TimeTable").child(key).removeValue();
        reference.child("Records").child(key).removeValue();


        subjectList.remove(position);
        dayList.remove(position);
        keyList.remove(position);

        adapter.notifyDataSetChanged();
    }
    //읽어오기
    private void setTimeTableList(){
        //firebase에서 읽어오기
        adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, subjectList);
        lv_timetable.setAdapter(adapter);
        lv_timetable.setOnItemClickListener(onItemClickListener);

        mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        subjectList.clear();
                        dayList.clear();
                        keyList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            SubjectInfo subjectInfo = snapshot.getValue(SubjectInfo.class);
                            subjectList.add(subjectInfo.getSubject()); //key : 과목명
                            dayList.add(subjectInfo.getDays()); //value : 요일 string
                            keyList.add(snapshot.getKey());
                            //getValue로 요일 정보 읽어와서
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    //리스트 아이템 클릭리스너 - 수정/삭제 대화상자 띄움
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            subjectRevise(subjectList.get(position), dayList.get(position), position);
        }
    };


}

