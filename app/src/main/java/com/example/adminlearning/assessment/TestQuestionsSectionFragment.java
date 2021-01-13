package com.example.adminlearning.assessment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.example.adminlearning.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestQuestionsSectionFragment extends Fragment {

    private static final String TAG = "ManageQuestionsFgmt";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ViewSectionsAdapter adapter;
    private String key;

    public TestQuestionsSectionFragment(String key) {
        this.key = key;
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

        CollectionReference reference = db.collection("AssessmentLevel")
                .document(key).collection("Sections");

        FirestoreRecyclerOptions<TestSectionSettingsModel> options = new FirestoreRecyclerOptions.Builder<TestSectionSettingsModel>()
                .setQuery(reference, TestSectionSettingsModel.class).build();

        adapter = new ViewSectionsAdapter(options);
        adapter.setActivity(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Select Section");
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

class ViewSectionsAdapter extends FirestoreRecyclerAdapter<TestSectionSettingsModel, ViewSectionsAdapter.MyViewHolder> {

    Activity activity;
    private static final String TAG = "ViewSectionsAdapter";
    private CollectionReference reference;

    public ViewSectionsAdapter(@NonNull FirestoreRecyclerOptions<TestSectionSettingsModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_question_sections, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final TestSectionSettingsModel model) {

        holder.sectionNameTxt.setText(model.getSectionName());

        holder.sectionNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageQuestionsActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Level1QuestionsFragment
                                (getSnapshots().getSnapshot(position).getReference())).addToBackStack(null).commit();
                Log.e(TAG, "reference: "+getSnapshots().getSnapshot(position).getReference().getPath());
            }
        });

    }

    protected void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }

    public CollectionReference getReference() {
        return reference;
    }

    public void setReference(CollectionReference reference) {
        this.reference = reference;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sectionNameTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionNameTxt = itemView.findViewById(R.id.section_name);
        }
    }
}