package com.example.chuckchuck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private ArrayList<Record> mData = null;
    private Context context;
    private AlertDialog.Builder builder;
    private View dialogView;
    private EditText et_keyWord, et_content;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_record_keyword ;

        ViewHolder(final View itemView) {
            super(itemView) ;

            tv_record_keyword = itemView.findViewById(R.id.tv_record_keyword) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION){
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        dialogView = inflater.inflate(R.layout.dialog_keyword, null);
                        et_keyWord = dialogView.findViewById(R.id.et_keyWord);
                        et_content = dialogView.findViewById(R.id.et_content);
                        et_keyWord.setText(mData.get(pos).getKeyword());
                        et_content.setText(mData.get(pos).getContent());
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
                                            updateRecord(keyword, content, pos);
                                        }
                                    }
                                })
                                .setNegativeButton("취소", null)
                                .setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeRecord(mData.get(pos).getSubPath());
                                    }
                                })
                                .show();

                    }
                }
            });
        }
    }

    public void updateRecord(String keyword, String content, int position){
        String path = mData.get(position).getSubPath();
        mDatabase = mDatabase.child(path);
        Record record = new Record(keyword, content, path);
        Content newContent = record;
        mData.set(position, record);
        mDatabase.setValue(newContent);

        notifyDataSetChanged();
    }

    public void removeRecord(String path){
        mDatabase = mDatabase.child(path);

        mDatabase.removeValue();
    }

    public RecordAdapter(ArrayList<Record> list, Context context) {
        mData = list;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).child("Records");
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.record_item_view, parent, false);
        RecordAdapter.ViewHolder vh = new RecordAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).getKeyword();
        holder.tv_record_keyword.setText(title);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}
