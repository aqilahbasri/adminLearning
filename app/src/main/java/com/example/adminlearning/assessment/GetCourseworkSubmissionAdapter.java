package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class GetCourseworkSubmissionAdapter extends RecyclerView.Adapter<GetCourseworkSubmissionAdapter.MyViewHolder> {

    ArrayList<ManageCourseworkModalClass> newApplicationList;
    private final Activity activity;
    private String applicantName;
    private String userID;

    GetCourseworkSubmissionAdapter(Activity activity, ArrayList<ManageCourseworkModalClass> newApplicationList) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_coursework_submission, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.index.setText(String.valueOf(position + 1));

        userID = newApplicationList.get(position).getApplicantId();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicantName = snapshot.child("fullName").getValue().toString();
                holder.studentName.setText(applicantName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String date = sdf.format(newApplicationList.get(position).getSubmittedDate());
        holder.submittedDate.setText(date);

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newApplicationList.get(position).getCourseworkFile()));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                }
            }
        });

        if (newApplicationList.get(position).getCourseworkMark() == null) {
            holder.giveMarkBtn.setVisibility(View.VISIBLE);
        } else {
            holder.marks.setVisibility(View.VISIBLE);
            holder.marks.setText(newApplicationList.get(position).getCourseworkMark().toString());
        }

        holder.giveMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveMarkDialog(holder.giveMarkBtn, holder.marks);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newApplicationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView studentName;
        TextView submittedDate;
        TextView marks;
        ImageButton downloadBtn;
        ImageButton giveMarkBtn;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.textView1);
            studentName = itemView.findViewById(R.id.textView2);
            submittedDate = itemView.findViewById(R.id.textView3);
            marks = itemView.findViewById(R.id.textView4);
            downloadBtn = itemView.findViewById(R.id.downloadButton);
            giveMarkBtn = itemView.findViewById(R.id.review_button);
        }
    }

    private void giveMarkDialog(ImageButton giveMarkBtn, TextView marks) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity, R.style.CustomMaterialDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_coursework_mark, null);

        EditText courseworkMark = view.findViewById(R.id.courseworkMark);

        dialog.setTitle("Set coursework mark for "+applicantName);
        dialog.setView(view);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (courseworkMark.getText().length()!=0) {

                    Long mark = Long.valueOf(courseworkMark.getText().toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                            .child("ManageCoursework").child("CourseworkSubmissions");
                    reference.child(userID).child("courseworkMark").setValue(mark).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                giveMarkBtn.setVisibility(View.GONE);
                                marks.setVisibility(View.VISIBLE);
                                marks.setText(mark.toString());
                                Toast.makeText(activity, "Mark has been saved", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(activity, "Please enter marks", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}