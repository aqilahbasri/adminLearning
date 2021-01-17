package com.example.adminlearning.assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.adminlearning.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallActivity extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener {

    private static String API_KEY = "47084734";
    private static String SESSION_ID = "2_MX40NzA4NDczNH5-MTYxMDg0MDY2OTA0NH5zeXZvdUVGSmxRMGdWdjJPWWwzMWdTZll-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NzA4NDczNCZzaWc9YTYzNTU3YWFkZDBmZDNlNjNjZmQ5NzNmNzIwODdmOTMwZWQ1NzA1NTpzZXNzaW9uX2lkPTJfTVg0ME56QTRORGN6Tkg1LU1UWXhNRGcwTURZMk9UQTBOSDV6ZVhadmRVVkdTbXhSTUdkV2RqSlBXV3d6TVdkVFpsbC1mZyZjcmVhdGVfdGltZT0xNjEwODQwNzcxJm5vbmNlPTAuMzA1NzA2NTY1NzIyMzA2Mzcmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYxMzQzMjc3MSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;

    private ImageView closeVideoChatBtn;
    private DatabaseReference ref;
    private String userID = "";

    private FrameLayout mPubViewController;
    private FrameLayout mSubViewController;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        closeVideoChatBtn = findViewById(R.id.rejectButton);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSession.disconnect();
            }
        });
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VideoCallActivity.this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this, perms)) {

            mPubViewController = findViewById(R.id.pub_container);
            mSubViewController = findViewById(R.id.sub_container);

            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(VideoCallActivity.this);
            mSession.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "Camera and microphone permissions are needed",
                    RC_VIDEO_APP_PERM, perms);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(userID).hasChild("Ringing")) {
                    ref.child(userID).child("Ringing").removeValue();

                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }

                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }
                }

                if (snapshot.child(userID).hasChild("Calling")) {
                    ref.child(userID).child("Calling").removeValue();

                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }

                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }

                } else {

                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }

                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }

                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(VideoCallActivity.this, com.example.adminlearning.assessment.MainActivity.class));
                        finish();
                    }
                }, 1500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });


    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoCallActivity.this);
        mPubViewController.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);

    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(userID).hasChild("Ringing")) {
                    ref.child(userID).child("Ringing").removeValue();

                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }

                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }
                }

                if (snapshot.child(userID).hasChild("Calling")) {
                    ref.child(userID).child("Calling").removeValue();

                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }

                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }

                } else {

                    if (mPublisher != null) {
                        mPublisher.destroy();
                    }

                    if (mSubscriber != null) {
                        mSubscriber.destroy();
                    }

                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(VideoCallActivity.this, com.example.adminlearning.assessment.MainActivity.class));
                        finish();
                    }
                }, 1500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(LOG_TAG, error.getMessage());
            }
        });

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubViewController.addView(mSubscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubViewController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}