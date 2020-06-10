package com.example.chuckchuck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllrecordsAdapter extends RecyclerView.Adapter<AllrecordsAdapter.ViewHolder> {
    private ArrayList<DateAndRecords> mData = null;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RecyclerView recyclerView;
        TextView tv_record_date ;

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
        }
    }



    public AllrecordsAdapter(ArrayList<DateAndRecords> list, Context context) {
        mData = list;
        this.context = context;
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


