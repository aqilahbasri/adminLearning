package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ManageCertificationFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ManageCertificationFragment extends Fragment implements View.OnClickListener {

    ArrayList<ApplyCertContactInfo> newApplicationList;
    FirebaseDatabase database;
    DatabaseReference detailsRef;
    private static final String TAG = "ManageCertFragment";

    Button viewNewApp;

    public ManageCertificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((MainActivity) getActivity()).setTitle("Manage Certification");  //set action bar title here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_certification, container, false);
        viewNewApp = view.findViewById(R.id.new_application_btn);
        viewNewApp.setOnClickListener(this);

        final RecyclerView mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        newApplicationList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("CertApplication_StudentInfo").child("ApprovedCertification");
        detailsRef.keepSynced(true);

        detailsRef.orderByChild("approvedTimestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newApplicationList.clear();
                String key = null;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ApplyCertContactInfo applyCertContactInfo;
                    applyCertContactInfo = ds.getValue(ApplyCertContactInfo.class);
                    newApplicationList.add(applyCertContactInfo);
                    key = ds.getKey();
                }

                GetApprovedCertificationAdapter adapter = new GetApprovedCertificationAdapter(getActivity(), newApplicationList, key);
                mRecyclerView.setAdapter(adapter);
                Collections.reverse(newApplicationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });

        return view;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Manage Certification");
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ViewNewCertApplicationFragment()).addToBackStack(null).commit();
    }
}