package com.example.adminlearning.assessment;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.adminlearning.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InitBottomBar {

    private Context context;
    InitBottomBar(Context context) {
        this.context = context;
    }

    void setNavigation(MainActivity activity) {
        BottomNavigationView bottom_bar;
        bottom_bar = activity.findViewById(R.id.bottom_bar);

        bottom_bar.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.nav_notifications:
                        selectedFragment = new NotificationsFragment();
                        break;
                }

                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
                return true;
            }
        });
    }
}