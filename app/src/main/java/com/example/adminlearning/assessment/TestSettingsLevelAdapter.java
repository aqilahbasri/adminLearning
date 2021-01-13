package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.adminlearning.R;
import com.squareup.picasso.Picasso;

public class TestSettingsLevelAdapter extends FirestoreRecyclerAdapter<TestSettingsLevelModel, TestSettingsLevelAdapter.MyViewHolder> {

    Activity activity;
    private static final String TAG = "TestSettingsLevelAdapter";

    public TestSettingsLevelAdapter(@NonNull FirestoreRecyclerOptions<TestSettingsLevelModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_test_settings, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final TestSettingsLevelModel model) {

        holder.levelName.setText(model.getLevelName());
        String iconUrl = model.getLevelIconUrl();
        Picasso.get().load(iconUrl).into(holder.levelIcon);

        holder.levelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof ManageQuestionsActivity) {
                    ((ManageQuestionsActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new TestQuestionsSectionFragment(getSnapshots().getSnapshot(position)
                                    .getId())).addToBackStack(null).commit();
                }

                else if (getActivity() instanceof ManageTestSettingsActivity) {
                    holder.levelCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ManageTestSettingsActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new ViewTestLevelFragment(getSnapshots().getSnapshot(position)
                                            .getId())).addToBackStack(null).commit();
                        }
                    });
                }

            }
        });

    }

    protected void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView levelCardView;
        TextView levelName;
        ImageView levelIcon;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            levelCardView = itemView.findViewById(R.id.level_cardView);
            levelName = itemView.findViewById(R.id.level_name);
            levelIcon = itemView.findViewById(R.id.level_icon);
        }
    }

}