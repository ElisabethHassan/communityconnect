package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
public class AddEventActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        databaseHelper = new DatabaseHelper();


        Event newEvent = new Event();
        newEvent.setEventName("Sample Event");
        newEvent.setDate("2023-01-01");
        newEvent.setTime("12:00 PM");
        newEvent.setLocation("Sample Location");
        newEvent.setAddress("123 Sample St");
        newEvent.setOrganizationId("org1");

        // Add the event to the database
        databaseHelper.addEvent(newEvent);
    }

    //implement stuff for adding event
}
