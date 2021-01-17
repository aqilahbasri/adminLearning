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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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

public class ViewOnlineInterviewDialog extends AppCompatDialogFragment {

    FirebaseDatabase database;
    DatabaseReference detailsRef;
    TextView interviewerTxt, dateTxt, timeTxt;
    EditText markTxt;
    CheckBox isCompleteBox;

    String applicantName, applicantId, interviewerId;
    Toast successMessage, failMessage;

    private static final String TAG = "ViewInterviewDialog";

    public ViewOnlineInterviewDialog(String applicantName, String applicantId) {
        this.applicantName = applicantName;
        this.applicantId = applicantId;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Interview Details");
        builder.setMessage("View interview details for " + applicantName);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_view_online_interview, null);

        interviewerTxt = view.findViewById(R.id.interviewNameTxt);
        dateTxt = view.findViewById(R.id.interviewDateTxt);
        timeTxt = view.findViewById(R.id.interviewTimeTxt);
        markTxt = view.findViewById(R.id.interviewMarkTxt);
        isCompleteBox = view.findViewById(R.id.isCompleteBox);

        successMessage = Toast.makeText(builder.getContext(), "Interview has been completed", Toast.LENGTH_SHORT);
        failMessage = Toast.makeText(builder.getContext(), "Database update failed", Toast.LENGTH_SHORT);

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageOnlineInterview").child("ScheduledInterview");
        detailsRef.keepSynced(true);

        getInfoFromDatabase();

        builder.setView(view);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!(markTxt.getText().length() == 0) && isCompleteBox.isChecked()) {
                    sendValuesToDatabase();
                } else {
                    Toast.makeText(getContext(), "Please complete all details and check the box", Toast.LENGTH_SHORT).show();
                }
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

    private void sendValuesToDatabase() {
        Long mark = Long.parseLong(markTxt.getText().toString());
        Long completedTime = new Date().getTime();

        detailsRef.child(applicantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("interviewMark").getRef().setValue(mark);
                snapshot.child("isCompleteInterview").getRef().setValue(true);
                snapshot.child("completedTime").getRef().setValue(completedTime);

                sendValuesAfterReview();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    private void getInfoFromDatabase() {

        detailsRef.child(applicantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String interviewerName = snapshot.child("interviewerName").getValue().toString();
                Long interviewDate = Long.valueOf(snapshot.child("interviewTime").getValue().toString());
                updateDateTimeLabel(interviewDate);

                interviewerTxt.setText(interviewerName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error", error.getMessage());
            }
        });

    }

    private void updateDateTimeLabel(Long interviewTime) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String timeFormat = "hh.mm aa"; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(timeFormat, Locale.getDefault());
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        String dateStr = sdf.format(interviewTime);
        String timeStr = sdf2.format(interviewTime);

        dateTxt.setText(dateStr);
        timeTxt.setText(timeStr);
    }

    private void sendValuesAfterReview() {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference toPath = database.getReference().child("ManageOnlineInterview").child("CompletedInterview");

        //Ref yang dapat ialah ref with applicant id
        detailsRef.child(applicantId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                Log.e(TAG, error.getMessage());
            }
        });
    }
}