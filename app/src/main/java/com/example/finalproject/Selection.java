package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Inside Selection activity
public class Selection extends AppCompatActivity {
    TextView back, eventNameTextView, eventDateTextView, eventTimeTextView, eventLocationTextView, eventOrgTextView, eventDescriptionTextView;
    Event selectedEvent;
    Button eventSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosenactivityscreen);

        back = findViewById(R.id.back_tv);
        eventNameTextView = findViewById(R.id.eventNameTextView);
        eventDateTextView = findViewById(R.id.eventDateTextView);
        eventTimeTextView = findViewById(R.id.eventTimeTextView);
        eventLocationTextView = findViewById(R.id.eventLocationTextView);
        eventOrgTextView = findViewById(R.id.eventOrgTextView);
        eventDescriptionTextView = findViewById(R.id.eventDescripTextView);
        eventSignUpButton = findViewById(R.id.eventSignUpButton);

        Intent intent = getIntent();
        selectedEvent = intent.getParcelableExtra("selectedEvent");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Options activity
                Intent backIntent = new Intent(Selection.this, Options.class);
                startActivity(backIntent);
            }
        });

        eventSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEvent != null) {
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Add the user's ID to the event's attendees list
                    List<String> attendees = selectedEvent.getAttendees();

                    // Ensure attendees list is initialized
                    if (attendees == null) {
                        attendees = new ArrayList<>();
                        selectedEvent.setAttendees(attendees);
                    }

                    // Check if the user is already signed up
                    if (!attendees.contains(currentUserId)) {
                        attendees.add(currentUserId);

                        // Save the updated event back to Firebase
                        saveEventToFirebase(selectedEvent);

                        // Optionally, you can update the UI to reflect the change
                        // or navigate to a different screen.
                        showToast("Signed up for the event!");

                        // Now, update the User's myEvents list
                        updateUserMyEvents(currentUserId, selectedEvent.getEventId());
                    } else {
                        showToast("You are already signed up for this event.");
                    }
                }
            }
        });

// Method to update User's myEvents list




        displayEventInfo();
    }

    private void updateUserMyEvents(String userId, String eventId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    // Add the event ID to the user's myEvents list
                    List<String> myEvents = user.getMyEvents();

                    // Ensure myEvents list is initialized
                    if (myEvents == null) {
                        myEvents = new ArrayList<>();
                        user.setMyEvents(myEvents);
                    }

                    // Check if the event is not already in the list
                    if (!myEvents.contains(eventId)) {
                        myEvents.add(eventId);

                        // Save the updated user back to Firebase
                        userRef.setValue(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void saveEventToFirebase(Event event) {
        // Get a reference to the events node in the Firebase Database
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        // Get the unique identifier (eventId) for the event
        String eventId = event.getEventId();
        // Use the eventId to update the event data in the database
        eventsRef.child(eventId).setValue(event);
    }
    private void displayEventInfo() {
        if (selectedEvent != null) {
            eventNameTextView.setText(selectedEvent.getEventName());
            eventDateTextView.setText(selectedEvent.getDate());
            eventTimeTextView.setText(selectedEvent.getTime());
            eventLocationTextView.setText(selectedEvent.getLocation());
            eventOrgTextView.setText(selectedEvent.getOrganization());
            eventDescriptionTextView.setText(selectedEvent.getDescription());
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
