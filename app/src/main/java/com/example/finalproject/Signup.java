package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {
    EditText fullName, email, password, confirmPassword;
    Button signUpButton;

   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.signupscreen);
       fullName = findViewById(R.id.fullNameET);
       email = findViewById(R.id.emailET);
       password = findViewById(R.id.passwordET);
       confirmPassword = findViewById(R.id.confirmPasswordET);

       signUpButton = findViewById(R.id.signUpSubmit);

       signUpButton.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               boolean passCheck = validPasswordCheck();
               if(passCheck)
                   confirmPasswords();

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
        int maxU =0;
        for(int i =0; i<pass.length() && maxU <1;i++){
            ch = pass.charAt(i);
            if(Character.isUpperCase((ch)))
                capitalFlag=true;
            if(Character.isLowerCase(ch))
                lowerCaseFlag=true;
            if(capitalFlag&&lowerCaseFlag)



        return false;




        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }
    }

   private boolean confirmPasswords(){
        String pass = password.getText().toString();
        String pass2 =confirmPassword.getText().toString();
        if(pass.equals(pass2))
            return true;
        else
            return false;
   }
}