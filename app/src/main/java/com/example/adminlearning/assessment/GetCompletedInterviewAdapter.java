package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class GetCompletedInterviewAdapter extends RecyclerView.Adapter<GetCompletedInterviewAdapter.MyViewHolder> {

    ArrayList<OnlineInterviewApplication> completedInterviewList;
    private final Activity activity;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private static final String TAG = "GetCompletedInterview";

    GetCompletedInterviewAdapter(Activity activity, ArrayList<OnlineInterviewApplication> completedInterviewList) {
        this.activity = activity;
        this.completedInterviewList = completedInterviewList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_completed_interview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Long completedTime = completedInterviewList.get(position).getCompletedTime();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String timeFormat = "hh.mm aa"; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(timeFormat, Locale.getDefault());
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String dateStr = sdf.format(completedTime);
        String timeStr = sdf2.format(completedTime);

        holder.name.setText(completedInterviewList.get(position).getName());
        holder.interviewerName.setText(completedInterviewList.get(position).getInterviewerName());
        holder.interviewDate.setText(dateStr);
        holder.interviewTime.setText(timeStr);
        holder.mark.setText(completedInterviewList.get(position).getInterviewMark().toString());

    }

    @Override
    public int getItemCount() {
        return completedInterviewList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView interviewerName;
        TextView interviewDate;
        TextView interviewTime;
        TextView mark;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
            interviewerName = itemView.findViewById(R.id.textView2);
            interviewDate = itemView.findViewById(R.id.textView3);
            interviewTime = itemView.findViewById(R.id.textView4);
            mark = itemView.findViewById(R.id.textView5);
        }
    }

}