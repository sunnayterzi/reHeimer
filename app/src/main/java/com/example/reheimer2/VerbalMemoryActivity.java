package com.example.reheimer2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.BooleanResult;

import org.w3c.dom.Text;

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
    Button cancelButton;
    TextView gameInfo;
    TextView gameInfoContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbal_memory);

        /* wordArray gets assigned an array from string resources. That array is filled with words
        *  These words will be used to show on the screen. */
        wordArray = getResources().getStringArray(R.array.word_list);

        btn = (Button) findViewById(R.id.button);
        seenWord = (Button) findViewById(R.id.seenWordBtn);
        newWord = (Button) findViewById(R.id.newWordBtn);
        showWord = (TextView) findViewById(R.id.showWord);
        pointsText = (TextView) findViewById(R.id.pointsText);
        livesText = (TextView) findViewById(R.id.livesText);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        gameInfo = (TextView) findViewById(R.id.gameInfoText);
        gameInfoContext = (TextView) findViewById(R.id.gameInfoContext);

        gameInfo.setText(getString(R.string.game_info));
        gameInfoContext.setText(getString(R.string.game_info_context));

        /* These parts belongs to the game screen so before the game starts they will be invisible */
        seenWord.setVisibility(View.INVISIBLE);
        newWord.setVisibility(View.INVISIBLE);
        showWord.setVisibility(View.INVISIBLE);
        pointsText.setVisibility(View.INVISIBLE);
        livesText.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);

        /* This button starts the game. Invisible elements become visible. Since this button starts
        *  the game point will be equal to 0 and lives will be equal to 3.  */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                point = 0;
                lives = 3;

                /* This array list will hold the seen words. So that when checking for if a word seen or
                *  new we will look at this list */
                seenArray = new ArrayList<>();
                btn.setVisibility(View.INVISIBLE);
                gameInfo.setVisibility(View.INVISIBLE);
                gameInfoContext.setVisibility(View.INVISIBLE);

                seenWord.setVisibility(View.VISIBLE);
                newWord.setVisibility(View.VISIBLE);
                showWord.setVisibility(View.VISIBLE);
                pointsText.setVisibility(View.VISIBLE);
                livesText.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);


                pointsText.setText(getString(R.string.points_text,point));
                livesText.setText((getString(R.string.lives_text,lives)));

                /* First word is chosen from the array. */
                String s = wordArray[(int)(Math.random() * wordArray.length)];
                showWord.setText(s);
            }
        });

        /* When user clicks the seen button, if the word has already been seen then user earns 1 point
        *  and game continues with another word. If the word is new and user clicks on seen button then
        *  user loses one of their lives. */
        seenWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean includes = false;
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
                    /* If lives are less than 1 than game is over */
                    gameOver(point);
                }

                boolean alreadyInSeen = false;
                for(String element : seenArray){
                    if (a.equals(element)) {
                        alreadyInSeen = true;
                        break;
                    }
                }

                if(!alreadyInSeen){
                    seenArray.add(a);
                }

                /* Since in large array of words, it is unlikely to get already seen word. Because
                *  of this next word is either taken from the seenArray that has seen words or the
                *  word is taken from the all word array. */
                String s = getNextWord(seenArray,wordArray);
                showWord.setText(s);
                pointsText.setText(getString(R.string.points_text,point));
                livesText.setText((getString(R.string.lives_text,lives)));

            }
        });

        /* When user clicks the new button, if the word is new then user earns 1 point and game
         * continues with another word. If the word is seen and user clicks on new button then
         *  user loses one of their lives. */
        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean includes = false;
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
                    gameOver(point);
                }

                String s = getNextWord(seenArray,wordArray);
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

    /* Makes game related elements invisible and shows a dialogue box. In the box user can see
    *  how many points they earn. Also there are two buttons. Retry and exit. */
    public void gameOver(int point){
        seenWord.setVisibility(View.INVISIBLE);
        newWord.setVisibility(View.INVISIBLE);
        showWord.setVisibility(View.INVISIBLE);
        pointsText.setVisibility(View.INVISIBLE);
        livesText.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);


        final AlertDialog.Builder gameOverAlert = new AlertDialog.Builder(VerbalMemoryActivity.this);
        View gameOverView = getLayoutInflater().inflate(R.layout.verbal_game_over, null);
        Intent returnGamesIntent = new Intent(this, GamesActivity.class);

        final TextView gameOverText = (TextView) gameOverView.findViewById(R.id.textView7);

        Button exit = (Button) gameOverView.findViewById(R.id.verbalExitButton);
        Button retry = (Button) gameOverView.findViewById(R.id.verbalRetryBtn);
        gameOverAlert.setView(gameOverView);

        final AlertDialog alertDialog = gameOverAlert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        gameOverText.setText(getString(R.string.game_over_text,point));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(returnGamesIntent);
                alertDialog.dismiss();

            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent thisIntent = getIntent();
                finish();
                startActivity(thisIntent);
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }

    /* Next word is taken from either seen array list (30%) or the main word array (70%)*/
    public String getNextWord(ArrayList<String> seenList, String[] wordArray){
        int p = (int) (Math.random() * 10);

        if(p <= 2){
            return seenList.get((int)(Math.random() * seenList.size()));
        }else{
            return wordArray[(int)(Math.random() * wordArray.length)];
        }
    }
}