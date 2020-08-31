package com.manikshakya.flappybirdtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements Test, GameOverInterface {

	private String difficulty;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		difficulty = getIntent().getExtras().getString("difficulty");

		this.setData(getIntent().getExtras().getString("difficulty"));
		Log.i("Hello: ", this.getData());

		Log.i("Type", this.getClass().getName());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new FlappyBird(this, this), config);
	}

	public void happy(){}


	@Override
	public String getData() {
		return this.difficulty;
	}

	@Override
	public void setData(String s) {
		this.difficulty = s;
	}

	@Override
	public void switchActivity() {
		Intent intent = new Intent(getApplicationContext(), GameOver.class);
		startActivity(intent);
	}
}

