package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReminderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        calendarView = (CalendarView) findViewById(R.id.calendarV);
        Button addEventBtn = (Button) findViewById(R.id.calendar_button);

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReminderActivity.this,AddEventActivity.class));
            }
        });


        bottomNavigationView=findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_reminder);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_photos:
                        startActivity(new Intent(getApplicationContext(),PhotosActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_reminder:

                        return true;

                    case R.id.nav_location:
                        startActivity(new Intent(getApplicationContext(),LocationActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_game:
                        startActivity(new Intent(getApplicationContext(),GamesActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}