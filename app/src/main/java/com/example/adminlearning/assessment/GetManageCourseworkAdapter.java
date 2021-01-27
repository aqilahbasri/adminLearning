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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class GetManageCourseworkAdapter extends FirebaseRecyclerAdapter< ManageCourseworkModalClass, GetManageCourseworkAdapter.MyViewHolder> {

//    ArrayList<ManageCourseworkModalClass> newApplicationList;
    private Activity activity;

    GetManageCourseworkAdapter(FirebaseRecyclerOptions<ManageCourseworkModalClass> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_manage_coursework, parent, false));
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull ManageCourseworkModalClass model) {
        holder.index.setText(String.valueOf(i + 1));
        holder.courseworkName.setText(model.getCourseworkName());
        holder.dateCreated.setText(model.getDateCreated());

        //view coursework details
        holder.courseworkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCourseworkDialog viewCourseworkDialog = new ViewCourseworkDialog(activity, model.getCourseworkName());
                viewCourseworkDialog.show(((ManageCourseworkActivity) activity)
                        .getSupportFragmentManager(), "ViewCourseworkDialog");
            }
        });

        //TODO: Review coursework
        holder.viewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: try to get key? settle later. now get name first
                //TODO: change with submission dialog
                ((ManageCourseworkActivity) activity).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new ManageCourseworkSubmissionFragment(GetManageCourseworkAdapter.this.getRef(i))).commit();
            }
        });
    }

//    @Override
//    public int getItemCount() {
//        return newApplicationList.size();
//    }

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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}