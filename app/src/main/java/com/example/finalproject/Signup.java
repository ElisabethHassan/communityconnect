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

       //brings user to login screen
       loginButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), Login.class);
               startActivity(intent);
           }
       });

   }

   //confirm validity of passwords
   private boolean confirmPasswords(){
        String pass = passwordEditText.getText().toString();
        String pass2 =confirmPassword.getText().toString();
        //firebase requires password of at least length 6
        if(pass.length() >= 6){
            if(pass.equals(pass2))
                //password and password confirmation are equal
                return true;
            else{
                //if user enters different password alert them
                showToast("Passwords don't match.");
                return false;
            }
        }else{
            showToast("Passwords must be at least 6 characters.");
            return false;
        }
   }


   public void onSignup(){
       String email = emailEditText.getText().toString().trim();
       String password = passwordEditText.getText().toString().trim();
       String name = nameEditText.getText().toString().trim();
       String accountType;

       int selectedRadioButtonId = accountTypeRadioGroup.getCheckedRadioButtonId();

       //determine which account type was selected
       if (selectedRadioButtonId == R.id.volunteerRadioButton) {
           accountType = "volunteer";
       } else if (selectedRadioButtonId == R.id.organizationRadioButton) {
           accountType = "organization";
       } else {
           //user can't make an account without picking an account type
            showToast("Select an account type!") ;
            return;
       }

       // make sure user inputs all required information
       if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {
           showToast("Make sure to enter all information!");
           return;
       }

       firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                   if (firebaseUser != null) {
                       String userId = firebaseUser.getUid();
                       //create user object with all info
                       User newUser = new User(userId, name, email, accountType);

                       //save user in firebase
                       saveUserProfile(newUser);

                       //let user know their account was made successfully
                       showToast("Registration Successful!");
                   } else {
                       //show toast on error
                       showToast("Registration Failed! Please try again.");
                   }
               } else {
                   //show toast on error
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

            //make hashmap of user data to store in firebase
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("accountType", user.getAccountType());

            usersRef.setValue(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //if user account made successful bring them to login (app doesn't grant auto access upon signing up)
                            navigateToLoginScreen();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        //show toast on error
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to save user profile.");
                        }
                    });
        }
    }

    //navigate user to login screen
    private void navigateToLoginScreen() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
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
