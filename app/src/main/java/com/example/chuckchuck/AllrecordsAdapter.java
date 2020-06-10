package com.example.chuckchuck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AllrecordsAdapter extends RecyclerView.Adapter<AllrecordsAdapter.ViewHolder> {
    private ArrayList<DateAndRecords> mData = null;
    private Context context;
    private String path;

    private AlertDialog.Builder builder;
    private View dialogView;
    private EditText et_keyWord, et_content;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RecyclerView recyclerView;
        TextView tv_record_date ;
        ImageButton btn_addKeyword;

        ViewHolder(final View itemView) {
            super(itemView) ;

            recyclerView = itemView.findViewById(R.id.recycler_all);
            tv_record_date = itemView.findViewById(R.id.tv_record_date) ;
            tv_record_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerView.getAdapter().getItemCount()==0){

                    }
                    else if(recyclerView.getVisibility() == View.VISIBLE){
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });

            btn_addKeyword = itemView.findViewById(R.id.btn_addKeyWord);
            btn_addKeyword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    dialogView = inflater.inflate(R.layout.dialog_keyword, null);
                    et_keyWord = dialogView.findViewById(R.id.et_keyWord);
                    et_content = dialogView.findViewById(R.id.et_content);
                    builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView)
                            .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String keyword = et_keyWord.getText().toString();
                                    String content = et_content.getText().toString();
                                    if(keyword.replace(" ", "").equals("")){
                                        Toast.makeText(context, "취소되었습니다." , Toast.LENGTH_SHORT).show();
                                        return ;
                                    }else{
                                        String subPath = tv_record_date.getText().toString();
                                        String pushKey = mDatabase.child(subPath).push().getKey();
                                        Record record = new Record(keyword, content, path + "/"+subPath);
                                        Toast.makeText(context, "New Keyword", Toast.LENGTH_SHORT).show();
                                        mDatabase.child(subPath).push().setValue(record);
                                        int pos = getAdapterPosition();
                                        mData.get(pos).putRecord(record);
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }
            });

        }
    }



    public AllrecordsAdapter(ArrayList<DateAndRecords> list, Context context, String path) {
        mData = list;
        this.context = context;
        this.path = path;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).child("Records").child(path);

    }

    @NonNull
    @Override
    public AllrecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.all_records_view, parent, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        view.setLayoutParams(layoutParams);

        AllrecordsAdapter.ViewHolder vh = new AllrecordsAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AllrecordsAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).getDate();
        holder.tv_record_date.setText(title);

        RecordAdapter adapter = new RecordAdapter(mData.get(position).getRecordArrayList(), context);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        holder.recyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}


