package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ViewOnlineInterviewDialog extends AppCompatDialogFragment {

    FirebaseDatabase database;
    DatabaseReference detailsRef;
    EditText interviewerField;
    EditText interviewDate;
    EditText interviewTime;
    Calendar calendar;

    String applicantName, applicantId, interviewerId;

    private static final String TAG = "UpdateInterviewDialog";

    public ViewOnlineInterviewDialog(String applicantName, String applicantId) {
        this.applicantName = applicantName;
        this.applicantId = applicantId;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Interview Details");
        builder.setMessage("Update interview details for " + applicantName);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_view_online_interview, null);
        calendar = Calendar.getInstance();

        interviewerField = view.findViewById(R.id.interviewer_name);
        interviewDate = view.findViewById(R.id.interview_date);
        interviewTime = view.findViewById(R.id.interview_time);

        final Toast successMessage = Toast.makeText(builder.getContext(), "Interview details updated successfully", Toast.LENGTH_SHORT);
        final Toast failMessage = Toast.makeText(builder.getContext(), "Database update failed", Toast.LENGTH_SHORT);

        //TODO: Spinner for interviewer field, find out about Onfocuschangelistener

        //Dialog box for date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
        interviewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Dialog box for time
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        };

        interviewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), time,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(getActivity())).show();
            }
        });

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageOnlineInterview").child("ScheduledInterview");
        detailsRef.keepSynced(true);

        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                detailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(applicantId)) {

                            String interviewerName = interviewerField.getText().toString();
                            Long interviewTime = calendar.getTimeInMillis();

                            getInterviewerId(interviewerName);

                            HashMap<String, Object> values = new HashMap<>();
                            values.put("interviewerName", interviewerName);
                            values.put("interviewTime", interviewTime);
                            values.put("interviewerId", interviewerId);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    snapshot.child(applicantId).getRef().updateChildren(values)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isComplete()) {
                                                        successMessage.show();
                                                    } else {
                                                        failMessage.show();
                                                    }
                                                }
                                            });

                                }
                            }, 800);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("error", error.getMessage());
                    }
                });
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    private void getInterviewerId(final String interviewerName) {
        Log.e(TAG, interviewerName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    Log.e(TAG, snapshot.getValue().toString());
                    if (snapshot1.child("fullName").getValue().equals(interviewerName)) {
                        interviewerId = snapshot1.getKey();
                    } else {
                        Log.e(TAG, "Error getting interviewerID");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }
}