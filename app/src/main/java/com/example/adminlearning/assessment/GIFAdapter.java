package com.example.adminlearning.assessment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GIFAdapter extends RecyclerView.Adapter<GIFAdapter.GIFViewHolder> {

    private static final String TAG = "GIFAdapter";
    private static GIFAdapter uniqueInstance = new GIFAdapter();
    private List<GIF> gifList;
    private FirebaseAuth mAuth;
    private DatabaseReference GIFRef;
    private String userID;
    String gifUrl, caption, engCaption;
    Context t;

    private GIFAdapter() {}

    public static GIFAdapter getInstance() {
        return uniqueInstance;
    }

    public class GIFViewHolder extends RecyclerView.ViewHolder {
        public TextView engCaption, malayCaption;
        public WebView gifPicture;
        public CardView cardView;

        public GIFViewHolder(@NonNull View itemView) {
            super(itemView);

//            mAuth = FirebaseAuth.getInstance();
//            userID = mAuth.getCurrentUser().getUid();
            GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");

            gifPicture = itemView.findViewById(R.id.gifPicture);
            engCaption = itemView.findViewById(R.id.engCaption);
            malayCaption = itemView.findViewById(R.id.malayCaption);
            cardView = itemView.findViewById(R.id.cardview_id);
        }

    }

    @NonNull
    @Override
    public GIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_layout_gif, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new GIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GIFViewHolder gifViewHolder, final int i) {

        gifViewHolder.gifPicture.loadUrl(gifList.get(i).getGifPicture());
        gifViewHolder.gifPicture.getSettings().setLoadWithOverviewMode(true);
        gifViewHolder.gifPicture.getSettings().setUseWideViewPort(true);
        gifViewHolder.engCaption.setText(gifList.get(i).getEngCaption());
        gifViewHolder.malayCaption.setText(gifList.get(i).getMalayCaption());

        //handle selection?
        gifViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifUrl = gifList.get(i).getGifPicture();
                setGifUrl(gifUrl);
                setMalayCaption(gifList.get(i).getMalayCaption());
                setEngCaption(gifList.get(i).getEngCaption());
                Log.e(TAG, "You clicked: "+gifUrl);
            }
        });

    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getMalayCaption() {
        return caption;
    }

    public void setMalayCaption(String caption) {
        this.caption = caption;
    }

    public String getEngCaption() {
        return engCaption;
    }

    public void setEngCaption(String engCaption) {
        this.engCaption = engCaption;
    }

    public void setContext(Context t) {
        this.t = t;
    }

    public Context getContext() {
        return t;
    }

    public void setGifList(List<GIF> gifList) {
        this.gifList = gifList;
    }

}