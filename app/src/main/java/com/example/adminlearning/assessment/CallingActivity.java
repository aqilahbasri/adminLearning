package com.example.adminlearning.assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private static final String TAG = "CallingActivity";
    private String receiverUserId = "", receiverUserImage = "", receiverFullName = "";
    private String senderUserId = "", senderUserImage = "", senderFullName = "", checker = "";
    private String callingId = "", ringingId = "";

    TextView username;
    ImageView userImage;
    ImageView rejectButton, acceptCallBtn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        username = findViewById(R.id.userName);
        userImage = findViewById(R.id.meetingInitiator);
        rejectButton = findViewById(R.id.rejectButton);
        acceptCallBtn = findViewById(R.id.acceptButton);

        senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUserId = getIntent().getExtras().getString("visit_user_id");

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);

        ref = database.getReference().child("Users");

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                checker = "clicked";
                cancelCallingUser();
            }
        });

        acceptCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();

                final HashMap<String, Object> callingPickUpMap = new HashMap<>();
                callingPickUpMap.put("picked", "picked");

                ref.child(senderUserId).child("Ringing").updateChildren(callingPickUpMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(CallingActivity.this, VideoCallActivity.class);
                                    startActivity(intent);
                                }

                            }
                        });
            }
        });

        getAndSetUserProfileInfo();
    }

    private void cancelCallingUser() {

        //sender
        ref.child(senderUserId).child("Calling").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChild(" calling")) {
                    callingId = snapshot.child("calling").getValue().toString();

                    ref.child(callingId).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                ref.child(senderUserId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(CallingActivity.this, com.example.adminlearning.assessment.MainActivity.class));
                                                finish();
                                            }}, 500);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(CallingActivity.this, com.example.adminlearning.assessment.MainActivity.class));
                            finish();
                        }}, 500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //receiver
        ref.child(senderUserId).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChild("ringing")) {
                    ringingId = snapshot.child("ringing").getValue().toString();

                    ref.child(ringingId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                ref.child(senderUserId).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(CallingActivity.this, com.example.adminlearning.assessment.MainActivity.class));
                                                finish();
                                            }}, 500);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(CallingActivity.this, com.example.adminlearning.assessment.MainActivity.class));
                            finish();
                        }}, 500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAndSetUserProfileInfo() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(receiverUserId).exists()) {
                    receiverUserImage = snapshot.child(receiverUserId).child("profileImage").getValue().toString();
                    receiverFullName = snapshot.child(receiverUserId).child("fullName").getValue().toString();

                    username.setText(receiverFullName);
                    Glide.with(getApplicationContext()).load(receiverUserImage).into(userImage);
                }

                if (snapshot.child(senderUserId).exists()) {
                    senderUserImage = snapshot.child(senderUserId).child("profileImage").getValue().toString();
                    senderFullName = snapshot.child(senderUserId).child("fullName").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mediaPlayer.start();

        ref.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!checker.equals("clicked") && !snapshot.hasChild("Calling") && !snapshot.hasChild("Ringing")) {

                    HashMap<String, Object> callingInfo = new HashMap<>();
                    callingInfo.put("calling", receiverUserId);

                    ref.child(senderUserId).child("Calling").updateChildren(callingInfo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        HashMap<String, Object> ringingInfo = new HashMap<>();
                                        ringingInfo.put("ringing", senderUserId);

                                        ref.child(receiverUserId).child("Ringing").updateChildren(ringingInfo);
                                    }

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(senderUserId).hasChild("Ringing") &&
                        !snapshot.child(senderUserId).hasChild("Calling")) {
                    acceptCallBtn.setVisibility(View.VISIBLE);
                }

                if(snapshot.child(receiverUserId).child("Ringing").hasChild("picked")) {
                    mediaPlayer.stop();
                    Intent intent = new Intent(CallingActivity.this, VideoCallActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}