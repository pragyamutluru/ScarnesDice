package com.pragyamutluru.scarnesdice;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {


    //global variables for game functioning
    int totalUserScore=0;
    int totalCompScore=0;
    int turnUserScore=0;
    int turnCompScore=0;
    Random r;
    boolean userTurn=true;

    AlertDialog.Builder builder;
    TextView userScore;
    TextView turnView;
    Button roll;
    Button hold;
    TextView compScore;
    ImageView image;
    HashMap<Integer, Integer> intToImage= new HashMap<>();
    android.os.Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r=new Random();
        handler= new android.os.Handler();

        turnView=(TextView) findViewById(R.id.turnScore);
        userScore=(TextView) findViewById(R.id.userScore);
        hold= (Button) findViewById(R.id.buttonHold);
        roll=(Button) findViewById(R.id.buttonRoll);
        compScore=(TextView) findViewById(R.id.compScore);
        image= (ImageView)findViewById(R.id.imageView);
        userScore.setText(Integer.toString(totalUserScore));
        compScore.setText(Integer.toString(totalCompScore));
        intToImage.put(1, R.drawable.dice1);
        intToImage.put(2, R.drawable.dice2);
        intToImage.put(3, R.drawable.dice3);
        intToImage.put(4, R.drawable.dice4);
        intToImage.put(5, R.drawable.dice5);
        intToImage.put(6, R.drawable.dice6);
    }
    public void rollButtonClicked(View view){
        roll();
    }

    public void roll(){

        int i=r.nextInt(5)+1;
       // Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
        updateImage(i);
        updateScore(i);

    }
    public void updateScore(int i){
        if(i!=1){
            if(userTurn){
                turnUserScore+=i;
                turnView.setText(Integer.toString(turnUserScore));
                checkWinner();

            }
            else{
                turnCompScore+=i;
                checkWinner();
                compTurn();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "1 Occured...End of turn!", Toast.LENGTH_SHORT).show();
            endTurn();
        }
    }
    public void endTurn(){
        userTurn=!userTurn;
        turnCompScore=0;
        turnUserScore=0;

        turnView.setText(Integer.toString(0));

        if(!userTurn){
            compTurn();
        }
        else{
            roll.setEnabled(true);
            hold.setEnabled(true);
        }
    }

    public void compTurn(){
        roll.setEnabled(false);
        hold.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int odds=r.nextInt(10);
                if(odds%3==0){
                    holdFunc();
                    Toast.makeText(getApplicationContext(), "Computer put a hold", Toast.LENGTH_SHORT).show();
                }
                else{
                    roll();

                  //  Toast.makeText(getApplicationContext(), "Computer rolled", Toast.LENGTH_SHORT).show();
                }


            }
        },2000);
    }

    public void resetClicked(View view){
        reset();}
    public void reset(){
        if(!userTurn){
            endTurn();
        }
        roll.setEnabled(true);
        hold.setEnabled(true);
        userTurn=true;
        turnUserScore=0;
        turnCompScore=0;
        totalCompScore=0;
        totalUserScore=0;

        turnView.setText("0");
        compScore.setText(Integer.toString(0));
        userScore.setText(Integer.toString(0));

        updateImage(1);

    }

    public void holdClicked(View view){
        holdFunc();
    }
    public void holdFunc(){
        totalUserScore+=turnUserScore;
        totalCompScore+=turnCompScore;
        checkWinner();
        turnView.setText(Integer.toString(0));
        userScore.setText(Integer.toString(totalUserScore));
        compScore.setText(Integer.toString(totalCompScore));
        endTurn();
    }
    public void checkWinner(){
        if(totalCompScore>=100|| totalUserScore>=100){

            builder= new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("We have a Winner!");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setNeutralButton("Reset Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });
            if(totalCompScore>=100)
                builder.setMessage("Computer Won");
            else
                builder.setMessage("User Won");

            AlertDialog dialog= builder.create();
        dialog.show();
    }
    }

    public void updateImage(int i){

        image.setImageResource(intToImage.get(i));
    }
}

