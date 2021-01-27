package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageCourseworkSubmissionFragment extends Fragment {

    private static final String TAG = "ManageSubmissionFgmt";
    ArrayList<ManageCourseworkModalClass> newApplicationList;
    FirebaseDatabase database;
    DatabaseReference detailsRef;

    RecyclerView mRecyclerView;

    private DatabaseReference courseworkRef;

    public ManageCourseworkSubmissionFragment(DatabaseReference courseworkRef) {
        this.courseworkRef = courseworkRef;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_coursework_submission, container, false);

        mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        newApplicationList = new ArrayList<>();

        //TODO: NANTI DEBUG BENDA NI. WAJIB
        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageCoursework").child("CourseworkSubmissions");
        detailsRef.keepSynced(true);

        courseworkRef.child("CourseworkSubmissions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newApplicationList.clear();
                String key = null;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ManageCourseworkModalClass manageCourseworkModalClass;
                    manageCourseworkModalClass = ds.getValue(ManageCourseworkModalClass.class);
                    newApplicationList.add(manageCourseworkModalClass);
                    key = ds.getKey();
                }

                GetCourseworkSubmissionAdapter adapter = new GetCourseworkSubmissionAdapter(getActivity(), newApplicationList);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });

        //get questions first, then from question view submission

//        marksBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddNewCourseworkDialog addNewCourseworkDialog = new AddNewCourseworkDialog(getActivity());
//                addNewCourseworkDialog.show(getActivity().getSupportFragmentManager(), TAG);
//                Log.e(TAG, getActivity().toString());
//            }
//        });

        return view;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Manage Coursework");
    }
}