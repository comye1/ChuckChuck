package com.example.chuckchuck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllrecordsAdapter extends RecyclerView.Adapter<AllrecordsAdapter.ViewHolder> {
    private ArrayList<ArrayList<Record>> mData = null;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_record_date ;

        ViewHolder(final View itemView) {
            super(itemView) ;

            tv_record_date = itemView.findViewById(R.id.tv_record_date) ;
        }
    }



    public AllrecordsAdapter(ArrayList<ArrayList<Record>> list, Context context) {
        mData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AllrecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.all_records_view, parent, false);
        AllrecordsAdapter.ViewHolder vh = new AllrecordsAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
//        String title = mData.get(position).getKeyword();
//        holder.tv_record_keyword.setText(title);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}


