package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText fullName, email, password, confirmPassword;
    Button signUpButton, loginButton;

    FirebaseAuth firebaseAuth;

    Boolean success;


   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.signupscreen);
       fullName = findViewById(R.id.fullNameET);
       email = findViewById(R.id.emailET);
       password = findViewById(R.id.passwordET);
       confirmPassword = findViewById(R.id.confirmPasswordET);

       firebaseAuth = FirebaseAuth.getInstance();

       signUpButton = findViewById(R.id.signUpSubmit);
       loginButton = findViewById(R.id.loginButton);

       signUpButton.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               boolean passCheck = validPasswordCheck();
               if(passCheck)
                   success = confirmPasswords();
               if(success){
                   //add info to database
                   onSignup();
                   Intent intent = new Intent(getApplicationContext(), Login.class);
                   startActivity(intent);
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


    private boolean validPasswordCheck(){
        String pass = password.getText().toString();
        char ch;
        int correctness = 0;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        //password must contain 1 number, 1 uppercase, 1 lowercase, 1 special character(!@#$%^&*():;.,<>?), and 0 spaces
        if(pass.matches(".*[!@#$%^&*():;.,<>?].*")){
            correctness++;
        }

        for(int i =0; i<pass.length();i++){
            ch = pass.charAt(i);
            if(Character.isUpperCase((ch)))
                capitalFlag=true;
            if(Character.isLowerCase(ch))
                lowerCaseFlag=true;
            if(capitalFlag&&lowerCaseFlag)
                correctness +=2;


        if(correctness==3){
            return true;
        }

    }return false;

    }

   private boolean confirmPasswords(){
        String pass = password.getText().toString();
        String pass2 =confirmPassword.getText().toString();
        if(pass.equals(pass2))
            return true;
        else
            return false;
   }


   public void onSignup(){
       String userEmail = email.getText().toString();
       String userPassword = password.getText().toString();

       // Validations for input email and password
       if (TextUtils.isEmpty(userEmail)) {
           Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
           return;
       }
       if (TextUtils.isEmpty(userPassword)) {
           Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
           return;
       }

       firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Failed! Please try again.", Toast.LENGTH_LONG).show();
                }
           }
       });

   }
}
