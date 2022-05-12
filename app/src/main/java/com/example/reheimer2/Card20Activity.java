package com.example.reheimer2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Card20Activity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;
    private MemorizeButton [] buttons;
    private int [] buttonGraphicLocations;
    private int [] buttonGraphics;
    private MemorizeButton selectedButton1;
    private MemorizeButton selectedButton2;
    int count = 0;
    private TextView timerText;
    private boolean isBusy = false;
    private boolean timerStarted = false;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    Button exittTwenty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card20);
        exittTwenty = (Button) findViewById(R.id.button_exit20);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout_5x4);
        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();
        numberOfElements = numColumns*numRows;
        buttons = new MemorizeButton[numberOfElements];
        buttonGraphics = new int[numberOfElements/2];
        buttonGraphics[0] = R.drawable.at;
        buttonGraphics[1] = R.drawable.devekusu;
        buttonGraphics[2] = R.drawable.dog;
        buttonGraphics[3] = R.drawable.inek;
        buttonGraphics[4] = R.drawable.kelebek;
        buttonGraphics[5] = R.drawable.kirpi;
        buttonGraphics[6] = R.drawable.pig;
        buttonGraphics[7] = R.drawable.rabbit;
        buttonGraphics[8] = R.drawable.snail;
        buttonGraphics[9] = R.drawable.tirtil;

        //// Adding timer to set game time
        timerText = (TextView) findViewById(R.id.Text_cardMatchingTimer20);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerText.setText(getTimerText());

                    }
                });
            }


        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

        buttonGraphicLocations = new int[numberOfElements];
        shuffleButtonGraphics();

        // // adding photos to gridLayout
        for(int r = 0; r<numRows; r++) {
            for(int c = 0; c<numColumns; c++ ){
                MemorizeButton tempButton = new MemorizeButton(this, r, c,
                        buttonGraphics[buttonGraphicLocations[r*numColumns+c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r*numColumns+c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }
    }

    // calculating hours, minutes and seconds
    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) /3600) ;

        return formatTime(seconds, minutes, hours);

    }

    //converting int time to string format time
    private String formatTime(int seconds, int minutes, int hours) {

        return String.format("%02d",hours) + ":" +  String.format("%02d",minutes) + ":" + String.format("%02d",seconds);
    }

    // shuffle the view of cards to provide random placement
    private void shuffleButtonGraphics() {
        Random random = new Random();

        for(int i = 0; i<numberOfElements; i++){
            buttonGraphicLocations[i]= i % (numberOfElements/2);

        }

        for (int j = 0; j < numberOfElements; j++){
            int temp = buttonGraphicLocations[j];
            int swapLocation = random.nextInt(12);
            buttonGraphicLocations[j] = buttonGraphicLocations[swapLocation];
            buttonGraphicLocations[swapLocation] = temp;
        }
    }

    // game logic
    @Override
    public void onClick(View view) {
        Intent gameIntent = new Intent(this, GamesActivity.class);

        if(isBusy)
            return;

        MemorizeButton button = (MemorizeButton) view;

        if(button.isMatched)
            return;

        if(selectedButton1 == null){
            selectedButton1 = button;
            selectedButton1.flip();
            return;

        }

        if(selectedButton1.getId() == button.getId()) {
            return;
        }

        // If a match is made, these cards are not turned back and the number of matches is increased by one.
        // When the number of matches reaches half the number of cards, the alert dialog is displayed.
        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()){
            button.flip();
            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);
            count++;


            selectedButton1 =null;
            if (count==10){

                timerText.setVisibility(View.INVISIBLE);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Card20Activity.this);
                alertDialogBuilder.setMessage("GAME OVER \n \n You completed it in " + timerText.getText().toString() + " time")

                        .setCancelable(false)
                        .setPositiveButton("New", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), Card20Activity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(gameIntent);
                                finish();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }

            return;

        }

        //If there is no match, the cards are returned after half a second.
        else{
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1=null;
                    selectedButton2=null;
                    isBusy = false;

                }
            },500);
        }

    }


    public void exitTwenty(View view) {
        Intent yintent = new Intent(this, CardMatchingMenuActivity.class);
        startActivity(yintent);
    }
}