package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class GetScheduledInterviewAdapter extends RecyclerView.Adapter<GetScheduledInterviewAdapter.MyViewHolder> {

    ArrayList<OnlineInterviewApplication> newApplicationList;
    private final Activity activity;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private static final String TAG = "ScheduledInterviewAptr";

    GetScheduledInterviewAdapter(Activity activity, ArrayList<OnlineInterviewApplication> newApplicationList) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_scheduled_interview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Long interviewTime = newApplicationList.get(position).getInterviewTime();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String timeFormat = "hh.mm aa"; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(timeFormat, Locale.getDefault());
//        sdf2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String dateStr = sdf.format(interviewTime);
        String timeStr = sdf2.format(interviewTime);

        holder.name.setText(newApplicationList.get(position).getName());
        holder.interviewerName.setText(newApplicationList.get(position).getInterviewerName());
        holder.interviewDate.setText(dateStr);
        holder.interviewTime.setText(timeStr);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewOnlineInterviewDialog viewOnlineInterviewDialog = new ViewOnlineInterviewDialog(newApplicationList.get(position).getName(),
                        newApplicationList.get(position).getUserId());
                viewOnlineInterviewDialog.show(((ManageOnlineInterviewActivity) activity)
                        .getSupportFragmentManager(), "set interview dialog");
            }
        });

        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateOnlineInterviewDialog updateOnlineInterviewDialog = new UpdateOnlineInterviewDialog(newApplicationList.get(position).getName(),
                        newApplicationList.get(position).getUserId());
                updateOnlineInterviewDialog.show(((ManageOnlineInterviewActivity) activity)
                        .getSupportFragmentManager(), "set interview dialog");
            }
        });

//        holder.callButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                initiateVideoMeeting(newApplicationList.get(position).getUserId());
//            }
//        });
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
        ImageButton reviewButton;
//        ImageButton callButton;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
            interviewerName = itemView.findViewById(R.id.textView2);
            interviewDate = itemView.findViewById(R.id.textView3);
            interviewTime = itemView.findViewById(R.id.textView4);
            reviewButton = itemView.findViewById(R.id.review_button);
//            callButton = itemView.findViewById(R.id.callBtn);
        }
    }

    public void initiateVideoMeeting(String userId) {
        Intent intent = new Intent(activity, CallingActivity.class);
        intent.putExtra("visit_user_id", userId);
        activity.startActivity(intent);
    }

}