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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditLevel1QuestionFragment extends Fragment {

    private static final String TAG = "EditQuestionsFragment";
    private static EditLevel1QuestionFragment uniqueInstance = new EditLevel1QuestionFragment();
    int position;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reference;

    private TextView questionIDTxt;
    private EditText questionDetail, questionAnswer;
    Spinner questionType;
    TextView selectedGifTxt;
    private Button cancelBtn, confirmBtn;
    private Button searchGifButton;
    CardView gifView;
    TextView engCaption, malayCaption;
    WebView gifPicture;

    private EditLevel1QuestionFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_questions, container, false);

        questionDetail = view.findViewById(R.id.question_detail);
        questionType = view.findViewById(R.id.question_type);
        questionAnswer = view.findViewById(R.id.question_answer);
        cancelBtn = view.findViewById(R.id.cancel_button);
        confirmBtn = view.findViewById(R.id.confirm_button);
        searchGifButton = view.findViewById(R.id.search_gif_btn);
        selectedGifTxt = view.findViewById(R.id.selected_gif);
        gifView = view.findViewById(R.id.cardview_id);
        engCaption = view.findViewById(R.id.engCaption);
        malayCaption = view.findViewById(R.id.malayCaption);
        gifPicture = view.findViewById(R.id.gifPicture);
        questionIDTxt = view.findViewById(R.id.question_id);

        final QuestionTypeSpinner spinner = new QuestionTypeSpinner(getContext(), questionType);
        spinner.spinnerActivity();

        final SearchGIFDialog dialog = SearchGIFDialog.getInstance();
        dialog.setFragContext(getContext());

        selectedGifTxt.setText("No GIF selected");
        selectedGifTxt.setTextColor(Color.parseColor("#b20000"));

        getInfoFromDatabase();

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
                    toDatabase(spinner);
                } else {
                    Toast.makeText(getContext(), "Please complete all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getInfoFromDatabase() {

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String questionIDStr = documentSnapshot.getString("questionID");
                String questionDetailStr = documentSnapshot.getString("questionDetail");
                String questionTypeStr = documentSnapshot.getString("questionType");
                String questionAnswerStr = documentSnapshot.getString("correctAnswer");
                String malayCaptionStr = documentSnapshot.getString("malayCaption");
                String engCaptionStr = documentSnapshot.getString("engCaption");

                questionIDTxt.setText(questionIDStr);
                questionDetail.setText(questionDetailStr);
//                questionType.set
//                questionType.setText(questionTypeStr);
                questionAnswer.setText(questionAnswerStr);

                selectedGifTxt.setText("You have initially selected GIF of \"" +malayCaptionStr+ "/" +engCaptionStr+ "\"");
                gifPicture.loadUrl(documentSnapshot.getString("gifUrl"));
                gifPicture.getSettings().setLoadWithOverviewMode(true);
                gifPicture.getSettings().setUseWideViewPort(true);
                engCaption.setText(engCaptionStr);
                malayCaption.setText(malayCaptionStr);
            }
        });
    }

    private void toDatabase(QuestionTypeSpinner spinner) {

//        CollectionReference reference = db.collection("Level1TestQuestions");

        Date date = new Date();
        Long time = date.getTime();

        final String questionDetailStr = this.questionDetail.getText().toString();
        final String questionTypeStr = spinner.getItem();
        final String questionAnswerStr = this.questionAnswer.getText().toString();
        GIFAdapter adapter = GIFAdapter.getInstance();

        final Map question = new HashMap<>();
        question.put("dateModified", time);
        question.put("questionDetail", questionDetailStr);
        question.put("questionType", questionTypeStr);
        question.put("correctAnswer", questionAnswerStr);
        question.put("gifUrl", adapter.getGifUrl());
        question.put("malayCaption", adapter.getMalayCaption());
        question.put("engCaption", adapter.getEngCaption());

        reference.update(question)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Question saved successfully");
                        Toast.makeText(getContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static EditLevel1QuestionFragment getInstance() {
        return uniqueInstance;
    }

    public DocumentReference getReference() {
        return reference;
    }

    public void setReference(DocumentReference reference) {
        this.reference = reference;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Edit Question Detail");
    }
}
