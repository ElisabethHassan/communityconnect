package com.example.finalproject;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    DatabaseReference databaseReference;
    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");
    DatabaseReference orgREf = FirebaseDatabase.getInstance().getReference().child("organizations");

    //helps with connections between the databases that has the events and the users
    public DatabaseHelper() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addEvent(Event event) {
        String eventId = databaseReference.child("events").push().getKey();
        event.setEventId(eventId);
        assert eventId != null;
        databaseReference.child("events").child(eventId).setValue(event);
    }

    //for database of organizations -> this was not implemented
    public void addOrganization(Organization organization) {
        String orgId = databaseReference.child("organizations").push().getKey();
        organization.setOrganizationId(orgId);
        assert orgId != null;
        databaseReference.child("organizations").child(orgId).setValue(organization);
    }
    public void getEvents(final ValueEventListener listener) {
        databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Process the list of events and pass them to the listener
                List<Event> events = new ArrayList<>();

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);

                    // Ensure the event is not null before adding it to the list
                    if (event != null) {
                        events.add(event);
                    }
                }

                listener.onDataChange(snapshot);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(DatabaseError.fromException(error.toException()));
            }
        });
    }

}
