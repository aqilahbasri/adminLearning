package com.example.adminlearning.assessment;

import android.os.Bundle;

import com.example.adminlearning.R;

public class ManageTestSettingsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ManageTestSettingsFragment()).commit();
        }
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity) getActivity()).setTitle("Manage Test Questions");
        setTitle("Manage Assessment Settings");
    }
}