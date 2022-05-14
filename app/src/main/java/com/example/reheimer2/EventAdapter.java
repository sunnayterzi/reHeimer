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

public class EventAdapter extends RecyclerView.Adapter <EventAdapter.EventViewHolder> {

    /* Class to convert an array list to fit in a recycler view */

    private ArrayList <SingleEvent> eventList;

    public EventAdapter(ArrayList <SingleEvent> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        * Every list item puts into a event_item layout. So that every item will look as a card
        * with individual information
        */
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.event_item, parent, false);
        EventViewHolder vHolder = new EventViewHolder(listItem);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        SingleEvent event = eventList.get(position);

        /*Every event_item gets the data on the single event (context, date, hour) */
        holder.eventContext.setText(event.getEventContext());
        holder.eventDate.setText(event.getDate());
        holder.eventHour.setText(event.getHour());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventContext;
        TextView eventDate;
        TextView eventHour;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            this.eventContext = itemView.findViewById(R.id.eventContext);
            this.eventDate = itemView.findViewById(R.id.eventDate);
            this.eventHour = itemView.findViewById(R.id.eventTime);

        }
    }

}
