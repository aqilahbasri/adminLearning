package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class PendingLibraryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView actionBarTitle;

    private RecyclerView myGIFList, myGIFRecommendationList;
    private SearchView searchView;
    private TextView noResult;
    private ImageButton voiceButton;
    private DatabaseReference GIFRef;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;


    ArrayList<PendingGIF> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_library);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarTitle = findViewById(R.id.toolbar_title);
        actionBarTitle.setText("Pending Library");

        myGIFList = (RecyclerView) findViewById(R.id.gif_list);
        searchView = (SearchView) findViewById(R.id.search_bar);
        noResult = (TextView) findViewById(R.id.no_result);
        voiceButton = (ImageButton) findViewById(R.id.voice_button);
        GIFRef = FirebaseDatabase.getInstance().getReference("PendingGIF");

    }

    @Override
    public void onStart() {

        super.onStart();

        if(GIFRef != null){
            GIFRef.orderByChild("malayCaption").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(PendingGIF.class));
                        }

                        PendingGIFAdapter gifAdapter = new PendingGIFAdapter(getApplicationContext(),list);
                        myGIFList.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                        myGIFList.setAdapter(gifAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            if (matches != null) {
                if (matches.size() > 0) {
                    searchView.setQuery(matches.get(0), false);
                }
            }
        }
    }

    private void search(String s) {
        ArrayList<PendingGIF> myList = new ArrayList<>();
        ArrayList<PendingGIF> wordList = new ArrayList<>();
        ArrayList<PendingGIF> categoryList = new ArrayList<>();
//        boolean resultExist = false;

        myGIFList.setVisibility(View.VISIBLE);
//        myGIFRecommendationList.setVisibility(View.INVISIBLE);
        noResult.setVisibility(View.INVISIBLE);

        for(PendingGIF gif : list){
            if(gif.getEngCaption().toLowerCase().contains(s.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(s.toLowerCase())){
                myList.add(gif);
//                resultExist = true;
            }

        }

        if(myList.isEmpty()){
            myGIFList.setVisibility(View.INVISIBLE);
            noResult.setVisibility(View.VISIBLE);

        }

        else{
            PendingGIFAdapter gifAdapter = new PendingGIFAdapter(getApplicationContext(),myList);
            myGIFList.setAdapter(gifAdapter);
        }

    }
}