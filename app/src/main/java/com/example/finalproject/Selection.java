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

                    // add the user's ID to the event's attendees list
                    List<String> attendees = selectedEvent.getAttendees();

                    // make sure that the attendees list is initialized
                    if (attendees == null) {
                        attendees = new ArrayList<>();
                        selectedEvent.setAttendees(attendees);
                    }

                    // check if the user is already signed up
                    if (!attendees.contains(currentUserId)) {
                        attendees.add(currentUserId);

                        // save the updated event back to Firebase
                        saveEventToFirebase(selectedEvent);

                        //show toast if successful
                        showToast("Signed up for the event!");

                        //update the user's myEvents list (makes sure database between the two databases are synced)
                        updateUserMyEvents(currentUserId, selectedEvent.getEventId());
                    } else {
                        showToast("You are already signed up for this event.");
                    }
                }
            }
        });
        //updates UI
        displayEventInfo();
    }

    private void updateUserMyEvents(String userId, String eventId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    // add the event ID to the user's myEvents list
                    List<String> myEvents = user.getMyEvents();

                    // make sure that myEvents list is initialized
                    if (myEvents == null) {
                        myEvents = new ArrayList<>();
                        user.setMyEvents(myEvents);
                    }

                    // check if the event is not already in the list
                    if (!myEvents.contains(eventId)) {
                        myEvents.add(eventId);

                        // save the updated user back to Firebase
                        userRef.setValue(user);
                    }
                }
            }

            //show toast on error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Error occurred.");
            }
        });
    }

    //if user signups, update event in firebase
    private void saveEventToFirebase(Event event) {
        // reference to the events node in the Firebase Database
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        // eventId for the event
        String eventId = event.getEventId();
        //  update the event data in the database
        eventsRef.child(eventId).setValue(event);
    }

    //update all info on display with corresponding event
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
    //show toast method, just to help simplify things
    private void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
