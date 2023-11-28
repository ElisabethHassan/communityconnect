package com.example.finalproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Calendar extends AppCompatActivity {
    View cal;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarscreen);

        cal = findViewById(R.id.calendar);

    }
}
