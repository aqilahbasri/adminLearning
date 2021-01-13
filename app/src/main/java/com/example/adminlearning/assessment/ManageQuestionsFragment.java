package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.adminlearning.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageQuestionsFragment extends Fragment {

    private static final String TAG = "ManageQuestionsFgmt";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TestSettingsLevelAdapter adapter;

    public ManageQuestionsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_manage_questions, container, false);

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

//        CardView level1 = view.findViewById(R.id.level1);
//        CardView level2 = view.findViewById(R.id.level2);
//        CardView level3 = view.findViewById(R.id.level3);
//
//        level1.setOnClickListener(this);
//        level2.setOnClickListener(this);
//        level3.setOnClickListener(this);
        return view;
    }

    /*
    //@Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.level1:
                selectedFragment = new Level1QuestionsFragment();
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
    } */

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Select Level");
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