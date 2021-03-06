package com.example.adminlearning.assessment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class SetOnlineInterviewDialog extends AppCompatDialogFragment {

    private static final String TAG = "SetInterviewDialog";
    FirebaseDatabase database;
    DatabaseReference detailsRef;
    EditText interviewerField;
    EditText interviewDate;
    EditText interviewTime;
    EditText meetingLink;
    Calendar calendar;

    private Button positiveButton, negativeButton;

    String applicantName;
    String applicantId;
    private String interviewerId;

    public SetOnlineInterviewDialog(String applicantId, String applicantName) {
        this.applicantId = applicantId;
        this.applicantName = applicantName;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);
        builder.setTitle("Set Interview Details for "+applicantName);
//        builder.setMessage("Set interview details for " + applicantName);
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_set_online_interview, null);
        calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));

        interviewerField = view.findViewById(R.id.interviewer_name);
        meetingLink = view.findViewById(R.id.meting_link);
        interviewDate = view.findViewById(R.id.interview_date);
        interviewTime = view.findViewById(R.id.interview_time);
        positiveButton = view.findViewById(R.id.positiveButton);
        negativeButton = view.findViewById(R.id.negativeButton);

        final Toast successMessage = Toast.makeText(builder.getContext(), "Interview details saved successfully", Toast.LENGTH_SHORT);
        final Toast failMessage = Toast.makeText(builder.getContext(), "Database update failed", Toast.LENGTH_SHORT);
        final Toast incompleteMessage = Toast.makeText(getContext(), "Please complete all details", Toast.LENGTH_SHORT);

        //Dialog box for date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        interviewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerDialogTheme, date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        //Dialog box for time
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                updateLabel();
            }
        };

        interviewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), R.style.TimePickerDialogTheme, time,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(getActivity())).show();
            }
        });

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageOnlineInterview").child("NewApplication");

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String interviewerName = interviewerField.getText().toString();
                String meetingLinkStr = meetingLink.getText().toString();
                getInterviewerId(interviewerName);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!interviewerName.equals("") && !String.valueOf(calendar.getTimeInMillis()).equals(null)
                        && !meetingLinkStr.equals("") && !meetingLinkStr.equals("")) {
                            setInterviewDetails(interviewerName, meetingLinkStr, successMessage, failMessage);
                        }
                        else incompleteMessage.show();
                    }
                }, 1000);
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void setInterviewDetails(final String interviewerName, String meetingLinkStr, final Toast successMessage, final Toast failMessage) {
        detailsRef.child(applicantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Long interviewTime = calendar.getTimeInMillis();

                HashMap<String, Object> values = new HashMap<>();
                values.put("interviewerName", interviewerName);
                values.put("interviewerId", interviewerId);
                values.put("interviewTime", interviewTime);
                values.put("meetingLink", meetingLinkStr);

                snapshot.getRef().updateChildren(values);

                sendValuesAfterReview(snapshot.getRef(), successMessage, failMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: "+error.getMessage());
            }
        });
    }

    private void getInterviewerId(final String interviewerName) {
        Log.e(TAG, interviewerName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("fullName").getValue().equals(interviewerName)) {
                        interviewerId = snapshot1.getKey();
                    }
                    else {
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

    private void sendValuesAfterReview(final DatabaseReference ref, final Toast successMessage, final Toast failMessage) {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference toPath = database.getReference().child("ManageOnlineInterview").child("ScheduledInterview");

        //Ref yang dapat ialah ref with applicant id
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                toPath.child(applicantId).setValue(snapshot.getValue())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    snapshot.getRef().removeValue();
                                    successMessage.show();
                                } else {
                                    failMessage.show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Set the interview time and date string
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        interviewDate.setText(sdf.format(calendar.getTime()));

        String timeFormat = "hh:mm aa";
        SimpleDateFormat sdf2 = new SimpleDateFormat(timeFormat, Locale.getDefault());
        interviewTime.setText(sdf2.format(calendar.getTime()));
    }

}