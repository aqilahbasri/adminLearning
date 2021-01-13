package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddSectionLevelFragment extends Fragment {

    private static final String TAG = "AddSectionLevelFragment";
    private EditText sectionNameTxt, noOfQuestionsTxt, sectionPassMarkTxt;
    private Button cancelBtn, confirmBtn;
    private String key;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AddSectionLevelFragment(String key) {
        this.key = key;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_section, container, false);

        sectionNameTxt = view.findViewById(R.id.editText_1);
        noOfQuestionsTxt = view.findViewById(R.id.editText_2);
        sectionPassMarkTxt = view.findViewById(R.id.editText_3);

        cancelBtn = view.findViewById(R.id.cancel_button);
        confirmBtn = view.findViewById(R.id.confirm_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sectionNameTxt.getText().length() != 0 && noOfQuestionsTxt.getText().length() != 0 &&
                        sectionPassMarkTxt.getText().length() != 0) {
                    toDatabase();
                }
                else {
                    Toast.makeText(getContext(), "Please complete all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    //Send initial info to db
    private void toDatabase() {

        CollectionReference reference = db.collection("AssessmentLevel")
                .document(key).collection("Sections");

        Date date = new Date();
        Long time = date.getTime();

        String levelName = sectionNameTxt.getText().toString();
        int noOfQuestions = Integer.parseInt(noOfQuestionsTxt.getText().toString());
        int sectionPassingMark = Integer.parseInt(sectionPassMarkTxt.getText().toString());

        Map question = new HashMap<>();
        question.put("dateAdded", time);
        question.put("dateModified", time);
        question.put("noOfQuestions", noOfQuestions);
        question.put("sectionName", levelName);
        question.put("sectionPassMark", sectionPassingMark);

        reference.add(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Section details saved successfully");
                getActivity().getSupportFragmentManager().popBackStack();
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

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Add New Section");
    }

}