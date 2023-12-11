package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizationDashboardActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button logoutButton, addEventButton, seeProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_dashboard);

        // Initialize views
        welcomeTextView = findViewById(R.id.welcomeTextView);
        logoutButton = findViewById(R.id.logoutButton);
        addEventButton = findViewById(R.id.addEventButton);
        seeProfileButton = findViewById(R.id.seeProfileButton);

        // Fetch and display organization name
        fetchOrganizationName();

        // Set click listeners for buttons
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform logout and return to the Home page
                logout();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Add Event activity
                navigateToAddEvent();
            }
        });

        seeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Profile activity
                navigateToProfile();
            }
        });
    }

    private void fetchOrganizationName() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data found, retrieve organization name
                        String organizationName = dataSnapshot.child("organizationName").getValue(String.class);

                        // Display the organization name in the welcome message
                        if (organizationName != null) {
                            welcomeTextView.setText("Welcome, " + organizationName);
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

    private void logout() {
        //sign out user
        FirebaseAuth.getInstance().signOut();

        //navigate to the home page
        Intent intent = new Intent(OrganizationDashboardActivity.this, Home.class);
        startActivity(intent);

    }

    private void navigateToAddEvent() {
        //navigate to the add event activity
        Intent intent = new Intent(OrganizationDashboardActivity.this, AddEventActivity.class);
        startActivity(intent);
    }

    private void navigateToProfile() {
        // navigate to the profile activity
        Intent intent = new Intent(OrganizationDashboardActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
