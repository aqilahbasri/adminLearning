package com.example.adminlearning;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GIFAdapter extends RecyclerView.Adapter<com.example.adminlearning.GIFAdapter.GIFViewHolder> {

    private List<com.example.adminlearning.GIF> gifList;
    private FirebaseAuth mAuth;
    private DatabaseReference GIFRef, favlistRef;
    private String userID;
    Context t;

    public GIFAdapter(Context t, List<com.example.adminlearning.GIF> gifList){
        this.t = t;
        this.gifList = gifList;
    }

    public class GIFViewHolder extends RecyclerView.ViewHolder {
        public TextView engCaption, malayCaption;
        public WebView gifPicture;
        public CardView cardView;
        public ImageButton unfavbtn;


        public GIFViewHolder(@NonNull View itemView)
        {
            super(itemView);


            GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");
            favlistRef = FirebaseDatabase.getInstance().getReference("FavouriteGIF");

            gifPicture = (WebView) itemView.findViewById(R.id.gifPicture);
            engCaption = (TextView) itemView.findViewById(R.id.engCaption);
            malayCaption = (TextView) itemView.findViewById(R.id.malayCaption);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);



        }

    }

    @NonNull
    @Override
    public GIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gif, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new GIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GIFViewHolder gifViewHolder, final int i) {

        gifViewHolder.gifPicture.loadUrl(gifList.get(i).getGifPicture());
        gifViewHolder.gifPicture.getSettings().setLoadWithOverviewMode(true);
        gifViewHolder.gifPicture.getSettings().setUseWideViewPort(true);
        gifViewHolder.engCaption.setText(gifList.get(i).getEngCaption());
        gifViewHolder.malayCaption.setText(gifList.get(i).getMalayCaption());

        gifViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            String gif = gifList.get(i).getGifPicture();
            String engCaption = gifList.get(i).getEngCaption();
            String malayCaption = gifList.get(i).getMalayCaption();
            String category = gifList.get(i).getCategory();
            //
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(t, GIFDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("gifurl", gif);
                intent.putExtra("engCaption", engCaption);
                intent.putExtra("malayCaption", malayCaption);
                intent.putExtra("category", category);
                intent.putExtra("type", "GIF");

                v.getContext().startActivity(intent);



//                final Dialog gifDialog = new Dialog(t);
//                gifDialog.setContentView(R.layout.enlarge_gif);
//                WebView wb = (WebView) gifDialog.findViewById(R.id.bigGif);
//                ImageView sharebtn = (ImageView) gifDialog.findViewById(R.id.sharebtn);
//
//                wb.loadUrl(gif);
//                wb.getSettings().setLoadWithOverviewMode(true);
//                wb.getSettings().setUseWideViewPort(true);
//
//                sharebtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(t, ContactsToSendGIf.class);
//                        intent.putExtra("gifurl", gif);
//                        t.startActivity(intent);
//                    }
//                });
//
//
//                gifDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                gifDialog.show();
//
            }

        });
    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }

}
