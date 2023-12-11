package com.example.finalproject;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize TextViews
        nameTextViewUPDATE = findViewById(R.id.nameTextViewUPDATE);
        emailTextViewUPDATE = findViewById(R.id.emailTextViewUPDATE);
        accountTypeTextViewUPDATE = findViewById(R.id.accountTypeTextViewUPDATE);

        // Fetch and display user information from Firebase
        displayUserProfile();
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
