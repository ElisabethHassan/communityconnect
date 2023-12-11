package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventCalendar extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarscreen);

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerViewEvents);
        eventAdapter = new EventAdapter();
        eventList = new ArrayList<>();
        backButton = findViewById(R.id.toOptionsButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);

        // Set a listener for calendar view date changes
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date change
                fetchEventsForSelectedDay(year, month, dayOfMonth);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCalendar.this, Options.class);
                startActivity(intent);
            }
        });
    }

    private void fetchEventsForSelectedDay(int year, int month, int dayOfMonth) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        // Format the selected date to match your database date format (MMDDYYYY)
        String formattedDate = formatDate(year, month + 1, dayOfMonth);

        // Query events for the selected date
        eventsRef.orderByChild("date").equalTo(formattedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }

                // Update the RecyclerView with the new event list
                eventAdapter.setEventList(eventList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                showToast("Error fetching events: " + databaseError.getMessage());
            }
        });
    }
    private String formatDate(int year, int month, int dayOfMonth) {
        //format: 2 digits for day, 2 digits for month, 4 digits for year
        return String.format(Locale.getDefault(), "%02d%02d%04d", month, dayOfMonth, year);
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

