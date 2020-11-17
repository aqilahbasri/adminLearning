package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Category> list;
    MyAdapter adapter;
    Button addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbtn = (Button) findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcategory();
            }

        });



        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        reference = FirebaseDatabase.getInstance().getReference().child("LEARNING");


    }
    public void onStart() {

        super.onStart();

        reference.orderByChild("categoryname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        Category p = dataSnapshot1.getValue(Category.class);
                        list.add(p);
                    }
                    adapter = new MyAdapter(MainActivity.this, list);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void addcategory(){
        Intent intent = new Intent(this, AddCategory.class );
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {

    }
}
