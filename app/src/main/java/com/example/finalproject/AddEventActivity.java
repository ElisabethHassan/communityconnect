package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextEventName, editTextDate, editTextTime, editTextLocation;
    private Button buttonAddEvent, backButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize Firebase Database
        databaseHelper = new DatabaseHelper();

        // Initialize UI elements
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        backButton = findViewById(R.id.backButton2);

        // Set onClickListener for the "Add Event" button
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventToDatabase();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEventActivity.this, OrganizationDashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addEventToDatabase() {
        // Get event information from EditText fields
        String eventName = editTextEventName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        // Validate event information (you can add more validation if needed)
        if (eventName.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {

            return;
        }

        // Get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve the user's name from the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("name");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.getValue(String.class);

                        // Create a new Event object
                        Event newEvent = new Event(eventName, date, time, location, userName);
                        showToast("Event Added: " + eventName);
                        // Add the event to the database
                        databaseHelper.addEvent(newEvent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
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
