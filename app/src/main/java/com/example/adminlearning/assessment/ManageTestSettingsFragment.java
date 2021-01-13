package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.adminlearning.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageTestSettingsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TestSettingsLevelAdapter adapter;

    public ManageTestSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_manage_test_settings, container, false);

        Button addLevelBtn = view.findViewById(R.id.add_level_btn);

        final RecyclerView mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        CollectionReference reference = db.collection("AssessmentLevel");
        Query query = reference.orderBy("levelName");

        FirestoreRecyclerOptions<TestSettingsLevelModel> options = new FirestoreRecyclerOptions.Builder<TestSettingsLevelModel>()
                .setQuery(query, TestSettingsLevelModel.class).build();

        adapter = new TestSettingsLevelAdapter(options);
        adapter.setActivity(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddTestLevelFragment()).addToBackStack(null).commit();
            }
        });

        return view;
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
