package com.manikshakya.flappybirdtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button click1;
    private Button click2;
    private Button click3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click1 = (Button) findViewById(R.id.click1);
        click2 = (Button) findViewById(R.id.click2);
        click3 = (Button) findViewById(R.id.click3);

        // Click or touch event handler
        click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Message","Hello World");
                Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
                intent.putExtra("difficulty", "1");
                startActivity(intent);
            }
        });

        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Message","Hello World");
                Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
                intent.putExtra("difficulty", "2");
                startActivity(intent);
            }
        });

        click3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        });
    }
}