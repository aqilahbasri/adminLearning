package com.example.adminlearning.assessment;

import android.os.Bundle;

import com.example.adminlearning.R;

public class ManageCertificationActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ManageCertificationFragment()).commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //for back button

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container).addToBackStack().commit();
//        getSupportFragmentManager().addOnBackStackChangedListener(
//                new FragmentManager.OnBackStackChangedListener() {
//                    public void onBackStackChanged() {
//                        // Update your UI here.
//                        Fragment fragment = getFragment();
//                        setTitleFromFragment(fragment);
//                    }
//                });
    }

}