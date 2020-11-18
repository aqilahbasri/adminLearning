package com.example.adminlearning;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChallengeSLadapter extends RecyclerView.Adapter< ChallengeSLadapter.MyViewHolder> {

    Context context;
    ArrayList<ChallengeSLlist> challengeSLlist;
    private DatabaseReference addquesRef;
    String refchild;

    public  ChallengeSLadapter (Context c, ArrayList<ChallengeSLlist> p, String data){
        context =c;
        challengeSLlist =p;
        refchild = data;
    }



    @NonNull
    @Override
    public ChallengeSLadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChallengeSLadapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardquesview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.slimage.loadUrl(challengeSLlist.get(position).getQuestion());
        holder.slimage.getSettings().setUseWideViewPort(true);
        holder.slimage.getSettings().setLoadWithOverviewMode(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog catDialog = new Dialog(context);
                catDialog.setContentView(R.layout.learn_question_choose);
                Button learncat = (Button) catDialog.findViewById(R.id.learnSL);
                Button challcat = (Button) catDialog.findViewById(R.id.challengeSL);

                catDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                catDialog.show();

            }

        });
    }


    @Override
    public int getItemCount() {
        return challengeSLlist.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        WebView slimage;
        ImageButton deletebtn;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            slimage = (WebView) itemView.findViewById(R.id.slimage);
            deletebtn = (ImageButton) itemView.findViewById(R.id.delbtn);

            addquesRef = FirebaseDatabase.getInstance().getReference().child("Question").child(refchild);


            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final ChallengeSLlist cat = challengeSLlist.get(position);
                    addquesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String question= cat.getQuestion();

//                            ChallengeSLlist delcat = new ChallengeSLlist(question);
//                            addquesRef.child(pushkey).removeValue();

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }
                    });

                }
            });
        }



    }




}


