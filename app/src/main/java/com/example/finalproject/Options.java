package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;

public class Options extends AppCompatActivity {

    CardView cardView, cardView2;
    Button calendarButton, toProfileButton;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Options.this, Selection.class);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionsscreen);
        eventAdapter = new EventAdapter();

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
        eventList = new ArrayList<>();
        calendarButton = findViewById(R.id.calendarButton);


        calendarButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventCalendar.class);
                startActivity(intent);
            }
        });

        toProfileButton = findViewById(R.id.toProfButton);
        toProfileButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        fetchEventsFromFirebase();

    }

    private void fetchEventsFromFirebase() {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    eventList.clear();

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        if (event != null && isEventValid(event)) {
                            eventList.add(event);
                        }
                    }

                    // sort events by date
                    Collections.sort(eventList, new Comparator<Event>() {
                        @Override
                        public int compare(Event event1, Event event2) {
                            // date is in the format "MMddyyyy"
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy", Locale.getDefault());
                            try {
                                //compare two dates
                                Date date1 = dateFormat.parse(event1.getDate());
                                Date date2 = dateFormat.parse(event2.getDate());
                                return date1.compareTo(date2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    // update the RecyclerView with the new event list
                    eventAdapter.setEventList(eventList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // show error message
                showToast("Error fetching events: " + databaseError.getMessage());
            }
        });
    }
    //don't show past events
    private boolean isEventValid(Event event) {
        // Get the current date
        Date currentDate = Calendar.getInstance().getTime();

        // parse the event date from the string format "MMddyyyy" to date
        Date eventDate = parseEventDate(event.getDate());

        // check if the event date is in the future
        return eventDate != null && eventDate.after(currentDate);
    }

    private Date parseEventDate(String inputDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy", Locale.getDefault());
            return dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
