package com.example.adminlearning;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.TestLooperManager;
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
    String refchild, refchildd;

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
    public void onBindViewHolder(@NonNull ChallengeSLadapter.MyViewHolder holder, final int position) {

        holder.slimage.loadUrl(challengeSLlist.get(position).getQuestion());
        holder.slimage.getSettings().setUseWideViewPort(true);
        holder.slimage.getSettings().setLoadWithOverviewMode(true);

        holder.slanswer.setText(challengeSLlist.get(position).desc);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refchildd = challengeSLlist.get(position).getDesc();
                Intent intent = new Intent(context, EditQuestion.class);
                intent.putExtra("catTitle", refchild);
                intent.putExtra("childTitle", refchildd);
                context.startActivity(intent);

        }
    });
    }


    public int getItemCount() {
        return challengeSLlist.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        WebView slimage;
        ImageButton deletebtn;
        TextView slanswer;
        Button yes,no;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            slimage = (WebView) itemView.findViewById(R.id.slimage);
            deletebtn = (ImageButton) itemView.findViewById(R.id.delbtn);
            slanswer = (TextView) itemView.findViewById(R.id.slanswer);

            addquesRef = FirebaseDatabase.getInstance().getReference().child("Question").child(refchild);


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
                            final ChallengeSLlist cat = challengeSLlist.get(position);
                            addquesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String question= cat.getQuestion();
                                    int correctAnswer = cat.getCorrectAnswer();
                                    String option1 = cat.getOption1();
                                    String option2 = cat.getOption2();
                                    String option3 = cat.getOption3();
                                    String option4 = cat.getOption4();
                                    String desc = cat.getDesc();

                                    ChallengeSLlist delcat = new ChallengeSLlist(question, correctAnswer, option1, option2, option3, option4, desc);
                                    addquesRef.child(desc).removeValue();
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






