package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// Inside Selection activity
public class Selection extends AppCompatActivity {
    TextView back, eventNameTextView, eventDateTextView, eventTimeTextView, eventLocationTextView, eventOrgTextView, eventDescriptionTextView;
    Event selectedEvent;

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

        // Display event information in TextView elements
        displayEventInfo();
    }

    private void displayEventInfo() {
        if (selectedEvent != null) {
            eventNameTextView.setText("Event Name: " + selectedEvent.getEventName());
            eventDateTextView.setText("Event Date: " + selectedEvent.getDate());
            eventTimeTextView.setText("Event Time: " + selectedEvent.getTime());
            eventLocationTextView.setText("Event Location: " + selectedEvent.getLocation());
            eventOrgTextView.setText("Event Organization: " + selectedEvent.getOrganization());
            eventDescriptionTextView.setText("Event Description" + selectedEvent.getDescription());
        }
    }
}
