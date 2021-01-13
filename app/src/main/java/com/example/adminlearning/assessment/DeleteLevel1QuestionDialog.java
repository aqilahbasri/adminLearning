package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class DeleteLevel1QuestionDialog extends AppCompatDialogFragment {

    private final String TAG = "DeleteQuestionDialog";
    Calendar calendar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reference;

    int position;

    public DeleteLevel1QuestionDialog(DocumentReference reference, int position) {
        this.reference = reference;
        this.position = position;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Question");
        builder.setMessage("Are you sure you want to delete question " + (position+1) +"?");

        calendar = Calendar.getInstance();

        final Toast successMessage = Toast.makeText(builder.getContext(), "Question deleted successfully", Toast.LENGTH_SHORT);
        final Toast failMessage = Toast.makeText(builder.getContext(), "Error deleting question", Toast.LENGTH_SHORT);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Question deleted successfully");
                                successMessage.show();
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting question", e);
                                failMessage.show();
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
}