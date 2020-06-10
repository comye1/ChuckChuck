package com.example.chuckchuck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<DateAndRecords> darArray;
    private ArrayList<Record> records;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;;

    private String subjectKey;
    private String subjectName;
    private TextView tv_subjectName;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        subjectKey = getIntent().getStringExtra("subjectKey");
        subjectName = getIntent().getStringExtra("subjectName");

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tv_subjectName = findViewById(R.id.subjectName);
        tv_subjectName.setText(subjectName);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Users").child(mAuth.getUid()).child("Records").child(subjectKey)
                        .child(makeKey()).push().child("keyword").setValue("sample keyword");
                finish();
                startActivity(getIntent());
                //액티비티 종료 후 다시 실행  ---> 함수를 만들지 않아도 리사이클러뷰 자동으로 업데이트 됨...ㅎㅎ
            }
        });

        loadRecords();
    }

    public void loadRecords(){
        records = new ArrayList<>();

        //firebase에서 데이터 로드, 어댑터 연결

        darArray = new ArrayList<>();
        final AllrecordsAdapter adapter = new AllrecordsAdapter(darArray, BoardActivity.this, subjectKey);
        recyclerView.setAdapter(adapter);



//        final DateAndRecords dateAndRecords = new DateAndRecords();
//        dateAndRecords.setDate("20200610");
//        dateAndRecords.setRecordArrayList(records);
//        darArray.add(dateAndRecords);
//        adapter.notifyDataSetChanged();


        mDatabase.child("Users").child(mAuth.getUid()).child("Records").child(subjectKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        darArray.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            DateAndRecords dar = new DateAndRecords();
                            String key = snapshot.getKey();
//                            Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
                            dar.setDate(key);
                            for(DataSnapshot innersnapshot : snapshot.getChildren()){
                                Content content = innersnapshot.getValue(Content.class);
                                String path = subjectKey + "/" + key + "/"+ innersnapshot.getKey();//subPath
                                Record record = new Record(content.getKeyword(), content.getContent(), path);

                                dar.putRecord(record);
                            }

                            darArray.add(dar);

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private String makeKey(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSS");
        return simpleDateFormat.format(new Date());
    }
}
