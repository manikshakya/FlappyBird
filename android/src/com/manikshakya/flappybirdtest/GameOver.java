package com.manikshakya.flappybirdtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOver extends Activity implements View.OnClickListener {

    private Button playAgain;
    private Button home;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        home = (Button) findViewById(R.id.gameover1);
        playAgain = (Button) findViewById(R.id.gameover2);
        exit = (Button) findViewById(R.id.gameover3);


        home.setOnClickListener(this);
        playAgain.setOnClickListener(this);
        exit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.gameover1){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.gameover2){
            Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
            intent.putExtra("difficulty", "2");
            startActivity(intent);
        }

        if(view.getId() == R.id.gameover3){
//            Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
//            intent.putExtra("difficulty", "2");
//            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        }

    }
}