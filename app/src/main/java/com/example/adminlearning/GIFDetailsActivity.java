package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class GIFDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView actionBarTitle;

    private WebView gifPicture;
    private TextView engDescription, malayDescription, warningDialog, warningDescriptionDialog;
    private Button editButton, deleteButton, cancelDialog, confirmDialog;
    private Dialog dialogBox;
    private ImageView warningImage;

    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_details);

        RootRef = FirebaseDatabase.getInstance().getReference();

        dialogBox = new Dialog(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("GIF Details");

        gifPicture = (WebView) findViewById(R.id.gifImage);
        engDescription = (TextView) findViewById(R.id.gifDescription);
        malayDescription = (TextView) findViewById(R.id.gifDescription2);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditGIFDetailsActivity.class);
                intent.putExtra("gifurl", getIntent().getStringExtra("gifurl"));
                intent.putExtra("engCaption", getIntent().getStringExtra("engCaption"));
                intent.putExtra("malayCaption", getIntent().getStringExtra("malayCaption"));
                intent.putExtra("category", getIntent().getStringExtra("category"));
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarningDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        gifPicture.loadUrl(getIntent().getStringExtra("gifurl"));
        gifPicture.getSettings().setUseWideViewPort(true);
        gifPicture.getSettings().setLoadWithOverviewMode(true);
        engDescription.setText(getIntent().getStringExtra("engCaption"));
        malayDescription.setText(getIntent().getStringExtra("malayCaption"));


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
                RootRef.child("SignLanguageGIF").child(getIntent().getStringExtra("engCaption")).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "GIF successfully deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), GIFLibraryActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    String message = task.getException().toString();
                                    Toast.makeText(getApplicationContext(), "Error : " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBox.show();
    }
}