package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class
GetNewInterviewApplicationAdapter extends RecyclerView.Adapter<GetNewInterviewApplicationAdapter.MyViewHolder> {

    ArrayList<OnlineInterviewApplication> newApplicationList;
    private final Activity activity;
    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");

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
        Double overallMark = Double.valueOf(newApplicationList.get(position).getOverallMark().toString());
        holder.mark.setText(REAL_FORMATTER.format(overallMark));

        boolean completeSubmission = newApplicationList.get(position).getCompleteSubmission();
        boolean completeAssessment = newApplicationList.get(position).getCompleteAssessment();

        if (completeSubmission == true) {
            holder.submission.setImageResource(R.drawable.ic_accept_interview);
            holder.submission.setBackgroundResource(R.drawable.bg_accept_interview);
        }
        if (completeSubmission == false) {
            holder.submission.setImageResource(R.drawable.ic_reject_interview);
            holder.submission.setBackgroundResource(R.drawable.bg_reject_interview);
        }
        if (completeAssessment == true) {
            holder.assessment.setImageResource(R.drawable.ic_accept_interview);
            holder.assessment.setBackgroundResource(R.drawable.bg_accept_interview);
        }
        if (completeAssessment == false) {
            holder.assessment.setImageResource(R.drawable.ic_reject_interview);
            holder.assessment.setBackgroundResource(R.drawable.bg_reject_interview);
        }

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
        ImageView submission;
        ImageView assessment;
        ImageView reviewButton;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
            mark = itemView.findViewById(R.id.textView2);
            submission = itemView.findViewById(R.id.imageView1);
            assessment = itemView.findViewById(R.id.imageView2);
            reviewButton = itemView.findViewById(R.id.review_button);
        }
    }

}