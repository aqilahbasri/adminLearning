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

import java.util.ArrayList;

public class EditSL extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit_s_l);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");
        childdata = extras.getString("childTitle");

        mStorageRef = FirebaseStorage.getInstance().getReference("LearningCategory/");
        dbref = FirebaseDatabase.getInstance().getReference("SignLanguage").child(data);
        databaseReference = FirebaseDatabase.getInstance().getReference("SignLanguage").child(data).child(childdata);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        description = dataSnapshot.child("sldescription").getValue().toString();
                       slimage = dataSnapshot.child("imgurl").getValue().toString();
                       loadData();

                databaseReference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditSL.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        slimg = (WebView) findViewById(R.id.slimg);
        updateslbtn = (Button) findViewById(R.id.updateslbtn);
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

        if(!sldescription.equals("")) {
            if (imguri != null) {
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
                                        UploadSL imageUploadInfo = new UploadSL(url, sldescription);
//                                        String categorykey = databaseReference.push().getKey();
                                        dbref.child(sldescription).setValue(imageUploadInfo);
                                        String oldsldesc = description;
                                        dbref.child(oldsldesc).removeValue();

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Sign language GIF edited successfully!", Toast.LENGTH_LONG).show();


                            }
                        });
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplication(), listLearnSLactivity.class);
                        intent.putExtra("catTitle", data);
                        startActivity(intent);
                    }
                }, 3000);
            } else if (imguri == null) {
                String oldimgsl = slimage;
                UploadSL imageUploadInfo = new UploadSL(oldimgsl, sldescription);
                dbref.child(sldescription).setValue(imageUploadInfo);

                String oldsldesc = description;
                dbref.child(oldsldesc).removeValue();

                Toast.makeText(getApplicationContext(), "Sign language GIF edited successfully!", Toast.LENGTH_LONG).show();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplication(), listLearnSLactivity.class);
                        intent.putExtra("catTitle", data);
                        startActivity(intent);
                    }
                }, 3000);

            }
        }else{
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
                                    UploadSL imageUploadInfo = new UploadSL(url, oldsldesc);
                                    dbref.child(oldsldesc).setValue(imageUploadInfo);


                                }
                            });
                            Toast.makeText(getApplicationContext(), "Sign language GIF edited successfully!", Toast.LENGTH_LONG).show();

                        }
                    });
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplication(), listLearnSLactivity.class);
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
}