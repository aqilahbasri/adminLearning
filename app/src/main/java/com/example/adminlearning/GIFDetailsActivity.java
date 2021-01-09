package com.example.adminlearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class GIFDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView actionBarTitle;

    private WebView gifPicture;
    private TextView engDescription, malayDescription;
    private Button editButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_details);

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
}