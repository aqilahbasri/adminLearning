package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditQuestion extends AppCompatActivity {

    Button uploadimgbtn, submitqbtn;
    TextInputEditText addsldesc, option1, option2, option3, option4, correctAnswer;
    ImageView delbtn;
    WebView slimg;
    public Uri imguri;
    DatabaseReference databaseReference, dbref;
    StorageReference mStorageRef;
    String data, childdata, desc;
    String description, cortans,opt1,opt2,opt3,opt4,ques;
    int correctanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");
        childdata = extras.getString("childTitle");

        mStorageRef = FirebaseStorage.getInstance().getReference("LearningModule/").child(data);
        dbref = FirebaseDatabase.getInstance().getReference("Question").child(data);
        databaseReference = FirebaseDatabase.getInstance().getReference("Question").child(data).child(childdata);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description = dataSnapshot.child("desc").getValue().toString();
                cortans = dataSnapshot.child("correctAnswer").getValue().toString();
                opt1 = dataSnapshot.child("option1").getValue().toString();
                opt2 = dataSnapshot.child("option2").getValue().toString();
                opt3 = dataSnapshot.child("option3").getValue().toString();
                opt4 = dataSnapshot.child("option4").getValue().toString();
                ques = dataSnapshot.child("question").getValue().toString();


                loadData();

                databaseReference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditQuestion.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        slimg = (WebView) findViewById(R.id.slimg);
        submitqbtn = (Button) findViewById(R.id.submitqbtn);
        uploadimgbtn = (Button) findViewById(R.id.uploadimgbtn);
        addsldesc = (TextInputEditText) findViewById(R.id.addsldesc);
        option1 = (TextInputEditText) findViewById(R.id.option1);
        option2 = (TextInputEditText) findViewById(R.id.option2);
        option3 = (TextInputEditText) findViewById(R.id.option3);
        option4 = (TextInputEditText) findViewById(R.id.option4);
        correctAnswer = (TextInputEditText) findViewById(R.id.correctAnswer);



        uploadimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        submitqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
            }
        });


    }

    private String getExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    //upload photo and question
    private void Update() {
        desc = addsldesc.getText().toString();

        if(!desc.equals("")){
            if (imguri != null) {

                final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
                Ref.putFile(imguri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final String optionA = option1.getText().toString().trim();
                                final String optionB = option2.getText().toString().trim();
                                final String optionC = option3.getText().toString().trim();
                                final String optionD = option4.getText().toString().trim();
                                final String correctAns = correctAnswer.getText().toString().trim();

                                if(correctAns.equals("A")){
                                    correctanswer = 1;
                                }else if(correctAns.equals("B")){
                                    correctanswer = 2;
                                }else if(correctAns.equals("C")){
                                    correctanswer = 3;
                                }else{
                                    correctanswer = 4;
                                }

                                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String url = uri.toString();
                                        ChallengeSLlist imageUploadInfo = new ChallengeSLlist(url, correctanswer, optionA, optionB, optionC, optionD, desc);
                                        dbref.child(desc).setValue(imageUploadInfo);

                                        String oldsldesc = description;
                                        dbref.child(oldsldesc).removeValue();

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Question edited successfully!", Toast.LENGTH_LONG).show();

                            }
                        });
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplication(), ListChallengeSLactivity.class);
                        intent.putExtra("catTitle", data);
                        startActivity(intent);
                    }
                }, 3000);

        }else if(imguri == null){
                final String optionA = option1.getText().toString().trim();
                final String optionB = option2.getText().toString().trim();
                final String optionC = option3.getText().toString().trim();
                final String optionD = option4.getText().toString().trim();
                final String correctAns = correctAnswer.getText().toString().trim();
                String oldimgsl = ques;
                if(correctAns.equals("A")){
                    correctanswer = 1;
                }else if(correctAns.equals("B")){
                    correctanswer = 2;
                }else if(correctAns.equals("C")){
                    correctanswer = 3;
                }else{
                    correctanswer = 4;
                }
                ChallengeSLlist imageUploadInfo = new ChallengeSLlist(oldimgsl, correctanswer, optionA, optionB, optionC, optionD, desc);
                dbref.child(desc).setValue(imageUploadInfo);

                String oldsldesc = description;
                dbref.child(oldsldesc).removeValue();

                Toast.makeText(getApplicationContext(), "Question edited successfully!", Toast.LENGTH_LONG).show();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplication(), ListChallengeSLactivity.class);
                        intent.putExtra("catTitle", data);
                        startActivity(intent);
                    }
                }, 3000);
            }


        } else {
            final String oldsldesc = description;
            final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
            Ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String optionA = option1.getText().toString().trim();
                            final String optionB = option2.getText().toString().trim();
                            final String optionC = option3.getText().toString().trim();
                            final String optionD = option4.getText().toString().trim();
                            final String correctAns = correctAnswer.getText().toString().trim();

                            if(correctAns.equals("A")){
                                correctanswer = 1;
                            }else if(correctAns.equals("B")){
                                correctanswer = 2;
                            }else if(correctAns.equals("C")){
                                correctanswer = 3;
                            }else{
                                correctanswer = 4;
                            }

                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String url = uri.toString();
                                    ChallengeSLlist imageUploadInfo = new ChallengeSLlist(url, correctanswer, optionA, optionB, optionC, optionD, oldsldesc);
                                    dbref.child(oldsldesc).setValue(imageUploadInfo);

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Question edited successfully!", Toast.LENGTH_LONG).show();


                        }
                    });
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplication(), ListChallengeSLactivity.class);
                    intent.putExtra("catTitle", data);
                    startActivity(intent);
                }
            }, 3000);


        }
    }

    //choose category photo
    private void Filechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();
            slimg.loadUrl(String.valueOf(imguri));
            slimg.getSettings().setLoadWithOverviewMode(true);
            slimg.getSettings().setUseWideViewPort(true);


        }
    }

    private void loadData() {
        slimg.loadUrl(ques);
        slimg.getSettings().setLoadWithOverviewMode(true);
        slimg.getSettings().setUseWideViewPort(true);
        addsldesc.setHint(description);
        option1.setText(opt1);
        option2.setText(opt2);
        option3.setText(opt3);
        option4.setText(opt4);
        if(cortans.equals("1")){
            correctAnswer.setText("A");
        }else if(cortans.equals("2")){
            correctAnswer.setText("B");
        }else if(cortans.equals("3")){
            correctAnswer.setText("C");
        }else{
            correctAnswer.setText("D");
        }

    }

}