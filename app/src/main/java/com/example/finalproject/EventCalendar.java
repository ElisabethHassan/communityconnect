package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventCalendar extends AppCompatActivity {
    CalendarView calendarView;
    ListView eventListView;
    Calendar calendar;
    List<Event> events;
    ArrayAdapter<Event> eventAdapter;
    Button toOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarscreen);

        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.eventListView);
        calendar = Calendar.getInstance();
        events = new ArrayList<>();
        eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);

        // Set up the ListView
        eventListView.setAdapter(eventAdapter);

        toOptions= findViewById(R.id.toOptionsButton);
        toOptions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Options.class);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                displayEvents(dayOfMonth, month, year);
                Toast.makeText(getApplicationContext(), (dayOfMonth + "/" + month + "/" + year), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getDate(){
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(getApplicationContext(),selected_date, Toast.LENGTH_SHORT).show();
    }

    public void setDate(int day, int month, int year){
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        long time = calendar.getTimeInMillis();
        calendarView.setDate(time);
    }

    private void displayEvents(int day, int month, int year) {
        // TODO: Retrieve events for the selected date and update the 'events' list
        events.clear();
        //events.add("Event 1");
        //events.add("Event 2");
        // Update the ListView
        eventAdapter.notifyDataSetChanged();
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
