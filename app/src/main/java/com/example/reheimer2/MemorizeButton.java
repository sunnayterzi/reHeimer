package com.example.reheimer2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemorizeButton extends androidx.appcompat.widget.AppCompatButton {

    protected int row, column,frontDrawableId;
    protected boolean isFlipped = false;
    protected boolean isMatched = false;
    protected Drawable front, back;


    // grid layout settings
    public MemorizeButton(Context context, int r, int c, int frontImageDrawableId){
        super(context);

        row =r;
        column = c;
        frontDrawableId = frontImageDrawableId;
        front = context.getDrawable(frontImageDrawableId);
        back = context.getDrawable(R.drawable.card_back3);
        setBackground(back);
        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r),GridLayout.spec(c));
        //87 for phone 130 for emulator
        tempParams.width = (int) getResources().getDisplayMetrics().density*130;
        tempParams.height = (int) getResources().getDisplayMetrics().density*130;
        setLayoutParams(tempParams);


    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public void flip(){
        if(isMatched){
            return;
        }
        // when click the image button turn and show the image
        if(isFlipped){
            setBackground(back);
            isFlipped=false;
        }

        else{
            setBackground(front);
            isFlipped=true;
        }
    }
}
