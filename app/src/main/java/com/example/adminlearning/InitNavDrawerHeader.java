package com.example.adminlearning;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitNavDrawerHeader {

    NavigationView navigationView;
    private static String TAG;

    public InitNavDrawerHeader(NavigationView navigationView, String TAG) {
        this.navigationView = navigationView;
        this.TAG = TAG;
        initHeader();
    }

    public void initHeader() {
        // New code for navDrawer userImage+name starts here
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        final CircleImageView userImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        final TextView fullNameTxt = navigationView.getHeaderView(0).findViewById(R.id.user_fullName);
        final TextView userEmailTxt = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        String userId = user.getUid();

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = snapshot.child("fullName").getValue().toString();
                String userImageUrl = snapshot.child("profileImage").getValue().toString();
                String userEmail = snapshot.child("email").getValue().toString();

                fullNameTxt.setText(fullName);
                userEmailTxt.setText(userEmail);
                Picasso.get().load(userImageUrl).into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "ERROR: "+error.getMessage());
            }
        });
        // Code ends here
    }
}
