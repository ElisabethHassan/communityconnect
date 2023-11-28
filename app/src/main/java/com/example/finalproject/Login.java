package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button loginButton;

    View.OnClickListener loginListenter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Login.this, Options.class );
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        email = findViewById(R.id.email_ET);
        password = findViewById(R.id.password_ET);
        loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(loginListenter);

    }
}