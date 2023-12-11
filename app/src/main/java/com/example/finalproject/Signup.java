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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;


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
        if(pass.equals(pass2))
            return true;
        else
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
           // Handle the case where any field is empty
           return;
       }

       firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Failed! Please try again.", Toast.LENGTH_LONG).show();
                }
           }
       });

   }
}
