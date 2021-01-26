package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;

import java.util.ArrayList;

public class GetApprovedCertificationAdapter extends RecyclerView.Adapter<GetApprovedCertificationAdapter.MyViewHolder> {

    private static final String TAG = "ApprovedCertAdapter";
    ArrayList<ApplyCertContactInfo> newApplicationList;
    private final Activity activity;
    private String key;

    GetApprovedCertificationAdapter(Activity activity, ArrayList<ApplyCertContactInfo> newApplicationList, String key) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
        this.key = key;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_approved_certification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(newApplicationList.get(position).getName());
//        holder.assessmentLevel.setText("1");
        holder.appliedDate.setText(newApplicationList.get(position).getApprovedDate());
        holder.phoneNumber.setText(newApplicationList.get(position).getPhoneNumber());
        holder.viewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: try to get key? settle later. now get name first
                //TODO: update dialog instead of erasing
                ApprovedCertAddressDialog approvedCertAddressDialog = new ApprovedCertAddressDialog(key, newApplicationList.get(position).getName());
                approvedCertAddressDialog.show(((ManageCertificationActivity) activity)
                        .getSupportFragmentManager(), TAG);
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
        TextView appliedDate;
        TextView phoneNumber;
        Button viewAddress;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
//            assessmentLevel = itemView.findViewById(R.id.textView2);
            appliedDate = itemView.findViewById(R.id.textView2);
            phoneNumber = itemView.findViewById(R.id.textView3);
            viewAddress = itemView.findViewById(R.id.review_button);
        }
    }

}