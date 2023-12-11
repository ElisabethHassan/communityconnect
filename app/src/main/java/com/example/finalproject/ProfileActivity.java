package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameTextViewUPDATE, emailTextViewUPDATE, accountTypeTextViewUPDATE;
    private Button logOutButton, backButton;

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
        // Fetch and display user information from Firebase
        displayUserProfile();

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
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        String userEmail = dataSnapshot.child("email").getValue(String.class);
                        String userAccountType = dataSnapshot.child("accountType").getValue(String.class);

                        // Update the TextViews with the user's information
                        nameTextViewUPDATE.setText(userName);
                        emailTextViewUPDATE.setText(userEmail);
                        accountTypeTextViewUPDATE.setText(userAccountType);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }


}
