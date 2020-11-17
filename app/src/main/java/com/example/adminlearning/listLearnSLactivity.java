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

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listLearnSLactivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView texttoolbar;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<LearnSLlist> learnSLlists;
    LearnSLadapter adapter;
    Button addslbtn;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_learn_s_lactivity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        texttoolbar = (TextView) findViewById(R.id.texttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data);
        addslbtn = (Button)findViewById(R.id.addslbtn);
;
        addslbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewSL();
            }

        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        reference = FirebaseDatabase.getInstance().getReference().child("SignLanguage").child(data);

    }

    public void onStart() {

        super.onStart();

        reference.orderByChild("sldescription").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    learnSLlists = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        LearnSLlist p = dataSnapshot1.getValue(LearnSLlist.class);
                        learnSLlists.add(p);
                    }

                    adapter = new LearnSLadapter(listLearnSLactivity.this, learnSLlists, data);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(listLearnSLactivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addnewSL() {
        Intent intent = new Intent(this, AddSLactivity.class );
        startActivity(intent);
    }
}