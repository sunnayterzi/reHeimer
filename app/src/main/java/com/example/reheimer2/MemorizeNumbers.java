package com.example.reheimer2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MemorizeNumbers extends AppCompatActivity {

    private TextView point, number;
    private EditText typeNumber;
    private Button check;
    Random random;
    int clickCounter = 0;
    int currentPoint = 0;
    int level = 1;
    String showedNumber;
    Button exitGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize_numbers);


        point = (TextView) findViewById(R.id.textView_pointNumbers);
        random = new Random();
        number = (TextView) findViewById(R.id.textView_numbers);
        typeNumber = (EditText) findViewById(R.id.editTextText_typeNumber);
        point.setText("Point: " +currentPoint);
        showedNumber=generateNumbers(level);
        exitGame = (Button) findViewById(R.id.button_exitmain);
        number.setText(showedNumber);
        check = (Button) findViewById(R.id.button_numberCheck);
        typeNumber.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
        number.setVisibility(View.VISIBLE);

        // show number to user 2 sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                typeNumber.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
                number.setVisibility(View.GONE);
                typeNumber.requestFocus();

            }
        }, 2000);


        // compare user input with number if they are match increase point else show dialog box
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [ ] splitted = point.getText().toString().split(" ");
                if(showedNumber.equals(typeNumber.getText().toString())) {
                    currentPoint = Integer.parseInt(splitted[1]) + level*10;
                    typeNumber.setText("");
                    typeNumber.setVisibility(View.GONE);
                    check.setVisibility(View.GONE);
                    number.setVisibility(View.VISIBLE);
                    point.setText("Point: " + currentPoint);
                    clickCounter++;
                    if (clickCounter!=level){
                        showedNumber = generateNumbers(level);
                        number.setText(showedNumber);

                    }

                    else{
                        level++;
                        showedNumber = generateNumbers(level);
                        number.setText(showedNumber);
                        clickCounter=0;

                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            typeNumber.setVisibility(View.VISIBLE);
                            check.setVisibility(View.VISIBLE);
                            number.setVisibility(View.GONE);

                            typeNumber.requestFocus();

                        }
                    }, 2000);
                }

                else {
                    showDialog(showedNumber, currentPoint);


                    check.setEnabled(false);
                }


            }
        });
    }

    // generate random numbers with increasing digits
    private String generateNumbers(int digits){
        String output = "";
        for (int i=0; i<digits; i++) {
            int randomDigit = random.nextInt(10);
            output = output + "" + randomDigit;

        }
        return output;
    }

    // set dialog box elements
    public void showDialog(String showedNumber, int currentPoint){
        Intent gameIntent = new Intent(this, GamesActivity.class);
        final AlertDialog.Builder alert = new AlertDialog.Builder(MemorizeNumbers.this);
        View mView = getLayoutInflater().inflate(R.layout.memorise_number_dialog, null);

        final TextView textScore = (TextView) mView.findViewById(R.id.textView_earnPoint);
        final TextView textCorrectNum = (TextView) mView.findViewById(R.id.textView_correctNum);
        Button exit = (Button)mView.findViewById(R.id.button_exitNumbers);
        Button retry = (Button) mView.findViewById(R.id.button_retry);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        textCorrectNum.setText("Generated number was "+ showedNumber);
        textScore.setText("You have reached " + currentPoint+" points");



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(gameIntent);
                alertDialog.dismiss();

            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent thisIntent=getIntent();
                finish();
                startActivity(thisIntent);
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }

    // when clicked exit button, navigate game intent
    public void exitGame(View view) {

        Intent exitIntent = new Intent(this, GamesActivity.class);
        startActivity(exitIntent);
    }
}