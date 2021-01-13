package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminlearning.R;

import java.util.ArrayList;

public class GetManageCourseworkAdapter extends RecyclerView.Adapter<GetManageCourseworkAdapter.MyViewHolder> {

    ArrayList<ManageCourseworkModalClass> newApplicationList;
    private final Activity activity;

    GetManageCourseworkAdapter(Activity activity, ArrayList<ManageCourseworkModalClass> newApplicationList) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_manage_certification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.index.setText(String.valueOf(position + 1));
        holder.courseworkName.setText(newApplicationList.get(position).getCourseworkName());
        holder.dateCreated.setText(newApplicationList.get(position).getDateCreated());

        //view coursework details
        holder.courseworkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //TODO: Review coursework
        holder.viewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: try to get key? settle later. now get name first
                //TODO: change with submission dialog
                AddNewCourseworkDialog addNewCourseworkDialog = new AddNewCourseworkDialog(activity);
                addNewCourseworkDialog.show(((ManageCourseworkActivity) activity)
                        .getSupportFragmentManager(), "ApplyCertDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return newApplicationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView courseworkName;
        TextView dateCreated;
        Button viewSubmission;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.textView1);
            courseworkName = itemView.findViewById(R.id.textView2);
            dateCreated = itemView.findViewById(R.id.textView3);
            viewSubmission = itemView.findViewById(R.id.review_button);
        }
    }
}