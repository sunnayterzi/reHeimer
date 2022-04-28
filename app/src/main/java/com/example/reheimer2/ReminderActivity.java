package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReminderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CalendarView calendarView;
    TextView myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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