package com.example.adminlearning.assessment;

import android.os.Bundle;
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

public class ViewNewCertApplicationFragment extends Fragment {

    ArrayList<ApplyCertContactInfo> newApplicationList;
    FirebaseDatabase database;
    DatabaseReference detailsRef;

    public ViewNewCertApplicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_new_cert_application, container, false);

        final RecyclerView mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        newApplicationList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("CertApplication_StudentInfo").child("NewApplication");

        Query query = detailsRef.orderByChild("appliedTimestamp");
        detailsRef.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newApplicationList.clear();
                String key = null;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ApplyCertContactInfo certDetails;
                    certDetails = ds.getValue(ApplyCertContactInfo.class);
                    newApplicationList.add(certDetails);
                    key = ds.getKey();
                }
                GetNewCertApplicationAdapter adapter = new GetNewCertApplicationAdapter(getActivity(), newApplicationList, key);
                mRecyclerView.setAdapter(adapter);
                Collections.reverse(newApplicationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}