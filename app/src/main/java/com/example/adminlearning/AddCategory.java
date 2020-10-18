package com.example.adminlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddCategory extends AppCompatActivity {

    Button uploadimgbtn, submitcatbtn;
    TextInputEditText addcategoryname;
    ImageView catimg;
    public Uri imguri;
    DatabaseReference databaseReference;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        mStorageRef = FirebaseStorage.getInstance().getReference("LearningCategory/");
        databaseReference = FirebaseDatabase.getInstance().getReference("LEARNING");

        catimg = (ImageView) findViewById(R.id.catimg);
        submitcatbtn = (Button) findViewById(R.id.submitcatbtn);
        uploadimgbtn = (Button) findViewById(R.id.uploadimgbtn);
        addcategoryname = (TextInputEditText) findViewById(R.id.addcategoryname);

        uploadimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        submitcatbtn.setOnClickListener(new View.OnClickListener() {
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

    //upload photo and category name
    private void Fileuploader() {
        if (imguri != null) {

            final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + getExtention(imguri));
            Ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String categoryname = addcategoryname.getText().toString().trim();
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String url = uri.toString();
                                    UploadCategory imageUploadInfo = new UploadCategory(url, categoryname);
                                    String categoryimage = databaseReference.push().getKey();
                                    databaseReference.child(categoryimage).setValue(imageUploadInfo);

                                }
                            });
                            Toast.makeText(getApplicationContext(), "Category Added successfully!", Toast.LENGTH_LONG).show();

                        }
                    });
        } else {

            Toast.makeText(AddCategory.this, "Please Select Image or Add Category Name", Toast.LENGTH_LONG).show();

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
            catimg.setImageURI(imguri);

        }
    }
}

