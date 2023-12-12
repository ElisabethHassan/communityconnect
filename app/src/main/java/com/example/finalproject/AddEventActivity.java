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

    private EditText editTextEventName, editTextDate, editTextTime, editTextLocation, editTextDescription;
    private Button buttonAddEvent, backButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        databaseHelper = new DatabaseHelper();

        editTextEventName = findViewById(R.id.editTextEventName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        backButton = findViewById(R.id.backButton2);


        // listeners
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
        // get event information from EditText fields
        String eventName = editTextEventName.getText().toString();
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String location = editTextLocation.getText().toString();
        String blurb = editTextDescription.getText().toString();

        // validate event information (no field can be empty)
        if (eventName.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty() || blurb.isEmpty()) {
            showToast("Please enter all information.");
            return;
        }

        //organization associated with event is autofilled with the current user's id
        // get  current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // get user's name from the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("name");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.getValue(String.class);

                        // create a new Event object
                        Event newEvent = new Event(eventName, date, time, location, userName, blurb);
                        showToast("Event Added: " + eventName);
                        // add the event to the database
                        databaseHelper.addEvent(newEvent);

                    }
                }

                //show toast on error
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Error occurred.");
                }
            });
        }
    }

    //show toast method for simplicity
    private void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
