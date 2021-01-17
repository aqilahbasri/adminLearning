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

public class GetNewInterviewApplicationAdapter extends RecyclerView.Adapter<GetNewInterviewApplicationAdapter.MyViewHolder> {

    ArrayList<OnlineInterviewApplication> newApplicationList;
    private final Activity activity;

    GetNewInterviewApplicationAdapter(Activity activity, ArrayList<OnlineInterviewApplication> newApplicationList) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_interview_application, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(newApplicationList.get(position).getName());
        holder.mark.setText(newApplicationList.get(position).getOverallMark().toString());
        holder.submission.setText(newApplicationList.get(position).getCompleteSubmission().toString());
        holder.assessment.setText(newApplicationList.get(position).getCompleteAssessment().toString());
        newApplicationList.get(position);
        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetOnlineInterviewDialog setOnlineInterviewDialog = new SetOnlineInterviewDialog(
                        newApplicationList.get(position).getUserId(),
                        newApplicationList.get(position).getName());
                setOnlineInterviewDialog.show(((ManageOnlineInterviewActivity) activity)
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
        TextView mark;
        TextView submission;
        TextView assessment;
        Button reviewButton;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
            mark = itemView.findViewById(R.id.textView2);
            submission = itemView.findViewById(R.id.textView3);
            assessment = itemView.findViewById(R.id.textView4);
            reviewButton = itemView.findViewById(R.id.review_button);
        }
    }

}