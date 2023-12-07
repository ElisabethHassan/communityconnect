package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {
    EditText email, password;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    TextView signup;

    View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Login.this, Signup.class );
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

        email = findViewById(R.id.email_ET);
        password = findViewById(R.id.password_ET);
        loginButton = findViewById(R.id.button);
        signup = findViewById(R.id.loginTV);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(loginListener);
        signup.setOnClickListener(signUpListener);
    }

    private void loginUser(){
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

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Options.class );
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed! Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
