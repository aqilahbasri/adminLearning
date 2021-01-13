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

public class GetScheduledInterviewAdapter extends RecyclerView.Adapter<GetScheduledInterviewAdapter.MyViewHolder> {

    ArrayList<OnlineInterviewApplication> newApplicationList;
    private final Activity activity;

    GetScheduledInterviewAdapter(Activity activity, ArrayList<OnlineInterviewApplication> newApplicationList) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_scheduled_interview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(newApplicationList.get(position).getName());
        holder.interviewerName.setText(newApplicationList.get(position).getInterviewerName());
        holder.interviewDate.setText(newApplicationList.get(position).getInterviewDate());
        holder.interviewTime.setText(newApplicationList.get(position).getInterviewTime());
        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: try to get key? settle later. now get name first
                //TODO: update dialog instead of erasing
                UpdateOnlineInterviewDialog updateOnlineInterviewDialog = new UpdateOnlineInterviewDialog(newApplicationList.get(position).getName());
                updateOnlineInterviewDialog.show(((ManageOnlineInterviewActivity) activity)
                        .getSupportFragmentManager(), "set interview dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return newApplicationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView assessmentLevel;   //TODO: Set assessment level for interviewee
        TextView interviewerName;
        TextView interviewDate;
        TextView interviewTime;
        Button reviewButton;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
            interviewerName = itemView.findViewById(R.id.textView2);
            interviewDate = itemView.findViewById(R.id.textView3);
            interviewTime = itemView.findViewById(R.id.textView4);
            reviewButton = itemView.findViewById(R.id.review_button);
        }
    }

}