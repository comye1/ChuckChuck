package com.example.chuckchuck;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class Frag1 extends Fragment {
    private View view;
    private TextView tv_date;
    private LinearLayout linearScroll;
    private LayoutInflater linflater;
    private Context mContext;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static int dayToday;
    private static String today;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);
        tv_date = view.findViewById(R.id.tv_date);
        tv_date.setText(getDate());
        linearScroll = view.findViewById(R.id.linear_scroll);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        builder =  new AlertDialog.Builder(mContext);
        alertDialog = builder.setTitle("로딩중").setMessage("잠시만 기다려 주세요.").setCancelable(false).create();
        alertDialog.show();

        createTodayRecords();
        return view;
    }

    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M월 d일 ");
        String day = simpleDateFormat.format(new Date());

        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        today = simpleDateFormat.format(new Date()) + "000000";
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
        mDatabase.child("Users/"+mAuth.getUid()+"/TimeTable")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            SubjectInfo subjectInfo = snapshot.getValue(SubjectInfo.class);
                            if(isToday(subjectInfo.getDays())){
                                newRecord(subjectInfo.getSubject(), snapshot.getKey());
//                                updateListener(snapshot.getKey());
                            }
                            //getValue로 요일 정보 읽어와서
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void newRecord(final String subjectName, final String key){
        linflater = getLayoutInflater();
        final View record = linflater.inflate(R.layout.frag_todayrecord, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        record.setLayoutParams(layoutParams);
        TextView titleView = (TextView)record.findViewById(R.id.tv_subjectName);
        final EditText addEt = (EditText)record.findViewById(R.id.edit_addKeyWord);
        Button addButton = (Button)record.findViewById(R.id.btn_addKeyWord);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = addEt.getText().toString();
                if(keyword.replace(" ", "").equals("")){ //공백 여부 검사
                    return ;
                }
                addKeyWordTextView(record, keyword, key);
                addEt.setText(null);

            }
        });

        titleView.setText(subjectName);
        linearScroll.addView(record);

        mDatabase.child("Users/"+mAuth.getUid()+"/Records/"+key+"/"+today)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FlowLayout flowLayout = (FlowLayout)record.findViewById(R.id.flowLayout);
                        flowLayout.removeAllViews();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Content content = snapshot.getValue(Content.class);

                            // flowLayout 전달
                            final TextView textView = KeywordTextView(content.getKeyword());
                            flowLayout.addView(textView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void addKeyWordTextView(final View record, String keyword, String key){
        Content content = new Content(keyword, null);
        mDatabase.child("Users/"+mAuth.getUid()+"/Records/"+key+"/"+today).push().setValue(content);
        FlowLayout flowLayout = (FlowLayout)record.findViewById(R.id.flowLayout);

        // flowLayout 전달
        TextView textView = KeywordTextView(content.getKeyword());
        textView.setOnClickListener(onclick);
        flowLayout.addView(textView);
    }


    private void pushKeyword(String keyword, String content, String key){
        mDatabase.child("Users/"+mAuth.getUid()+"/Records/"+key+"/"+today);

        Toast.makeText(mContext, "저장되었습니다." , Toast.LENGTH_SHORT).show();
    }

    private TextView KeywordTextView(String text){
        TextView textView = new TextView(mContext);//////todo context
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        textView.setLayoutParams(params);
        textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_border));
        textView.setText(text);
        return textView;
    }


    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView)v;
            String sName = tv.getText().toString();
            Toast.makeText(mContext,sName + " clicked", Toast.LENGTH_SHORT).show();

        }
    };


}

class Content {
    String keyword;
    String content;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getContent() {
        return content;
    }

    public Content(){

    }

    public Content(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }
}

