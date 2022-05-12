package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GamesActivity extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    private TextView memorizeNumberText, cardMatchingText;
    private ImageView memorizeNumberImage, cardMatchingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        memorizeNumberImage = (ImageView)findViewById(R.id.imageView_memorizeNumbers);
        memorizeNumberImage.setOnClickListener(this);

        cardMatchingImage = (ImageView)findViewById(R.id.imageView_cardMatching);
        cardMatchingImage.setOnClickListener(this);

        memorizeNumberText = (TextView) findViewById(R.id.textView_memorizeNumbers);
        memorizeNumberText.setOnClickListener(this);

        cardMatchingText = (TextView) findViewById(R.id.textView_cardMatching);
        cardMatchingText.setOnClickListener(this);

        // bottom navigation bar controls
        bottomNavigationView=findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_game);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_photos:
                        startActivity(new Intent(getApplicationContext(),PhotosActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_reminder:
                        startActivity(new Intent(getApplicationContext(),ReminderActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_location:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_game:
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

    // navigate user accarding to his/her game choice
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView_memorizeNumbers:
            case R.id.textView_memorizeNumbers:
                startActivity(new Intent(this,MemorizeNumbers.class));
                break;
            case R.id.imageView_cardMatching:
            case R.id.textView_cardMatching:
                startActivity(new Intent(this,CardMatchingMenuActivity.class));
                break;
        }

    }


}