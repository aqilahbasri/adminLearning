package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
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

public class EditCategory extends AppCompatActivity {

    String data, childdata, sldescription;
    Button uploadimgbtn, updateslbtn;
    TextInputEditText addsldesc;
    ImageView delbtn;
    WebView slimg;
    public Uri imguri;
    DatabaseReference databaseReference, dbref;
    StorageReference mStorageRef;
    String slimage, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");
        childdata = extras.getString("childTitle");

        mStorageRef = FirebaseStorage.getInstance().getReference("LearningCategory/");
        dbref = FirebaseDatabase.getInstance().getReference("LEARNING");
        databaseReference = FirebaseDatabase.getInstance().getReference("LEARNING").child(data);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description = dataSnapshot.child("categoryname").getValue().toString();
                slimage = dataSnapshot.child("categoryimage").getValue().toString();
                loadData();

                databaseReference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditCategory.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        slimg = (WebView) findViewById(R.id.slimg);
        updateslbtn = (Button) findViewById(R.id.updatecatbtn);
        uploadimgbtn = (Button) findViewById(R.id.uploadimgbtn);
        addsldesc = (TextInputEditText) findViewById(R.id.addsldesc);


        uploadimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        updateslbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
            }
        });
    }

    private void loadData() {
        slimg.loadUrl(slimage);
        slimg.getSettings().setLoadWithOverviewMode(true);
        slimg.getSettings().setUseWideViewPort(true);
        addsldesc.setHint(description);

    }
    private String getExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    //upload photo and sign language description
    private void Update() {
        sldescription = addsldesc.getText().toString();

        if (!sldescription.equals("")) { //change description
            if (imguri != null) { //change description and gif
                final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
                Ref.putFile(imguri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        Category imageUploadInfo = new Category(sldescription, url);
//                                        String categorykey = databaseReference.push().getKey();
                                        dbref.child(sldescription).setValue(imageUploadInfo);
                                        String oldsldesc = description;
                                        dbref.child(oldsldesc).removeValue();

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Sign language category edited successfully!", Toast.LENGTH_LONG).show();


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
            } else if (imguri == null) { //change description but not change gif
                String oldimgsl = slimage;
                Category imageUploadInfo = new Category(sldescription, oldimgsl);
                dbref.child(sldescription).setValue(imageUploadInfo);

                String oldsldesc = description;
                dbref.child(oldsldesc).removeValue();

                Toast.makeText(getApplicationContext(), "Sign language category edited successfully!", Toast.LENGTH_LONG).show();

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
        }
//        if(sldescription.equals("") && imguri != null){
//            Toast.makeText(getApplicationContext(), "Nothing was edited!", Toast.LENGTH_LONG).show();
//
//        }
        else{
            if(imguri==null){
                Toast.makeText(getApplicationContext(), "Nothing was edited!", Toast.LENGTH_LONG).show();
            }else
            {
                final String oldsldesc = description;
                final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
                Ref.putFile(imguri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        Category imageUploadInfo = new Category(oldsldesc, url);
                                        dbref.child(oldsldesc).setValue(imageUploadInfo);


                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Sign language category edited successfully!", Toast.LENGTH_LONG).show();

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
                }, 3000);}

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
            Toast.makeText(getApplicationContext(), "Upload successfully!", Toast.LENGTH_LONG).show();


        }
    }


}