package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Options extends AppCompatActivity {

    CardView cardView;
    CardView cardView2;

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

        cardView.setOnClickListener(cardClickListener);
        cardView2.setOnClickListener(cardClickListener);
    }
}
