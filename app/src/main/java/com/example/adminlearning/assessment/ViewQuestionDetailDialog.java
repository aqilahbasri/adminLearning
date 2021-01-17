package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class ViewQuestionDetailDialog extends AppCompatDialogFragment {

    private static final String TAG = "ViewQuestionsDialog";
    int position;
    private TextView engCaption, malayCaption;
    private WebView gifPicture;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reference;

    private TextView questionID, dateAdded, dateModified, questionDetail, questionType, questionAnswer;

    public ViewQuestionDetailDialog(DocumentReference reference, int position) {
        this.reference = reference;
        this.position = position;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);
        builder.setTitle("View question details");
        builder.setMessage("View question details for question " + (position+1));
        setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog_view_question_detail, null);

        questionID = view.findViewById(R.id.question_id);
        dateAdded = view.findViewById(R.id.date_added);
        dateModified = view.findViewById(R.id.date_modified);
        questionDetail = view.findViewById(R.id.question_detail);
        questionType = view.findViewById(R.id.question_type);
        questionAnswer = view.findViewById(R.id.question_answer);
        engCaption = view.findViewById(R.id.engCaption);
        malayCaption = view.findViewById(R.id.malayCaption);
        gifPicture = view.findViewById(R.id.gifPicture);

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String questionIDStr = documentSnapshot.getString("questionID");
                Long dateAddedLong = documentSnapshot.getLong("dateAdded");
                Long dateModifiedLong = documentSnapshot.getLong("dateModified");
                String questionDetailStr = documentSnapshot.getString("questionDetail");
                String questionTypeStr = documentSnapshot.getString("questionType");
                String questionAnswerStr = documentSnapshot.getString("correctAnswer");

                String myFormat = "dd/MM/yyyy HH:mm"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                String addedDate = sdf.format(dateAddedLong);
                String modifiedDate = sdf.format(dateModifiedLong);

                questionID.setText(questionIDStr);
                dateAdded.setText(addedDate);
                dateModified.setText(modifiedDate);
                questionDetail.setText(questionDetailStr);
                questionType.setText(questionTypeStr);
                questionAnswer.setText(questionAnswerStr);

                gifPicture.loadUrl(documentSnapshot.getString("gifUrl"));
                gifPicture.getSettings().setLoadWithOverviewMode(true);
                gifPicture.getSettings().setUseWideViewPort(true);
                engCaption.setText(documentSnapshot.getString("engCaption"));
                malayCaption.setText(documentSnapshot.getString("malayCaption"));
            }
        });

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}