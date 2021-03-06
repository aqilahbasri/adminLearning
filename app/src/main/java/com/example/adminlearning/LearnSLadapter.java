package com.example.adminlearning;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
    String refchild, refchildd;

    public LearnSLadapter (Context c, ArrayList<LearnSLlist> p, String data){
        context =c;
        learnSLlists =p;
        refchild = data;
    }



    @NonNull
    @Override
    public LearnSLadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LearnSLadapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardquesview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.sldesc.setText(learnSLlists.get(position).getSldescription());
        holder.slimage.getSettings().setAppCacheEnabled(true);
        holder.slimage.loadUrl(learnSLlists.get(position).getImgurl());
        holder.slimage.getSettings().setUseWideViewPort(true);
        holder.slimage.getSettings().setLoadWithOverviewMode(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refchildd = learnSLlists.get(position).getSldescription();
                Intent intent = new Intent(context, EditSL.class);
                intent.putExtra("catTitle", refchild);
                intent.putExtra("childTitle", refchildd);
                context.startActivity(intent);

            }

        });
    }


    @Override
    public int getItemCount() {
        return learnSLlists.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        WebView slimage;
        ImageButton deletebtn;
        TextView sldesc;
        Button yes,no;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sldesc = (TextView) itemView.findViewById(R.id.slanswer);
            slimage = (WebView) itemView.findViewById(R.id.slimage);
            deletebtn = (ImageButton) itemView.findViewById(R.id.delbtn);

            addslRef = FirebaseDatabase.getInstance().getReference().child("SignLanguage").child(refchild);


            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog deleteDialog = new Dialog(view.getContext());
                    deleteDialog.setContentView(R.layout.deleteconfirmation);
                    yes = (Button) deleteDialog.findViewById(R.id.delete);
                    no = (Button) deleteDialog.findViewById(R.id.dontdelete);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();
                            final LearnSLlist cat = learnSLlists.get(position);
                            addslRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String imgurl = cat.getImgurl();
                                    String sldescriptionname= cat.getSldescription();

                                    LearnSLlist delcat = new LearnSLlist(sldescriptionname, imgurl);
                                    addslRef.child(sldescriptionname).removeValue();
                                    deleteDialog.dismiss();

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {


                                }
                            });

                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteDialog.dismiss();
                        }
                    });

                    deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    deleteDialog.show();

                }
            });
        }



    }




}

