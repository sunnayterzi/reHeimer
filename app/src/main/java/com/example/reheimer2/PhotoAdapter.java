package com.example.reheimer2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter <PhotoAdapter.PhotoViewHolder> {

    private ArrayList <Photo> pList;
    private Context context;

    public PhotoAdapter(Context context, ArrayList <Photo> pList) {
        this.context = context;
        this.pList = pList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);


        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = pList.get(position);

        Glide.with(context).load(pList.get(position).imageUrl).into(holder.imageView);
        holder.textView.setText(photo.imageDescription);

    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_album);
            textView= itemView.findViewById(R.id.textView_albumdescription);
        }
    }

}
