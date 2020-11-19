package com.example.adminlearning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddQactivity extends AppCompatActivity {

    Button uploadimgbtn, submitqbtn;
    TextInputEditText addsldesc, option1, option2, option3, option4, correctAnswer;
    ImageView delbtn;
    WebView slimg;
    public Uri imguri;
    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    String data;
    int correctanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qactivity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");


        mStorageRef = FirebaseStorage.getInstance().getReference("LearningModule/").child(data);
        databaseReference = FirebaseDatabase.getInstance().getReference("Question").child(data);

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
                Fileuploader();
            }
        });
    }

    private String getExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    //upload photo and question
    private void Fileuploader() {
        if (imguri != null) {

            final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
            Ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String sldescription = addsldesc.getText().toString().trim();
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
                                    ChallengeSLlist imageUploadInfo = new ChallengeSLlist(url, correctanswer, optionA, optionB, optionC, optionD, sldescription);
//                                    String categorykey = databaseReference.push().getKey();
                                    databaseReference.child(sldescription).setValue(imageUploadInfo);

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Question Added successfully!", Toast.LENGTH_LONG).show();

                        }
                    });
        } else {

            Toast.makeText(AddQactivity.this, "Please Select sign language Image or don't leave any empty blank", Toast.LENGTH_LONG).show();

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
}