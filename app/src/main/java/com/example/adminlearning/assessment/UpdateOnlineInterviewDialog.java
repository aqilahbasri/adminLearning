package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class UpdateOnlineInterviewDialog extends AppCompatDialogFragment {

    FirebaseDatabase database;
    DatabaseReference detailsRef;
    EditText interviewerField;
    EditText interviewDate;
    EditText interviewTime;
    Calendar calendar;

    String operation;

    public UpdateOnlineInterviewDialog(String operation) {
        this.operation = operation;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Interview Details");
        builder.setMessage("Update interview details for " + operation);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_set_online_interview, null);
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
                updateLabel();
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
                updateLabel();
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
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            //to update value with interview details
                            //if name is same w the one received
                            if (ds.child("name").getValue().equals(operation)) {

                                String interviewerName = interviewerField.getText().toString();
                                String interviewDateString = interviewDate.getText().toString();
                                String interviewTimeString = interviewTime.getText().toString();

                                HashMap<String, Object> values = new HashMap<>();
                                values.put("interviewerName", interviewerName);
                                values.put("interviewDate", interviewDateString);
                                values.put("interviewTime", interviewTimeString);

                                ds.getRef().updateChildren(values)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()) {
                                            //delete here?
//                                            snapshot.getRef().removeValue();
                                            successMessage.show();
                                        } else {
                                            failMessage.show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("error", "haha tak jadi!!!");
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

    //Set the interview time and date string
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        interviewDate.setText(sdf.format(calendar.getTime()));

        String timeFormat = "hh:mm aa";
        SimpleDateFormat sdf2 = new SimpleDateFormat(timeFormat, Locale.UK);
        interviewTime.setText(sdf2.format(calendar.getTime()));
    }

}