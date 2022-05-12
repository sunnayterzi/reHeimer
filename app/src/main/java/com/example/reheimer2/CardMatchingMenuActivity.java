package com.example.reheimer2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CardMatchingMenuActivity extends AppCompatActivity {

    private Button button12;
    private Button button16;
    private Button button20;
    private Button buttonback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_matching_menu);
        button12 = findViewById(R.id.button_12cards);
        button16 = findViewById(R.id.button_16cards);
        button20 = findViewById(R.id.button_20cards);
        buttonback = findViewById(R.id.button_backMenu1);
    }
    // navigate user according to choice
    public void navigate4x3(View view) {
        Intent navigate12 = new Intent(this, CardMatchingActivity.class);
        startActivity(navigate12);
    }

    public void navigate16cards(View view) {
        Intent navigate16 = new Intent(this, Card16Activity.class);
        startActivity(navigate16);
    }

    public void navigate20cards(View view) {
        Intent navigate20 = new Intent(this, Card20Activity.class);
        startActivity(navigate20);
    }

    public void backMenu(View view) {
        Intent mintent = new Intent(this, GamesActivity.class);
        startActivity(mintent);
    }
}