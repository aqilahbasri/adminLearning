package com.example.adminlearning.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.adminlearning.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "";
    private TextView actionBarTitle;
    Boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBarTitle = findViewById(R.id.toolbar_title);

        InitBottomBar ibb = new InitBottomBar(this);    //Class for bottom nav_graph
        ibb.setNavigation(this);

        drawer = findViewById(R.id.nav_drawer_layout);
        ImageButton rightMenu = findViewById(R.id.ic_right_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        rightMenu.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AdministrationMenuFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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

    //For actions when PHONE back button is pressed
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (isTaskRoot()) {    //Only can exit from Home fragment
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            } else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void goToMainLearningModule() {
        Intent mainLearningModule = new Intent(com.example.adminlearning.assessment.MainActivity.this, com.example.adminlearning.MainActivity.class);
        startActivity(mainLearningModule);
    }

    private void goToMainAssessmentModule() {
        Intent mainAssessmentModule = new Intent(com.example.adminlearning.assessment.MainActivity.this, com.example.adminlearning.assessment.MainActivity.class);
        startActivity(mainAssessmentModule);
    }

    private void goToMainCommunicationModule() {
        Intent mainCommunicationModule = new Intent(com.example.adminlearning.assessment.MainActivity.this, com.example.adminlearning.MainCommunicationActivity.class);
        startActivity(mainCommunicationModule);
    }

    @Override
    public void onClick(View v) {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            drawer.openDrawer(GravityCompat.END);
        }
    }

    //Set action bar title.
    //P.s. have to use TextView instead of getSupportActionBar because nav_graph drawer is set to the right.
    public void setTitle(final String title) {
        actionBarTitle.setText(title);
    }

    // Change the title back when the fragment is changed
    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assert fragment != null;
        fragment.onResume();
    }
}
