package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        //cardView = findViewById(R.id.cardView);
        //cardView2 = findViewById(R.id.cardView2);

        calendarButton= findViewById(R.id.calendarButton);
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

        //cardView.setOnClickListener(cardClickListener);
        //cardView2.setOnClickListener(cardClickListener);
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

                    // Update the RecyclerView with the new event list
                    eventAdapter.setEventList(eventList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                showToast("Error fetching events: " + databaseError.getMessage());
            }
        });
    }

    private boolean isEventValid(Event event) {
        // Get the current date
        Date currentDate = Calendar.getInstance().getTime();

        // Parse the event date from the string format "MMddyyyy" to Date
        Date eventDate = parseEventDate(event.getDate());

        // Check if the event date is in the future
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
