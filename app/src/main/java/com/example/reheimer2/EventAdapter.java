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
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter <EventAdapter.EventViewHolder> {

    private ArrayList <SingleEvent> eventList;
    private Context context;
    private OnItemClickListener mListener;

    public EventAdapter(Context context, ArrayList <SingleEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);


        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        SingleEvent event = eventList.get(position);

        //Glide.with(context).load(pList.get(position).imageUrl).into(holder.imageView);
        holder.eventContext.setText(event.getEventContext());
        holder.eventDate.setText(event.getDate());
        holder.eventHour.setText(event.getHour());
    }


    // assign elements to imageview and textview variables via ids.
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView eventContext;
        TextView eventDate;
        TextView eventHour;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            this.eventContext = itemView.findViewById(R.id.eventContext);
            this.eventDate = itemView.findViewById(R.id.eventDate);
            this.eventHour = itemView.findViewById(R.id.eventTime);

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