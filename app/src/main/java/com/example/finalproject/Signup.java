package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {
    EditText nameEditText, emailEditText, passwordEditText, confirmPassword;
    Button signUpButton, loginButton;

    FirebaseAuth firebaseAuth;
    RadioGroup accountTypeRadioGroup;
    Boolean success;


   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.signupscreen);
       nameEditText = findViewById(R.id.fullNameET);
       emailEditText = findViewById(R.id.emailET);
       passwordEditText = findViewById(R.id.passwordET);
       confirmPassword = findViewById(R.id.confirmPasswordET);
       accountTypeRadioGroup = findViewById(R.id.accountTypeRadioGroup);
       firebaseAuth = FirebaseAuth.getInstance();

       signUpButton = findViewById(R.id.signUpSubmit);
       loginButton = findViewById(R.id.loginButton);

       signUpButton.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               success = confirmPasswords();
               if(success){
                   //add info to database
                   onSignup();
               }
           }
       });

       loginButton.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), Login.class);
               startActivity(intent);
           }
       });

   }

   private boolean confirmPasswords(){
        String pass = passwordEditText.getText().toString();
        String pass2 =confirmPassword.getText().toString();
        if(pass.length() == 6 && 6 == pass2.length()){
            if(pass.equals(pass2))
                return true;
            else{
                showToast("Passwords don't match.");
                return false;
            }
        }
        showToast("Passwords must me at least 6 characters.");
        return false;
   }


   public void onSignup(){
       String email = emailEditText.getText().toString().trim();
       String password = passwordEditText.getText().toString().trim();
       String name = nameEditText.getText().toString().trim();

       String accountType;
       int selectedRadioButtonId = accountTypeRadioGroup.getCheckedRadioButtonId();
       if (selectedRadioButtonId == R.id.volunteerRadioButton) {
           accountType = "volunteer";
       } else if (selectedRadioButtonId == R.id.organizationRadioButton) {
           accountType = "organization";
       } else {
           Toast.makeText(getApplicationContext(), "Please select an account type.", Toast.LENGTH_SHORT).show();
           return;
       }

       // Validations for input email and password
       if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {
           Toast.makeText(getApplicationContext(), "Make sure to enter your name, email & password.", Toast.LENGTH_SHORT).show();
           return;
       }

       firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                   if (firebaseUser != null) {
                       String userId = firebaseUser.getUid();
                       User newUser = new User(userId, name, email, accountType);
                       saveUserProfile(newUser);

                       if ("organization".equals(accountType)) {
                           //redirect to organization dashboard- can go to add event or organization profile
                       } else {
                           navigateToLoginScreen();
                       }

                       showToast("Registration Successful!");
                   } else {
                       showToast("Registration Failed! Please try again.");
                   }
               } else {
                   showToast("Registration Failed! Please try again.");
               }
           }
       });
   }

    private void saveUserProfile(User user) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("accountType", user.getAccountType());

            usersRef.setValue(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            navigateToNextScreen(user.getAccountType());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to save user profile.");
                        }
                    });
        }
    }

    private void navigateToNextScreen(String accountType) {
        if ("organization".equals(accountType)) {
            // Redirect to Organization Dashboard or similar
        } else {
            navigateToLoginScreen();
        }
    }

    private void navigateToLoginScreen() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
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
