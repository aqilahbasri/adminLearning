package com.example.adminlearning.assessment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ManageOnlineInterviewFragment extends Fragment implements View.OnClickListener {

    ArrayList<OnlineInterviewApplication> newApplicationList;
    ArrayList<OnlineInterviewApplication> completedInterviewList;
    FirebaseDatabase database;
    DatabaseReference detailsRef;

    Button viewNewApp;

    private String calledBy = "";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final String TAG = "ManageInterviewFgmt";

    public ManageOnlineInterviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_interview_menu, container, false);
        viewNewApp = view.findViewById(R.id.new_application_btn);
        viewNewApp.setOnClickListener(this);

        final RecyclerView mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageOnlineInterview");
        detailsRef.keepSynced(true);

        //view by sortOrder
//        Query query = detailsRef.orderByChild("sortOrder");
        newApplicationList = new ArrayList<>();
        detailsRef.child("ScheduledInterview").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                newApplicationList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    OnlineInterviewApplication interviewApplication;
                    interviewApplication = ds.getValue(OnlineInterviewApplication.class);
                    newApplicationList.add(interviewApplication);
                }

                GetScheduledInterviewAdapter adapter = new GetScheduledInterviewAdapter(getActivity(), newApplicationList);
                mRecyclerView.setAdapter(adapter);

                Collections.reverse(newApplicationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("checkError", "Error: "+error);
            }
        });

        final RecyclerView completeRecyclerView = view.findViewById(R.id.list_name_2);
        completeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        completedInterviewList = new ArrayList<>();

        detailsRef.child("CompletedInterview").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    OnlineInterviewApplication interviewApplication;
                    interviewApplication = ds.getValue(OnlineInterviewApplication.class);
                    completedInterviewList.add(interviewApplication);
                }

                GetCompletedInterviewAdapter adapter = new GetCompletedInterviewAdapter(getActivity(), completedInterviewList);
                completeRecyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });



        return view;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Manage Online Interview");
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new NewOnlineInterviewFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkForReceivingCall();
    }

    private void checkForReceivingCall() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = db.getReference().child("Users").child(userId).child("Ringing");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("ringing")) {
                    calledBy = snapshot.child("ringing").getValue().toString();

                    Intent intent = new Intent((ManageOnlineInterviewActivity) getActivity(), CallingActivity.class);
                    intent.putExtra("visit_user_id", calledBy);
                    getActivity().startActivity(intent);
//                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }
}