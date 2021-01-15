package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditGIFDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView actionBarTitle;
    private DatabaseReference RootRef;

    private WebView gifImage;
    private EditText engDescription, malayDescription, gifCategory;
    private Button confirmButton, approveButton, rejectButton, cancelDialog, confirmDialog;
    private Dialog dialogBox;
    private StorageTask uploadTask;
    private TextView warningDialog, warningDescriptionDialog;
    private ImageView warningImage;

    private String type;
    private String saveCurrentTime, saveCurrentDate;
//    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gif_details);

        RootRef = FirebaseDatabase.getInstance().getReference();
        type = getIntent().getStringExtra("type");

        dialogBox = new Dialog(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("Edit GIF");

        gifImage = findViewById(R.id.gifImage);
        engDescription = findViewById(R.id.gifEngDescription);
        malayDescription = findViewById(R.id.gifMalayDescription);
        gifCategory = findViewById(R.id.gifCategory);
        confirmButton = findViewById(R.id.confirmButton);
        approveButton = (Button) findViewById(R.id.approveButton);
        rejectButton = (Button) findViewById(R.id.rejectButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFieldsValue();
            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> messageDetails = (HashMap<String, String>) getIntent().getSerializableExtra("messageDetails");
                sendMessageToUser(messageDetails);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarningDialog();
            }
        });
    }

    private void sendMessageToUser(final HashMap<String, String> messageDetails){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        messageDetails.put("date", saveCurrentDate);
        messageDetails.put("time", saveCurrentTime);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

        final String messageSenderRef = "Messages/" + messageDetails.get("from") + "/" + messageDetails.get("to");
        final String messageReceiverRef = "Messages/" + messageDetails.get("to") + "/" + messageDetails.get("from");

        DatabaseReference userMessageKeyRef = RootRef.child("Messages").child(messageDetails.get("from")).child(messageDetails.get("to")).push();

        final String messagePushID = messageDetails.get("messageID");

        Map messageBodyDetails = new HashMap();
        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageDetails);
        messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageDetails);

        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                    deleteDataFromPending(messageDetails);
                    AddToLibrary();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteDataFromPending(HashMap<String, String> messageDetails) {
        final String messagePushID = messageDetails.get("messageID");
        
        RootRef.child("PendingGIF").child(messageDetails.get("from")).child(messagePushID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "GIF successfully deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PendingLibraryActivity.class);
                            startActivity(intent);
                        }
                        else{
                            String message = task.getException().toString();
                            Toast.makeText(getApplicationContext(), "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        RootRef.child("PendingGIFLibrary").child(messagePushID).removeValue();

    }

    private void showWarningDialog() {
        dialogBox.setContentView(R.layout.custom_dialog_box);
        warningImage = dialogBox.findViewById(R.id.warningImage);
        warningDialog = dialogBox.findViewById(R.id.warningDialog);
        warningDescriptionDialog = dialogBox.findViewById(R.id.warningDescriptionDialog);
        cancelDialog = dialogBox.findViewById(R.id.cancelDialog);
        confirmDialog = dialogBox.findViewById(R.id.confirmDialog);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.dismiss();
            }
        });

        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> messageDetails = (HashMap<String, String>) getIntent().getSerializableExtra("messageDetails");
                deleteDataFromPending(messageDetails);
            }
        });

        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBox.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gifImage.loadUrl(getIntent().getStringExtra("gifurl"));
        gifImage.getSettings().setUseWideViewPort(true);
        gifImage.getSettings().setLoadWithOverviewMode(true);
        engDescription.setText(getIntent().getStringExtra("engCaption"));
        malayDescription.setText(getIntent().getStringExtra("malayCaption"));
        gifCategory.setText(getIntent().getStringExtra("category"));

        type = getIntent().getStringExtra("type");

        switch(type){
            case "GIF" : confirmButton.setVisibility(View.VISIBLE);
                         approveButton.setVisibility(View.INVISIBLE);
                         rejectButton.setVisibility(View.INVISIBLE);
                         break;
            case "Pending" : confirmButton.setVisibility(View.INVISIBLE);
                         approveButton.setVisibility(View.VISIBLE);
                         rejectButton.setVisibility(View.VISIBLE);
                         break;
        }
    }

    private void UpdateFieldsValue() {
        final String setEngDescription = engDescription.getText().toString();
        final String setMalayDescription = malayDescription.getText().toString();
        final String setCategory = gifCategory.getText().toString();

        if(TextUtils.isEmpty(setEngDescription)){
            Toast.makeText(this, "Please enter English Caption.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(setMalayDescription)){
            Toast.makeText(this, "Please enter MalayCaption Caption.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(setCategory)){
            Toast.makeText(this, "Please enter Category.", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String> gifDetails = new HashMap<>();
            gifDetails.put("engCaption", setEngDescription);
            gifDetails.put("malayCaption", setMalayDescription);
            gifDetails.put("category", setCategory);
            gifDetails.put("gifPicture", getIntent().getStringExtra("gifurl"));

            RootRef.child("SignLanguageGIF").child(getIntent().getStringExtra("engCaption")).removeValue();
            RootRef.child("SignLanguageGIF").child(setEngDescription).push();
            RootRef.child("SignLanguageGIF").child(setEngDescription).setValue(gifDetails).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), GIFDetailsActivity.class);
                                intent.putExtra("gifPicture", getIntent().getStringExtra("gifurl"));
                                intent.putExtra("engCaption", setEngDescription);
                                intent.putExtra("malayCaption", setMalayDescription);
                                intent.putExtra("category", setCategory);
                                startActivity(intent);
                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(getApplicationContext(), "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void AddToLibrary() {
        final String setEngDescription = engDescription.getText().toString();
        final String setMalayDescription = malayDescription.getText().toString();
        final String setCategory = gifCategory.getText().toString();

        if(TextUtils.isEmpty(setEngDescription)){
            Toast.makeText(this, "Please enter English Caption.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(setMalayDescription)){
            Toast.makeText(this, "Please enter MalayCaption Caption.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(setCategory)){
            Toast.makeText(this, "Please enter Category.", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String> gifDetails = new HashMap<>();
            gifDetails.put("engCaption", setEngDescription);
            gifDetails.put("malayCaption", setMalayDescription);
            gifDetails.put("category", setCategory);
            gifDetails.put("gifPicture", getIntent().getStringExtra("gifurl"));

            RootRef.child("SignLanguageGIF").child(setEngDescription).push();
            RootRef.child("SignLanguageGIF").child(setEngDescription).setValue(gifDetails).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), GIFLibraryActivity.class);
                                intent.putExtra("gifPicture", getIntent().getStringExtra("gifurl"));
                                intent.putExtra("engCaption", setEngDescription);
                                intent.putExtra("malayCaption", setMalayDescription);
                                intent.putExtra("category", setCategory);
                                startActivity(intent);
                            }
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(getApplicationContext(), "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}