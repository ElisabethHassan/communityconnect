package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Options extends AppCompatActivity {

    CardView cardView, cardView2;
    Button calendarButton;

    View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Options.this, Selection.class);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionsscreen);

        cardView = findViewById(R.id.cardView);
        cardView2 = findViewById(R.id.cardView2);

        calendarButton= findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventCalendar.class);
                startActivity(intent);
            }
        });


        cardView.setOnClickListener(cardClickListener);
        cardView2.setOnClickListener(cardClickListener);
    }
}
