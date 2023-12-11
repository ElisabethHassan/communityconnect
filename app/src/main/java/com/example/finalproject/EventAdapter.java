package com.example.finalproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter() {
        this.eventList = new ArrayList<>();
    }


    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        private TextView textEventName, textEventDate, textEventTime, textEventLocation, textEventOrg;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textEventName = itemView.findViewById(R.id.title);
            textEventDate = itemView.findViewById(R.id.date);
            textEventTime = itemView.findViewById(R.id.time);
            textEventLocation = itemView.findViewById(R.id.location);
            textEventOrg  = itemView.findViewById(R.id.organization);
        }

        public void bind(Event event) {
            textEventName.setText(event.getEventName());
            textEventDate.setText(formatDate(event.getDate()));
            textEventTime.setText(event.getEventName());
            textEventLocation.setText(event.getLocation());
            textEventOrg.setText(event.getOrganization());
        }
    }


    private String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMddyyyy");
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // Return original date if parsing fails
        }
    }



}
