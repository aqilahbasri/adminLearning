package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TextView actionBarTitle;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;


    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Category> list;
    MyAdapter adapter;
    Button addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String TAG = "MainActivity";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        addbtn = (Button) findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcategory();
            }

        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("Learning");
        ImageButton rightMenu = findViewById(R.id.ic_right_menu);
        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.nav_view);

//        new InitNavDrawerHeader(navigationView, TAG);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        goToMainCommunicationModule();
                        break;
                    case R.id.nav_learning:
                        goToMainLearningModule();
                        break;
                    case R.id.nav_assessment:
                        goToMainAssessmentModule();
                        break;
                }
                drawer.closeDrawer(GravityCompat.END);
                return true;
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home: //nanti tukar Fatin punya
                goToMainCommunicationModule();
//                selectedFragment = new ChatsFragment();
                break;
            case R.id.nav_learning:
                goToMainLearningModule();
//                selectedFragment = new CategoryFragment();
                break;
            case R.id.nav_assessment:
                goToMainAssessmentModule();
//                selectedFragment = new AssessmentHomeFragment();
                break;
//            case R.id.nav_administration:
//                selectedFragment = new AdministrationMenu_Fragment();
//                break;
        }
//
////        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack(null).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToMainLearningModule() {
        Intent mainLearningModule = new Intent(com.example.adminlearning.MainActivity.this, MainActivity.class);
        startActivity(mainLearningModule);
    }

    private void goToMainAssessmentModule() {
        Intent mainAssessmentModule = new Intent(com.example.adminlearning.MainActivity.this, com.example.adminlearning.assessment.MainActivity.class);
        startActivity(mainAssessmentModule);
    }

    private void goToMainCommunicationModule() {
        Intent mainCommunicationModule = new Intent(com.example.adminlearning.MainActivity.this, MainCommunicationActivity.class);
        startActivity(mainCommunicationModule);
    }
}
