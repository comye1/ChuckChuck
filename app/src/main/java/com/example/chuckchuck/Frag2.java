package com.example.chuckchuck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Frag2 extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Category> categories;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recylerView);
        layoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(layoutManager);

        loadData();

        return view;
    }

    public void loadData(){
        categories = new ArrayList<>();

        final CategoryAdapter adapter = new CategoryAdapter(categories);
        /*
        itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Toast.makeText(itemView.getContext(), mData.get(pos).getSubjectKey(), Toast.LENGTH_SHORT).show();
                    postActivity(mData.get(pos).getSubjectKey());
                }
            });
         */
        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra("subjectKey", categories.get(pos).getSubjectKey());//과목 키 전달
                intent.putExtra("subjectName", categories.get(pos).getSubjectName());//과목 이름 전달
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        mDatabase.child("Users").child(mAuth.getUid()).child("TimeTable")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categories.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            SubjectInfo subjectInfo = snapshot.getValue(SubjectInfo.class);
                            Category category = new Category(subjectInfo.getSubject(),snapshot.getKey());
                            categories.add(category);
                            //getValue로 요일 정보 읽어와서
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
