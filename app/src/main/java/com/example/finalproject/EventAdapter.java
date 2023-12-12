package com.example.finalproject;


import android.content.Intent;
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

    //set the event list and notify of data change
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    //create a card view for each event in the event list
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

        //bind intent to each event card to bring to selection (info page) page of selected event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Selection.class);
                intent.putExtra("selectedEvent", event);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        private TextView textEventName, textEventDate, textEventTime, textEventLocation, textEventOrg;

        public EventViewHolder(@NonNull View itemView) {
            //get the text views for the event, these are in the card_event xml
            super(itemView);
            textEventName = itemView.findViewById(R.id.title);
            textEventDate = itemView.findViewById(R.id.date);
            textEventTime = itemView.findViewById(R.id.time);
            textEventLocation = itemView.findViewById(R.id.location);
            textEventOrg  = itemView.findViewById(R.id.organization);
        }

        public void bind(Event event) {
            //set the text views with the current event info
            textEventName.setText(event.getEventName());
            textEventDate.setText(formatDate(event.getDate()));
            textEventTime.setText(event.getTime());
            textEventLocation.setText(event.getLocation());
            textEventOrg.setText(event.getOrganization());
        }
    }

    //format date into easy to read format: MM/DD/YYYY
    private String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMddyyyy");
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // return original date if parsing fails
        }
    }



}
