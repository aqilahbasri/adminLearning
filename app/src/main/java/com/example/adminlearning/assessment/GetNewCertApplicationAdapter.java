package com.example.adminlearning.assessment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GetNewCertApplicationAdapter extends RecyclerView.Adapter<GetNewCertApplicationAdapter.MyViewHolder> {

    ArrayList<ApplyCertContactInfo> newApplicationList;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private String key;
    private Activity activity;

    GetNewCertApplicationAdapter(Activity activity, ArrayList<ApplyCertContactInfo> newApplicationList, String key) {
        this.activity = activity;
        this.newApplicationList = newApplicationList;
        this.key = key;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_layout_view_new_cert, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(newApplicationList.get(position).getName());
        holder.phoneNumber.setText(newApplicationList.get(position).getPhoneNumber());
        holder.appliedDate.setText(newApplicationList.get(position).getTimeDate());

        holder.viewReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewPdf viewPdf = new ViewPdf(context);
//                final WebView webView = new WebView(activity);
//                webView.getSettings().setJavaScriptEnabled(true);
//                activity.setContentView(webView);

                StorageReference storageReference = firebaseStorage.getReference()
                        .child("CertApplication_StudentInfo").child("NewApplication")
                        .child(key);

                storageReference.child(key+"_PaymentReceipt").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("file", "url: "+uri);
//                        webView.loadUrl(uri.toString());
                        //TODO: Edit this part
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(intent);
                        }
                    }
                });
            } //end void onclick
        }); //endonclicklistener

        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: try to get key? settle later. now get name first
                ApplyCertificateReviewDialog applyCertificateReviewDialog = new ApplyCertificateReviewDialog(key, newApplicationList.get(position).getName());
                applyCertificateReviewDialog.show(((ManageCertificationActivity) activity)
                        .getSupportFragmentManager(), "ApplyCertDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return newApplicationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phoneNumber;
        TextView appliedDate;
        Button viewReceipt;
        Button reviewButton;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView1);
            phoneNumber = itemView.findViewById(R.id.textView2);
            appliedDate = itemView.findViewById(R.id.textView3);
            viewReceipt = itemView.findViewById(R.id.receipt_button);
            reviewButton = itemView.findViewById(R.id.review_button);
        }
    }
}