package com.example.chuckchuck;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wefika.flowlayout.FlowLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class Frag1 extends Fragment {
    private View view;
    private View keywordView, record1;
    private TextView tv_date;
    private LinearLayout linearScroll;
    private LayoutInflater linflater;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static int dayToday;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);
        tv_date = view.findViewById(R.id.tv_date);
        tv_date.setText(getDate());
        linearScroll = view.findViewById(R.id.linear_scroll);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        createTodayRecords();


        return view;
    }

    String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M월 d일 ");
        String day = simpleDateFormat.format(new Date());
        dayToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK); //오늘 요일 저장
        switch(dayToday){
            case 1:
                day += "일요일";
                break;
            case 2:
                day += "월요일";
                break;
            case 3:
                day += "화요일";
                break;
            case 4:
                day += "수요일";
                break;
            case 5:
                day += "목요일";
                break;
            case 6:
                day += "금요일";
                break;
            case 7:
                day += "토요일";
                break;

        }
        return day;
    }

    private boolean isToday(String days){
        if(days.charAt(dayToday-1)=='1'){
            return true;
        }
        return false;
    }

    private void createTodayRecords(){
        //database 읽어서 체크 -> 해당하는 과목 create
        mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            SubjectInfo subjectInfo = snapshot.getValue(SubjectInfo.class);
                            if(isToday(subjectInfo.getDays())){
                                Toast.makeText(getContext(), subjectInfo.getSubject() + "는 오늘입니다", Toast.LENGTH_SHORT).show();
                                newRecord(subjectInfo.getSubject());
                            }
                            //getValue로 요일 정보 읽어와서
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//
//        linflater = getLayoutInflater();
//        //todo 메소드 만들기 (요일 해당 과목 찾아서 아래 뷰 만들고 추가하기 & 클릭리스너
//        //todo firebase에 초기화 (keyword: 키워드)
//        record1 = linflater.inflate(R.layout.frag_todayrecord, null);
//        FlowLayout flowLayout = (FlowLayout)record1.findViewById(R.id.flowLayout);
//        // flowLayout 전달
//        TextView textView = KeywordTextView();
//        textView.setText("sample keyword");
//        textView.setOnClickListener(onclick);
//        flowLayout.addView(textView);
//        linearScroll.addView(record1);
        /////
    }

    private void newRecord(final String subjectName){
        linflater = getLayoutInflater();
        //todo 메소드 만들기 (요일 해당 과목 찾아서 아래 뷰 만들고 추가하기 & 클릭리스너
        //todo firebase에 초기화 (keyword: 키워드)
        record1 = linflater.inflate(R.layout.frag_todayrecord, null);
        TextView titleView = (TextView)record1.findViewById(R.id.tv_subjectName);
        Button addButton = (Button)record1.findViewById(R.id.btn_addKeyWord);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), subjectName + " 추가 버튼입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        titleView.setText(subjectName);
        FlowLayout flowLayout = (FlowLayout)record1.findViewById(R.id.flowLayout);
        // flowLayout 전달
        TextView textView = KeywordTextView();
        textView.setText("sample keyword");
        textView.setOnClickListener(onclick);
        flowLayout.addView(textView);
        linearScroll.addView(record1);
    }

    private TextView KeywordTextView(){
        TextView textView = new TextView(getContext());
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        textView.setLayoutParams(params);
        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_border));
        return textView;
    }


    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView)v;
            String sName = tv.getText().toString();
            Toast.makeText(getContext(),sName + " clicked", Toast.LENGTH_SHORT).show();

        }
    };

}



