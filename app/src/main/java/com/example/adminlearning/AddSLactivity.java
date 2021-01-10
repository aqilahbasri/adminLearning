package com.example.adminlearning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class AddSLactivity extends AppCompatActivity {

    Button uploadimgbtn, submitslbtn;
    TextInputEditText addsldesc;
    ImageView delbtn;
    WebView slimg;
    VideoView videoView;
    public Uri imguri;
    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    String data ,sldescription;
    String checker="";
    String downloadurl;

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
        sldescription = addsldesc.getText().toString();
        if (imguri != null) {
            if(sldescription.equals("") ){
                Toast.makeText(AddSLactivity.this, "Please add sign language description!", Toast.LENGTH_LONG).show();
            }
            else{


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
                            Toast.makeText(getApplicationContext(), "Sign language added successfully!", Toast.LENGTH_LONG).show();

                        }
                    });
             final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.putExtra("catTitle", data);
                    startActivity(intent);
                }
            }, 3000);
            }
        } else {

            Toast.makeText(AddSLactivity.this, "Please select sign language material and/or add sign language description", Toast.LENGTH_LONG).show();

        }
    }

    //choose category photo
    private void Filechooser(){

        CharSequence options[] = new CharSequence[]{"Images/GIF", "Video"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the File");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    checker = "image/gif";
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);

                }
                if(which == 1){
                    checker = "video";
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("video/*");
                    startActivityForResult(galleryIntent, 2);

                }
            }
        });
        builder.show();


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
        if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();

            final StorageReference Ref = mStorageRef.child("upload/" + System.currentTimeMillis()+ getExtention(imguri));
            Ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadurl = uri.toString();
                                    slimg.loadUrl(downloadurl);
                                    slimg.getSettings().setLoadWithOverviewMode(true);
                                    slimg.getSettings().setUseWideViewPort(true);



                                }
                            });
//                            Toast.makeText(getApplicationContext(), "upload successfully!", Toast.LENGTH_LONG).show();


                        }
                    });

        }
    }
}