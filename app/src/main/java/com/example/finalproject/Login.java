package com.example.finalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    TextView signup;

    View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        }
    };

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginUser();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_ET);
        password = findViewById(R.id.password_ET);
        loginButton = findViewById(R.id.button);
        signup = findViewById(R.id.loginTV);

        loginButton.setOnClickListener(loginListener);
        signup.setOnClickListener(signUpListener);
    }

    //login in the user confirming it is a valid user in firebase
    private void loginUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (userEmail.isEmpty() || userPassword.isEmpty())
            Toast.makeText(getApplicationContext(), "Please enter login information.", Toast.LENGTH_LONG).show();
        else {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Login successful");

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            Log.d(TAG, "User ID: " + userId);

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "onDataChange executed");
                                    if (dataSnapshot.exists()) {
                                        String accountType = dataSnapshot.child("accountType").getValue(String.class);
                                        Log.d(TAG, "Account type: " + accountType);

                                        if ("volunteer".equals(accountType)) {
                                            // redirect to options activity for volunteers members
                                            Intent intent = new Intent(Login.this, Options.class);
                                            startActivity(intent);
                                        } else if ("organization".equals(accountType)) {
                                            // redirect to organization dashboard  for organizations members
                                            Intent intent = new Intent(Login.this, OrganizationDashboardActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                                //log error
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                                }
                            });
                        }
                    } else {
                        //log error and show toast if login failed
                        Log.e(TAG, "Login failed: " + task.getException().getMessage());
                        Toast.makeText(getApplicationContext(), "Login Failed! Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}