package com.example.reheimer2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class VerbalMemoryActivity extends AppCompatActivity {

    private String[] wordArray;
    private ArrayList<String> seenArray;
    private int point;
    private int lives;
    Button btn;
    Button seenWord;
    Button newWord;
    TextView showWord;
    TextView pointsText;
    TextView livesText;
    TextView gameOver;
    Button cancelButton;
    TextView gameInfo;
    TextView gameInfoContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbal_memory);

        wordArray = new String[]{"word","damn","hey","hello","place","holder","cat","dog","animal","sky","wow","yes","hair"};

        btn = (Button) findViewById(R.id.button);
        seenWord = (Button) findViewById(R.id.seenWordBtn);
        newWord = (Button) findViewById(R.id.newWordBtn);
        showWord = (TextView) findViewById(R.id.showWord);
        pointsText = (TextView) findViewById(R.id.pointsText);
        livesText = (TextView) findViewById(R.id.livesText);
        gameOver = (TextView) findViewById(R.id.gameOverText);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        gameInfo = (TextView) findViewById(R.id.gameInfoText);
        gameInfoContext = (TextView) findViewById(R.id.gameInfoContext);

        gameInfo.setText(getString(R.string.game_info));
        gameInfoContext.setText(getString(R.string.game_info_context));


        seenWord.setVisibility(View.INVISIBLE);
        newWord.setVisibility(View.INVISIBLE);
        showWord.setVisibility(View.INVISIBLE);
        pointsText.setVisibility(View.INVISIBLE);
        livesText.setVisibility(View.INVISIBLE);
        gameOver.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                point = 0;
                lives = 3;
                seenArray = new ArrayList<>();
                btn.setVisibility(View.INVISIBLE);
                seenWord.setVisibility(View.VISIBLE);
                newWord.setVisibility(View.VISIBLE);
                showWord.setVisibility(View.VISIBLE);
                pointsText.setVisibility(View.VISIBLE);
                livesText.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                gameOver.setVisibility(View.INVISIBLE);
                gameInfo.setVisibility(View.INVISIBLE);
                gameInfoContext.setVisibility(View.INVISIBLE);

                pointsText.setText(getString(R.string.points_text,point));
                livesText.setText((getString(R.string.lives_text,lives)));

                String s = wordArray[(int)(Math.random() * wordArray.length)];
                showWord.setText(s);
            }
        });

        seenWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean includes = false;
                String a = showWord.getText().toString();
                for(String el : seenArray){
                    if (a.equals(el)) {
                        includes = true;
                        break;
                    }
                }

                if(includes){
                    point++;

                }else{
                    lives--;
                }

                if(lives < 1){
                    seenWord.setVisibility(View.INVISIBLE);
                    newWord.setVisibility(View.INVISIBLE);
                    showWord.setVisibility(View.INVISIBLE);
                    pointsText.setVisibility(View.INVISIBLE);
                    livesText.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                    btn.setVisibility(View.VISIBLE);
                    gameOver.setText(getString(R.string.game_over_text,point));
                    gameOver.setVisibility(View.VISIBLE);
                }
                seenArray.add(a);
                String s = wordArray[(int)(Math.random() * wordArray.length)];
                showWord.setText(s);
                pointsText.setText(getString(R.string.points_text,point));
                livesText.setText((getString(R.string.lives_text,lives)));

            }
        });

        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean includes = false;
                String a = showWord.getText().toString();
                for(String el : seenArray){
                    if (a.equals(el)) {
                        includes = true;
                        break;
                    }
                }

                if(includes){
                    lives--;
                }else{
                    point++;
                    seenArray.add(a);
                }
                if(lives < 1){
                    seenWord.setVisibility(View.INVISIBLE);
                    newWord.setVisibility(View.INVISIBLE);
                    showWord.setVisibility(View.INVISIBLE);
                    pointsText.setVisibility(View.INVISIBLE);
                    livesText.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                    btn.setVisibility(View.VISIBLE);
                    gameOver.setText(getString(R.string.game_over_text,point));
                    gameOver.setVisibility(View.VISIBLE);
                }
                String s = wordArray[(int)(Math.random() * wordArray.length)];
                showWord.setText(s);
                pointsText.setText(getString(R.string.points_text,point));
                livesText.setText((getString(R.string.lives_text,lives)));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seenWord.setVisibility(View.INVISIBLE);
                newWord.setVisibility(View.INVISIBLE);
                showWord.setVisibility(View.INVISIBLE);
                pointsText.setVisibility(View.INVISIBLE);
                livesText.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
                gameInfo.setVisibility(View.VISIBLE);
                gameInfoContext.setVisibility(View.VISIBLE);
            }
        });

    }
}