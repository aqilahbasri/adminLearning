package com.example.adminlearning;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category> categories;
    private DatabaseReference addcatRef;

    public MyAdapter (Context c, ArrayList<Category> p){
        context =c;
        categories =p;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.categoryname.setText(categories.get(position).getCategoryname());
        Picasso.get().load(categories.get(position).getCategoryimage()).into(holder.categoryimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog catDialog = new Dialog(context);
                catDialog.setContentView(R.layout.learn_question_choose);
                Button learncat = (Button) catDialog.findViewById(R.id.learnSL);
                Button challcat = (Button) catDialog.findViewById(R.id.challengeSL);
                Button editcat = (Button) catDialog.findViewById(R.id.editCat);
                final String catname = categories.get(position).getCategoryname();

                learncat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, listLearnSLactivity.class);
                        intent.putExtra("catTitle", catname);
                        context.startActivity(intent);

                    }
                });

                challcat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ListChallengeSLactivity.class);
                        intent.putExtra("catTitle", catname);
                        context.startActivity(intent);
                    }
                });

                editcat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, EditCategory.class);
                        intent.putExtra("catTitle", catname);
                        context.startActivity(intent);
                    }
                });



                catDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                catDialog.show();

            }

        });


    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryname;
        ImageView categoryimage;
        ImageButton deletebtn;
        Button yes,no;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            categoryimage = (ImageView) itemView.findViewById(R.id.categoryimage);
            deletebtn = (ImageButton) itemView.findViewById(R.id.delbtn);

            addcatRef = FirebaseDatabase.getInstance().getReference().child("LEARNING");

            //remove from fav
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
                            final Category cat = categories.get(position);
                            addcatRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String categoryimage = cat.getCategoryimage();
                                    String categoryname= cat.getCategoryname();

                                    Category delcat = new Category(categoryname, categoryimage);
                                    addcatRef.child(categoryname).removeValue();
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
