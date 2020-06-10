package com.example.chuckchuck;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BoardActivity extends AppCompatActivity {
    String subjectKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subjectKey = getIntent().getStringExtra("subjectKey");

        //setContentView
    }
}
