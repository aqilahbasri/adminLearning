package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListChallengeSLactivity extends AppCompatActivity {

    private Toolbar toolbar;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<ChallengeSLlist> challengeSLlist;
    ChallengeSLadapter adapter;
    Button addquesbtn;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_challenge_s_lactivity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data);
        addquesbtn = (Button)findViewById(R.id.addquestionbtn);

        addquesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewQ();
            }

        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        reference = FirebaseDatabase.getInstance().getReference().child("Question").child(data);
    }

    public void onStart() {

        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    challengeSLlist = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        ChallengeSLlist p = dataSnapshot1.getValue(ChallengeSLlist.class);
                        challengeSLlist.add(p);
                    }

                    adapter = new ChallengeSLadapter(ListChallengeSLactivity.this, challengeSLlist, data);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListChallengeSLactivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addnewQ() {
        Intent intent = new Intent(this, AddQactivity.class );
        intent.putExtra("catTitle", data);
        startActivity(intent);
    }
}