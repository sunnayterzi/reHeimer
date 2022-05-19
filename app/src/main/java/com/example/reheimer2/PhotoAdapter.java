package com.example.reheimer2;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    private OnItemClickListener mListener;

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

    // set imageView and imageDescription according to user's database information
    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = pList.get(position);

        Glide.with(context).load(pList.get(position).imageUrl).into(holder.imageView);
        holder.textView.setText(photo.imageDescription);


    }

    // assign elements to imageview and textview variables via ids.
    @Override
    public int getItemCount() {
        return pList.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView imageView;
        TextView textView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_album);
            textView= itemView.findViewById(R.id.textView_albumdescription);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Option");
            MenuItem doWhatever = contextMenu.add(contextMenu.NONE, 1, 1,"Update");
            MenuItem delete = contextMenu.add(contextMenu.NONE,2,2,"Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

}
