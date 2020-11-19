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

public class AddSLactivity extends AppCompatActivity {

    Button uploadimgbtn, submitslbtn;
    TextInputEditText addsldesc;
    ImageView delbtn;
    WebView slimg;
    public Uri imguri;
    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_s_lactivity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");


        mStorageRef = FirebaseStorage.getInstance().getReference("LearningCategory/");
        databaseReference = FirebaseDatabase.getInstance().getReference("SignLanguage").child(data);

        slimg = (WebView) findViewById(R.id.slimg);
        submitslbtn = (Button) findViewById(R.id.submitslbtn);
        uploadimgbtn = (Button) findViewById(R.id.uploadimgbtn);
        addsldesc = (TextInputEditText) findViewById(R.id.addsldesc);


        uploadimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        submitslbtn.setOnClickListener(new View.OnClickListener() {
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

    //upload photo and sign language description
    private void Fileuploader() {
        if (imguri != null) {

            final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
            Ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String sldescription = addsldesc.getText().toString().trim();
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String url = uri.toString();
                                    UploadSL imageUploadInfo = new UploadSL(url, sldescription);
//                                    String categorykey = databaseReference.push().getKey();
                                    databaseReference.child(sldescription).setValue(imageUploadInfo);

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Sign language Added successfully!", Toast.LENGTH_LONG).show();

                        }
                    });
        } else {

            Toast.makeText(AddSLactivity.this, "Please Select sign language Image or Add sign language description", Toast.LENGTH_LONG).show();

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