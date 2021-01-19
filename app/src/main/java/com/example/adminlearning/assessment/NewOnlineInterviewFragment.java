package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class NewOnlineInterviewFragment extends Fragment {

    ArrayList<OnlineInterviewApplication> newApplicationList;
    FirebaseDatabase database;
    DatabaseReference detailsRef;
    int weightage = 0;
    private static final String TAG = "NewInterviewFragment";

    public NewOnlineInterviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_online_interview, container, false);

        final RecyclerView mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        newApplicationList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageOnlineInterview").child("NewApplication");
        detailsRef.keepSynced(true);

        //business logic recommendation algorithm
        detailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    updateAssessmentMarks(ds.getKey(), ds.getRef());    //get latest assessment marks first
                    updateCourseworkSubmission(ds.getKey(), ds.getRef());//get latest submission details first

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OnlineInterviewApplication interviewApplication;
                            interviewApplication = ds.getValue(OnlineInterviewApplication.class);

                            HashMap<String, Object> values = new HashMap<>();
                            for (DataSnapshot ds1 : ds.getChildren()) {

                                int sortOrder = 0;
                                if (interviewApplication != null) {
                                    sortOrder = interviewApplication.getOverallMark().intValue();
                                }

                                if (!ds.child("sortOrder").exists()) {
                                    if (ds.child("completeAssessment").getValue().equals(true) && ds.child("completeSubmission").getValue().equals(true)) {
                                        weightage = 4;
                                    } else if (ds.child("completeAssessment").getValue().equals(true) && ds.child("completeSubmission").getValue().equals(false)) {
                                        weightage = 2;
                                    } else if (ds.child("completeAssessment").getValue().equals(false) && ds.child("completeSubmission").getValue().equals(true)) {
                                        weightage = 2;
                                    } else weightage = 1;
                                    int newSortOrder = sortOrder * weightage;
                                    values.put("sortOrder", newSortOrder);
                                    ds.getRef().updateChildren(values);
                                }   //endif

                            }   //endfords1
                        }
                    }, 3000);

                } //endfords
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("checkError", "Error: " + error);
            }
        });

        //view by sortOrder
        Query query = detailsRef.orderByChild("sortOrder");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                newApplicationList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    OnlineInterviewApplication interviewApplication;
                    interviewApplication = ds.getValue(OnlineInterviewApplication.class);
                    newApplicationList.add(interviewApplication);
                }

                GetNewInterviewApplicationAdapter adapter = new GetNewInterviewApplicationAdapter(getActivity(), newApplicationList);
                mRecyclerView.setAdapter(adapter);

                Collections.reverse(newApplicationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("checkError", "Error: "+error);
            }
        });

        return view;
    }

    private void updateCourseworkSubmission(String key, DatabaseReference currentRef) {
        DatabaseReference submissionRef = FirebaseDatabase.getInstance().getReference().child("ManageCoursework")
                .child("CourseworkSubmissions");

        submissionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean completeSubmission;

                if(snapshot.hasChild(key)) {
                    completeSubmission = true;
                }
                else {
                    completeSubmission = false;
                }
                currentRef.child("completeSubmission").setValue(completeSubmission);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    //Update latest assessment marks, user may complete it after application
    public void updateAssessmentMarks(String key, DatabaseReference currentRef) {
        Log.i(TAG, "key: "+key);
        DatabaseReference scoreRef = FirebaseDatabase.getInstance().getReference().child("AssessmentMark");

        scoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int marks;
                boolean completeAssessment;

                if(snapshot.hasChild(key) && snapshot.child(key) != null) {
                    Log.i(TAG, "has child? "+snapshot.hasChild(key));
                    marks = Integer.parseInt(snapshot.child(key).child("Score").getValue().toString());
                    completeAssessment = true;
                }
                else {
                    marks = 0;
                    completeAssessment = false;
                }
                currentRef.child("overallMark").setValue(marks);
                currentRef.child("completeAssessment").setValue(completeAssessment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

}