package com.example.adminlearning.assessment;

import android.os.Bundle;

import com.example.adminlearning.R;

public class ManageOnlineInterviewActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ManageOnlineInterviewFragment()).commit();
        }
    }
}