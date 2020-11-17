package com.example.adminlearning;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class LearnSLadapter extends RecyclerView.Adapter<LearnSLadapter.MyViewHolder> {

    Context context;
    ArrayList<LearnSLlist> learnSLlists;
    private DatabaseReference addslRef;
    String refchild;

    public LearnSLadapter (Context c, ArrayList<LearnSLlist> p, String data){
        context =c;
        learnSLlists =p;
        refchild = data;
    }



    @NonNull
    @Override
    public LearnSLadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LearnSLadapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.categoryname.setText(learnSLlists.get(position).getSldescription());
        holder.categoryimage.loadUrl(learnSLlists.get(position).getImgurl());
        holder.categoryimage.getSettings().setUseWideViewPort(true);
        holder.categoryimage.getSettings().setLoadWithOverviewMode(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog catDialog = new Dialog(context);
                catDialog.setContentView(R.layout.learn_question_choose);
                Button learncat = (Button) catDialog.findViewById(R.id.learnSL);
                Button challcat = (Button) catDialog.findViewById(R.id.challengeSL);
//                final String catname = categories.get(position).getCategoryname();
//
//                learncat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, listLearnSLactivity.class);
//                        intent.putExtra("catTitle", catname);
//                        context.startActivity(intent);
//
//                    }
//                });
//
//                challcat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, ListChallengeSLactivity.class);
//                        intent.putExtra("catTitle", catname);
//                        context.startActivity(intent);
//                    }
//                });



                catDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                catDialog.show();

            }

        });
    }


    @Override
    public int getItemCount() {
        return learnSLlists.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryname;
        WebView categoryimage;
        ImageButton deletebtn;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            categoryimage = (WebView) itemView.findViewById(R.id.categoryimage);
            deletebtn = (ImageButton) itemView.findViewById(R.id.delbtn);

            addslRef = FirebaseDatabase.getInstance().getReference().child("SignLanguage").child(refchild);

            //remove from fav
            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final LearnSLlist cat = learnSLlists.get(position);
                    addslRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String imgurl = cat.getImgurl();
                            String sldescriptionname= cat.getSldescription();

                            Category delcat = new Category(sldescriptionname, imgurl);
                            addslRef.child(sldescriptionname).removeValue();

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

