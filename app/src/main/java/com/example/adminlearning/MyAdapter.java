package com.example.adminlearning;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Category> categories;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categoryname.setText(categories.get(position).getCategoryname());
        Picasso.get().load(categories.get(position).getCategoryimage()).into(holder.categoryimage);


    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryname;
        ImageView categoryimage;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            categoryimage = (ImageView) itemView.findViewById(R.id.categoryimage);
        }



    }




}
