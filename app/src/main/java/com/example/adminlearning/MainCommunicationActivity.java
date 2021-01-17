package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainCommunicationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView actionBarTitle;
    private Button gifLibrary, pendingLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_communication);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("Communication");
        ImageButton rightMenu = findViewById(R.id.ic_right_menu);
        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.com_nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        goToMainCommunicationModule();
                        break;
                    case R.id.nav_learning:
                        goToMainLearningModule();
                        break;
                    case R.id.nav_assessment:
                        goToMainAssessmentModule();
                        break;
                }
                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        gifLibrary = (Button) findViewById(R.id.gifLibrary);
        gifLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGIFLibrary();
            }

        });

        pendingLibrary = (Button) findViewById(R.id.pendingLibrary);
        pendingLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPendingLibrary();
            }

        });
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void goToMainLearningModule() {
        Intent mainLearningModule = new Intent(this, MainActivity.class);
        startActivity(mainLearningModule);
    }

    private void goToMainAssessmentModule() {
        Intent mainAssessmentModule = new Intent(this, com.example.adminlearning.assessment.MainActivity.class);
        startActivity(mainAssessmentModule);
    }

    private void goToMainCommunicationModule() {
        Intent mainCommunicationModule = new Intent(this, MainCommunicationActivity.class);
        startActivity(mainCommunicationModule);
    }

    private void goToPendingLibrary() {
        Intent intent = new Intent(this, PendingLibraryActivity.class);
        startActivity(intent);
    }

    private void goToGIFLibrary() {
        Intent intent = new Intent(this, GIFLibraryActivity.class);
        startActivity(intent);
    }

}