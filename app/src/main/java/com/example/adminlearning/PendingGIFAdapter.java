package com.example.adminlearning;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingGIFAdapter extends RecyclerView.Adapter<PendingGIFAdapter.PendingGIFViewHolder> {

    private List<PendingGIF> gifList;
    private FirebaseAuth mAuth;
    private String userID;
    Context t;

    public PendingGIFAdapter(Context t, List<PendingGIF> gifList)
    {
        this.t = t;
        this.gifList = gifList;
    }

    public class PendingGIFViewHolder extends RecyclerView.ViewHolder
    {
        public TextView engCaption, malayCaption;
        public WebView gifPicture;
        public CardView cardView;
        public ImageButton unfavbtn;
        boolean favcheker = false;


        public PendingGIFViewHolder(@NonNull View itemView)
        {
            super(itemView);

            gifPicture = (WebView) itemView.findViewById(R.id.gifPicture);
            engCaption = (TextView) itemView.findViewById(R.id.engCaption);
            malayCaption = (TextView) itemView.findViewById(R.id.malayCaption);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }

    }

    public PendingGIFViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gif, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();

        return new PendingGIFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingGIFViewHolder gifViewHolder, final int position) {

        gifViewHolder.gifPicture.loadUrl(gifList.get(position).getImageUrl());
        gifViewHolder.gifPicture.getSettings().setLoadWithOverviewMode(true);
        gifViewHolder.gifPicture.getSettings().setUseWideViewPort(true);
        gifViewHolder.gifPicture.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        gifViewHolder.engCaption.setText(gifList.get(position).getEngCaption());
        gifViewHolder.malayCaption.setText(gifList.get(position).getMalayCaption());

        //click to enlarge gif n send
        gifViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            String gif = gifList.get(position).getImageUrl();
            String engCaption = gifList.get(position).getEngCaption();
            String malayCaption = gifList.get(position).getMalayCaption();
            String messagePushID = gifList.get(position).getMessagePushID();
            String receiver = gifList.get(position).getReceiver();
            String sender = gifList.get(position).getSender();
            String uri = gifList.get(position).getUri();

            @Override
            public void onClick(View v) {
                HashMap<String, String> messagePictureBody = new HashMap<>();
                messagePictureBody.put("message", gif);
                messagePictureBody.put("type", "gif");
                messagePictureBody.put("from", sender);
                messagePictureBody.put("to", receiver);
                messagePictureBody.put("messageID", messagePushID);

                Intent intent = new Intent(t, EditGIFDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uri", uri);
                intent.putExtra("gifurl", gif);
                intent.putExtra("engCaption", engCaption);
                intent.putExtra("malayCaption", malayCaption);
                intent.putExtra("category", "");
                intent.putExtra("messageDetails", messagePictureBody);
                intent.putExtra("type", "Pending");
                v.getContext().startActivity(intent);


//                String gif = gifList.get(position).getImageUrl();
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
