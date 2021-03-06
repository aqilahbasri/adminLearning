package com.example.adminlearning.assessment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class AddNewCourseworkDialog extends AppCompatDialogFragment {

    private static final String TAG = "NewCourseworkDialog";
    String key;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText courseworkNameText;
    EditText courseworkQuestionText;
    TextView notification;
    Button uploadButton;
    private Button negativeButton, positiveButton;

    private Uri filepath = null; //Uri = URL for local storage
    ProgressDialog progressDialog;
    private final Activity activity;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public AddNewCourseworkDialog(Activity activity) {
        this.activity = activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);
        builder.setTitle("Add new coursework");

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog_add_new_coursework, null);
//        calendar = Calendar.getInstance();

        courseworkNameText = view.findViewById(R.id.coursework_name);
        courseworkQuestionText = view.findViewById(R.id.coursework_question);
        notification = view.findViewById(R.id.notification);
        uploadButton = view.findViewById(R.id.upload_btn);
        positiveButton = view.findViewById(R.id.positiveButton);
        negativeButton = view.findViewById(R.id.negativeButton);

        final Toast successMessage = Toast.makeText(builder.getContext(), "Details saved successfully", Toast.LENGTH_SHORT);
        final Toast failMessage = Toast.makeText(builder.getContext(), "Details update failed", Toast.LENGTH_SHORT);

        initButton(notification, uploadButton);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseworkNameText.getText().length() == 0 || courseworkQuestionText.getText().length() == 0
                        && filepath != null) {
                    Toast.makeText(getContext(), "Please complete all details", Toast.LENGTH_SHORT).show();
                } else {
                    toDatabase(courseworkNameText, courseworkQuestionText);
                }
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

    private void toDatabase(final EditText courseworkNameText, final EditText courseworkQuestionText) {

        myRef = database.getReference().child("ManageCoursework").child("CourseworkQuestions");

        final String courseworkName = courseworkNameText.getText().toString();
        final String courseworkQuestion = courseworkQuestionText.getText().toString();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, Object> values = new HashMap<>();
                values.put("courseworkName", courseworkName);
                values.put("courseworkQuestion", courseworkQuestion);

                myRef.child(getKey()).updateChildren(values);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

        myRef.child(getKey()).child("createdTimestamp").setValue(System.currentTimeMillis());

    }

    void initButton(TextView notification, Button uploadButton) {
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        notification.setText("Please select a file...");

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) //User has selected a file
                    uploadFile(filepath);
                else
                    Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(intent, 20);
    }

    //To check if user successfully select file
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            filepath = data.getData(); //return uri of selected file
            notification.setText("File selected: " + data.getData().getLastPathSegment());
        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    //Method to acknowledge permission, Override means automatically called  by Android
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 15 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else
            Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    //Upload pdf file
    private void uploadFile(Uri filepath) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        //TODO: settle uid
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String userId = user.getUid();

        //Store file in storage
        final String fileName = System.currentTimeMillis() + "_CourseworkQuestion";
        final StorageReference mStorageRef = storage.getReference().child("ManageCoursework").child("CourseworkQuestions");
        mStorageRef.child(fileName).putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.i(TAG, "File successfully uploaded");

                        mStorageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference databaseReference = database.getReference().child("ManageCoursework").child("CourseworkQuestions");
                                databaseReference.push().child("courseworkFile").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
//                                        Log.e(TAG, "get from firebase: "+ds.getKey());
//                                        setKey(ds.getKey());
                                            Toast.makeText(activity, "File submission successful", Toast.LENGTH_SHORT).show();
                                            notification.setText("File uploaded: " + fileName);
                                            progressDialog.dismiss();
                                        }
                                    }
                                });

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            if (ds.child("courseworkFile").getValue().equals(uri.toString())) {
                                                setKey(ds.getKey());
                                                Log.e(TAG, ds.getKey());
                                            } else Log.e(TAG, "sum ting wong");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "File submission failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                //track progress of upload
                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    protected String getKey() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }

}