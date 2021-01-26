package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewTestLevelFragment extends Fragment {

    private static final String TAG = "ViewTestLevelFragment";
    private TextView levelNameTxt, durationTxt, overallMarkTxt;
    private Button addSectionBtn, editBtn, deleteBtn;
    private ImageView levelIcon;

    private RecyclerView recyclerView;
    private ListenerRegistration registration;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    private TestSectionSettingsAdapter adapter;
    private String key;

    public ViewTestLevelFragment(String key) {
        this.key = key;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_test_level, container, false);

        levelNameTxt = view.findViewById(R.id.textView1);
        durationTxt = view.findViewById(R.id.textView2);
        overallMarkTxt = view.findViewById(R.id.textView3);

        addSectionBtn = view.findViewById(R.id.addSection_button);
        editBtn = view.findViewById(R.id.editLevel_button);
        deleteBtn = view.findViewById(R.id.delete_button);
        levelIcon = view.findViewById(R.id.level_icon);

        recyclerView = view.findViewById(R.id.list_item);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        addSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddSectionLevelFragment(key)).addToBackStack(null).commit();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditTestLevelFragment(key)).addToBackStack(null).commit();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        getInfoFromDatabase();

        return view;
    }

    //Get initial info from db
    private void getInfoFromDatabase() {

        docRef = db.collection("AssessmentLevel").document(key);

        registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String levelName = value.get("levelName").toString();
                long duration = Long.parseLong(value.get("duration").toString());
                String overallPassingMark = value.get("overallPassingMark").toString();
                String iconUrl = value.get("levelIconUrl").toString();

                String durationStr = String.format(
                        Locale.getDefault(), "%02d hr, %02d mins",
                        TimeUnit.MILLISECONDS.toHours(duration),
                        TimeUnit.MILLISECONDS.toMinutes(duration) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)
                                ));

                levelNameTxt.setText(levelName);
                overallMarkTxt.setText(overallPassingMark);
                durationTxt.setText(durationStr);
                Glide.with(getContext()).load(iconUrl).into(levelIcon);
            }
        });

        //To display sections
        CollectionReference collection = docRef.collection("Sections");

        FirestoreRecyclerOptions<TestSectionSettingsModel> options = new FirestoreRecyclerOptions.Builder<TestSectionSettingsModel>()
                .setQuery(collection, TestSectionSettingsModel.class).build();

        adapter = new TestSectionSettingsAdapter(options);
        adapter.setActivity(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.CustomMaterialDialog);
        dialog.setTitle("Confirm deletion level");
        dialog.setMessage("All test questions associated with this level will also be deleted.");

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onStop();
                DocumentReference reference = db.collection("AssessmentLevel").document(key);
                reference.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new DeleteLevelSuccessFragment()).commit();
                                }
                            }
                        });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("View Test Details");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        registration.remove();
        adapter.stopListening();
    }

}