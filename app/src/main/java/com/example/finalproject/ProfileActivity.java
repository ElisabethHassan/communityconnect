package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameTextViewUPDATE, emailTextViewUPDATE, accountTypeTextViewUPDATE;
    private RecyclerView recyclerViewEvents;
    private Button logOutButton, backButton;
    private EventAdapter eventAdapter;
    private List<Event> myEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initialize TextViews
        nameTextViewUPDATE = findViewById(R.id.nameTextViewUPDATE);
        emailTextViewUPDATE = findViewById(R.id.emailTextViewUPDATE);
        accountTypeTextViewUPDATE = findViewById(R.id.accountTypeTextViewUPDATE);
        logOutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.backButton);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        //crete event adapter and events list
        eventAdapter = new EventAdapter();
        myEventsList = new ArrayList<>();

        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);

        // fetch and display user information from Firebase
        displayUserProfile();
        fetchUserEvents();

        //listeners
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform logout and return to the Home page
                logout();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user from firebase
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //get account type as this dictates their app experience
                                String userAccountType = dataSnapshot.child("accountType").getValue(String.class);
                                if(userAccountType.equals("volunteer")) {
                                    Intent intent = new Intent(ProfileActivity.this, Options.class);
                                    startActivity(intent);
                                }else{ //if not volunteer then user is an organization
                                    Intent intent = new Intent(ProfileActivity.this, OrganizationDashboardActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                        //on error display toast
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            showToast("Error getting data.");
                        }
                    });
                }
            }
        });

    }

    private void logout() {
        //sign out user
        FirebaseAuth.getInstance().signOut();

        //navigate to the home page
        Intent intent = new Intent(ProfileActivity.this, Home.class);
        startActivity(intent);

    }

    //display info for current user
    private void displayUserProfile() {
        //get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            //update profile texts based on account info
                            nameTextViewUPDATE.setText(user.getName());
                            emailTextViewUPDATE.setText(user.getEmail());
                            accountTypeTextViewUPDATE.setText(user.getAccountType());
                        }
                    }
                }
                // show toast for error
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Error getting data.");

                }
            });
        }
    }

    //get the events associated with current user
    private void fetchUserEvents() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // fetch user's myEvents from firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            // get the user's myEvents list
                            List<String> myEvents = user.getMyEvents();

                            if (myEvents != null) {
                                // fetch events for each event ID in myEvents list
                                fetchEventsForUser(userId, myEvents);
                            }
                        }
                    }
                }
                //show toast on error
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("There was an error getting events.");
                }
            });
        }
    }

    // helper method to fetch events for the user
    private void fetchEventsForUser(String userId, List<String> myEvents) {

        // fetch events from Firebase based on the myEvents list
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        if (event != null && myEvents.contains(event.getEventId())) {
                            //add events to users myEventsList
                            myEventsList.add(event);
                        }
                    }

                    // update the recyclerview with the user's events
                    eventAdapter.setEventList(myEventsList);
                }
            }
            //show toast on error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("There was an error getting events.");
            }
        });
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
