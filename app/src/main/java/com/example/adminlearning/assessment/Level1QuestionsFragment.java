package com.example.adminlearning.assessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Level1QuestionsFragment extends Fragment {

    private static final String TAG = "Level1QuestionsFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Level1QuestionsAdapter adapter;
    private TextView noQuestion;

    FloatingActionButton addQuestionsBtn;

    private DocumentReference reference;

    public Level1QuestionsFragment(DocumentReference reference) {
        this.reference = reference;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_level_1_questions, container, false);

        final RecyclerView mRecyclerView = view.findViewById(R.id.list_name);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        final CollectionReference collection = reference.collection("Questions");
        FirestoreRecyclerOptions<Level1QuestionsModel> options = new FirestoreRecyclerOptions.Builder<Level1QuestionsModel>()
                .setQuery(collection, Level1QuestionsModel.class).build();

        adapter = new Level1QuestionsAdapter(options);
        adapter.setActivity(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        noQuestion = view.findViewById(R.id.no_question_text);
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    noQuestion.setText("There are no questions. Click the '+' button to add a question." );
                }
            }
        });

        addQuestionsBtn = view.findViewById(R.id.add_questions_btn);
        addQuestionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewQuestionsFragment fragment = AddNewQuestionsFragment.getInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment).addToBackStack(null).commit();
                fragment.setReference(collection);
            }
        });

        return view;
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Level 1 Questions");
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