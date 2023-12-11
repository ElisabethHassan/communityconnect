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

        // Initialize TextViews
        nameTextViewUPDATE = findViewById(R.id.nameTextViewUPDATE);
        emailTextViewUPDATE = findViewById(R.id.emailTextViewUPDATE);
        accountTypeTextViewUPDATE = findViewById(R.id.accountTypeTextViewUPDATE);
        logOutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.backButton);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        eventAdapter = new EventAdapter();
        myEventsList = new ArrayList<>();

        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);
        // Fetch and display user information from Firebase
        displayUserProfile();
        fetchUserEvents();

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
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
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
    private void displayUserProfile() {
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
                            nameTextViewUPDATE.setText(user.getName());
                            emailTextViewUPDATE.setText(user.getEmail());
                            accountTypeTextViewUPDATE.setText(user.getAccountType());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // show toast for error
                }
            });
        }
    }

    private void fetchUserEvents() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch user's events from Firebase and update RecyclerView
            DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference().child("user_events").child(userId);
            userEventsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        myEventsList.clear();

                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            Event event = eventSnapshot.getValue(Event.class);
                            if (event != null) {
                                myEventsList.add(event);
                            }
                        }

                        // update the RecyclerView with the user's events
                        eventAdapter.setEventList(myEventsList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // show toast
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
