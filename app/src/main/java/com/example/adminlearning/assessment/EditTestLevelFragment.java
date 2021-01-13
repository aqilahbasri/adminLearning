package com.example.adminlearning.assessment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class EditTestLevelFragment extends Fragment {

    private static final String TAG = "EditTestLevelFragment";
    private EditText levelNameTxt, hourTxt, minuteTxt, overallMarkTxt;
    private TextView selectedPicTxt;
    private Button cancelBtn, confirmBtn;
    private Button selectPicBtn;
    private ImageView levelIcon;
    private CardView iconCardView;

    private RecyclerView recyclerView;
    private String key;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri filepath; //Uri = URL for local storage
    ProgressDialog progressDialog;

    public EditTestLevelFragment(String key) {
        this.key = key;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_test_level, container, false);

        levelNameTxt = view.findViewById(R.id.editText_1);
        hourTxt = view.findViewById(R.id.editText_2);
        minuteTxt = view.findViewById(R.id.editText_3);
        overallMarkTxt = view.findViewById(R.id.editText_4);
        recyclerView = view.findViewById(R.id.list_item);

        cancelBtn = view.findViewById(R.id.cancel_button);
        confirmBtn = view.findViewById(R.id.confirm_button);
        selectPicBtn = view.findViewById(R.id.select_btn);

        selectedPicTxt = view.findViewById(R.id.selectedPicName);
        levelIcon = view.findViewById(R.id.level_icon); //imageview, guna picasso
        iconCardView = view.findViewById(R.id.cardview_id);

        selectedPicTxt.setText("No picture selected");
        selectedPicTxt.setTextColor(Color.parseColor("#b20000"));

        selectPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        getInfoFromDatabase();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelNameTxt.getText().length() != 0 && hourTxt.getText().length() != 0 &&
                        minuteTxt.getText().length() != 0 && overallMarkTxt.getText().length() != 0 &&
                        !filepath.equals(null)) {
                    toDatabase();
                } else if (filepath == null)
                    Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getContext(), "Please complete all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    //Get initial info from db
    private void getInfoFromDatabase() {

        DocumentReference reference = db.collection("AssessmentLevel").document(key);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String levelName = value.get("levelName").toString();
                long duration = Long.parseLong(value.get("duration").toString());
                String overallPassingMark = value.get("overallPassingMark").toString();
                String iconUrl = value.get("levelIconUrl").toString();

                String hourStr = String.format(
                        Locale.getDefault(), "%02d",
                        TimeUnit.MILLISECONDS.toHours(duration));

                String minuteStr = String.format(
                        Locale.getDefault(), "%02d",
                        TimeUnit.MILLISECONDS.toMinutes(duration)-
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)
                                ));

                levelNameTxt.setText(levelName);
                overallMarkTxt.setText(overallPassingMark);
                hourTxt.setText(hourStr);
                minuteTxt.setText(minuteStr);
                Glide.with(getContext()).load(iconUrl).into(levelIcon);

            }
        });
    }

    //Send initial info to db
    private void toDatabase() {

        DocumentReference reference = db.collection("AssessmentLevel").document(key);

        Date date = new Date();
        Long time = date.getTime();

        final String levelName = levelNameTxt.getText().toString();
        final long overallPassingMark = Long.parseLong(overallMarkTxt.getText().toString());

        //convert time to milliseconds
        int hourToMs = Integer.parseInt(hourTxt.getText().toString())*60*60*1000;
        int minToMs = Integer.parseInt(minuteTxt.getText().toString())*60*1000;
        long duration = hourToMs+minToMs;

        Map question = new HashMap<>();
        question.put("dateModified", time);
        question.put("duration", duration);
        question.put("levelName", levelName);
        question.put("overallPassingMark", overallPassingMark);

        reference.update(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                uploadFile(filepath, id);
                Log.d(TAG, "Level details saved successfully");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getContext(), "Details update failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectIcon();
        } else
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 15);
    }

    private void selectIcon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(intent, 20);
    }

    //To check if user successfully select file
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            filepath = data.getData(); //return uri of selected file
            selectedPicTxt.setText("File selected: " + data.getData().getLastPathSegment());
            selectedPicTxt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            iconCardView.setVisibility(View.VISIBLE);
            levelIcon.invalidate();
            levelIcon.setImageURI(filepath);
        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    //Method to acknowledge permission, Override means automatically called by Android
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 15 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectIcon();
        } else
            Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
    }

    //Upload pdf file
    private void uploadFile(Uri filepath, final String id) {

        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //TODO: settle uid
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String userId = user.getUid();

        //Store file in storage
        final String fileName = levelNameTxt.getText().toString() + "_LevelIcon";
        final StorageReference mStorageRef = storage.getReference().child("AssessmentLevel").child("levelIcons");

        mStorageRef.child(fileName).putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.e(TAG, "File successfully uploaded");

                        mStorageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e(TAG, "url: "+uri);
                                final DocumentReference reference = db.collection("AssessmentLevel").document(id);

                                reference.update("levelIconUrl", uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "File submission successful", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        }
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

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Edit Test Level");
    }

}