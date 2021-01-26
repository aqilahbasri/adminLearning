package com.example.adminlearning.assessment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNewQuestionsFragment extends Fragment {

    private static final String TAG = "AddNewQuestionsFragment";
    private static AddNewQuestionsFragment uniqueInstance = new AddNewQuestionsFragment();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference;

    private EditText questionDetail, questionAnswer;
    TextView selectedGifTxt;
//    private Spinner questionType;
    private Button cancelBtn, confirmBtn;
    private Button searchGifButton;
    CardView gifView;
    TextView engCaption, malayCaption;
    WebView gifPicture;

    private AddNewQuestionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_questions, container, false);

        questionDetail = view.findViewById(R.id.question_detail);
//        questionType = view.findViewById(R.id.question_type);
        questionAnswer = view.findViewById(R.id.question_answer);
        cancelBtn = view.findViewById(R.id.cancel_button);
        confirmBtn = view.findViewById(R.id.confirm_button);
        searchGifButton = view.findViewById(R.id.search_gif_btn);
        selectedGifTxt = view.findViewById(R.id.selected_gif);
        gifView = view.findViewById(R.id.cardview_id);
        engCaption = view.findViewById(R.id.engCaption);
        malayCaption = view.findViewById(R.id.malayCaption);
        gifPicture = view.findViewById(R.id.gifPicture);

//        final QuestionTypeSpinner spinner = new QuestionTypeSpinner(getContext(), questionType);
//        spinner.spinnerActivity();

        final SearchGIFDialog dialog = SearchGIFDialog.getInstance();
        dialog.setFragContext(getContext());

        gifView.setVisibility(View.INVISIBLE);
        selectedGifTxt.setText("No GIF selected");
        selectedGifTxt.setTextColor(Color.parseColor("#b20000"));

        searchGifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getActivity().getSupportFragmentManager(), "SearchGIFDialog");
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionDetail.getText().length() != 0 && questionAnswer.getText().length() != 0 &&
                        !GIFAdapter.getInstance().getGifUrl().equals(null)) {
                    toDatabase();
                } else {
                    Toast.makeText(getContext(), "Please complete all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void toDatabase() {

        Date date = new Date();
        Long time = date.getTime();

        final String questionIDStr = "L1Q" + time;
        final String questionDetailStr = this.questionDetail.getText().toString();
//        final String questionTypeStr = spinner.getItem();
        final String questionAnswerStr = this.questionAnswer.getText().toString();
        GIFAdapter adapter = GIFAdapter.getInstance();

        final Map question = new HashMap<>();
        question.put("dateAdded", time);
        question.put("dateModified", time);
        question.put("questionID", questionIDStr);
        question.put("questionDetail", questionDetailStr);
//        question.put("questionType", questionTypeStr);
        question.put("correctAnswer", questionAnswerStr);
        question.put("gifUrl", adapter.getGifUrl());
        question.put("malayCaption", adapter.getMalayCaption());
        question.put("engCaption", adapter.getEngCaption());

        getReference().document(questionIDStr).set(question)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Question saved successfully");
                        Toast.makeText(getContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
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

    public static AddNewQuestionsFragment getInstance() {
        return uniqueInstance;
    }

    public CollectionReference getReference() {
        return reference;
    }

    public void setReference(CollectionReference reference) {
        this.reference = reference;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Add New Question");
    }

}