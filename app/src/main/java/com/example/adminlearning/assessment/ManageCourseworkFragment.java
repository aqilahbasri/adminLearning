package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageCourseworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageCourseworkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "ManageCourseworkFgmt";
    ArrayList<ManageCourseworkModalClass> newApplicationList;
    FirebaseDatabase database;
    DatabaseReference detailsRef;

    FloatingActionButton addCourseworkBtn;
    RecyclerView mRecyclerView;

    private GetManageCourseworkAdapter adapter;

    public ManageCourseworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageCourseworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageCourseworkFragment newInstance(String param1, String param2) {
        ManageCourseworkFragment fragment = new ManageCourseworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_coursework, container, false);

        mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        newApplicationList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("ManageCoursework").child("CourseworkQuestions"); //TODO: Later set by level
        detailsRef.keepSynced(true);

        FirebaseRecyclerOptions<ManageCourseworkModalClass> options =
                new FirebaseRecyclerOptions.Builder<ManageCourseworkModalClass>()
                        .setQuery(detailsRef, ManageCourseworkModalClass.class)
                        .build();

        adapter = new GetManageCourseworkAdapter(options);
        adapter.setActivity(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        /*
        detailsRef.orderByChild("createdTimestamp").addValueEventListener(new ValueEventListener() {
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

                GetManageCourseworkAdapter adapter = new GetManageCourseworkAdapter(getActivity(), newApplicationList);
                mRecyclerView.setAdapter(adapter);
//                Collections.reverse(newApplicationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });

         */

        //get questions first, then from question view submission
        addCourseworkBtn = view.findViewById(R.id.add_coursework_btn);
        addCourseworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewCourseworkDialog addNewCourseworkDialog = new AddNewCourseworkDialog(getActivity());
                addNewCourseworkDialog.show(getActivity().getSupportFragmentManager(), TAG);
                Log.e(TAG, getActivity().toString());
            }
        });

        return view;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Manage Coursework");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}