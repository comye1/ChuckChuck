package com.example.chuckchuck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Record> records;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;;

    private String subjectKey;
    private String subjectName;
    private TextView tv_subjectName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        subjectKey = getIntent().getStringExtra("subjectKey");
        subjectName = getIntent().getStringExtra("subjectName");

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tv_subjectName = findViewById(R.id.subjectName);
        tv_subjectName.setText(subjectName);

        loadRecords();
        //setContentView
    }

    public void loadRecords(){
        records = new ArrayList<>();
    }
}
