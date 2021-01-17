package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ApplyCertificateReviewDialog extends AppCompatDialogFragment {

    private final String TAG = "ApplyCertificateDialog";
    FirebaseDatabase database;
    DatabaseReference detailsRef;
    Calendar calendar;

    String key;
    String name;

    public ApplyCertificateReviewDialog(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);
        builder.setTitle("Review Application");
        builder.setMessage("Approve certification application by " + name +"?");

//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.dialog_set_online_interview, null);
        calendar = Calendar.getInstance();

        final Toast successMessage = Toast.makeText(builder.getContext(), "Application approved successfully", Toast.LENGTH_SHORT);
        final Toast failMessage = Toast.makeText(builder.getContext(), "Database update failed", Toast.LENGTH_SHORT);

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("CertApplication_StudentInfo").child("NewApplication");
        detailsRef.keepSynced(true);

//        builder.setView(view);

        builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                detailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            if (ds.getKey().equals(key)) {
                                HashMap<String, Object> values = new HashMap<>();
                                values.put("approvedTimestamp", ServerValue.TIMESTAMP);
                                ds.getRef().updateChildren(values);
                                sendValuesAfterReview(ds.getRef(), successMessage, failMessage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.toString());
                    }
                });
            }

        });

        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
                detailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.getKey().equals(key)) {
                                ds.getRef().removeValue();
                                Toast.makeText(builder.getContext(), "Application rejected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.toString());
                    }
                });
            }
        });

        return builder.create();
    }

    private void sendValuesAfterReview(final DatabaseReference ref, final Toast successMessage, final Toast failMessage) {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference toPath = database.getReference().child("CertApplication_StudentInfo").child("ApprovedCertification");
        //TODO: remove from storageReference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                toPath.child(ref.getKey()).setValue(snapshot.getValue())
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
                Log.e(TAG, error.toString());
            }
        });
    }

}