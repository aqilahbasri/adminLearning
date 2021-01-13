package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class EditGIFDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView actionBarTitle;
    private DatabaseReference RootRef;

    private WebView gifImage;
    private EditText engDescription, malayDescription, gifCategory;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gif_details);

        RootRef = FirebaseDatabase.getInstance().getReference();

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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFieldsValue();
            }
        });
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
}