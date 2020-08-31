package com.manikshakya.flappybirdtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
//	private Texture background;
//	private Texture[] bird;
//	private Texture topTube;
//	private Texture bottomTube;
//	private Texture gameover;


	//=================================================
		// Variables to initialize the images
	//=================================================
	private Sprite background;
	private Sprite[] bird;
	private Sprite topTube;
	private Sprite bottomTube;
	private Sprite gameover;
	//=================================================

	private int birdState = 0; // Birs state. Bird flapping up or down.
	private int counter = 0; // For testing purposes. Keeps track of number of touches

	/*
	* 	The position of the bird in y axis.
	* 	The value changes when the players taps the screen. Bird flies up.
	* 	else the gravity is increased and the bird falls.
	* */
	private float yPosition;

	/*
	* 	Games state keeps track of the state of the game.
	* 	By default, game is in gamestate 0 where player has to touch the screen to
	* 	play the game.
	*
	* 	gamestate == 1 is playing state.
	* 	gamestate == 2 is gameoverscreen.
	* */
	private int gameState = 0;
	private float gravity = 5; // 1 in real device // 5 in Emulator
	private float velocity = 0;
	private float tubeVelocity = 8; // 3 in real device // 8 in Emulator


	/*
	* 	Minimum gap between two tubes vertically. Top tube and bottom tube.
	* */
	private float gap = 450;

	private int numberOfTubes = 4;
	private float[] tubeX = new float[numberOfTubes];

	// Minimum gap between two tubes so the two tubes are not too close to each other.
	private float[] tubeOffset = new float[numberOfTubes];

	// Distance between the tubes horizontally.
	private float distanceBetweenTubes;

	private Random random;

	private ShapeRenderer shapeRenderer;
	private Circle circle;
	private Rectangle[] topRectangles;
	private Rectangle[] bottomRectangles;

	private BitmapFont font;
	private BitmapFont highscoreFont;

	private int score = 0;
	private int scoringTube = 0;

	//=================================================
	Texture newBird;
	int birdWidth;
	int birdHeight;
	//=================================================

	private TextureAtlas textureAtlas;
	private Animation<TextureRegion> animation;
	private float elapsedTime = 0f;

	private float tubeWidth;
	private float dis;

	private OrthographicCamera camera;
//	private PerspectiveCamera camera;
	private float GAME_WORLD_WIDTH = 100f ;
	private float GAME_WORLD_HEIGHT = 50f;
	private Viewport viewport;

	// returns the difficulty number
	private Test difficulty;

	// returns the game over screen intent.
	private GameOverInterface gameOverInterface;

	// Keeps track of the game if the game is still in play or is it over.
	private boolean valid;

	// File to read and save the highscore.
	private FileHandle file;

	public FlappyBird(Test difficulty, GameOverInterface gameOverInterface) {
		this.difficulty = difficulty;
		this.gameOverInterface = gameOverInterface;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
//		background = new Texture("bg.png");
//		bird = new Texture[2];
//		bird[0] = new Texture("bird.png");
//		bird[1] = new Texture("bird2.png");
//
//		topTube = new Texture("toptube.png");
//		bottomTube = new Texture("bottomtube.png");
//		gameover = new Texture("game-over.png");

		background = new Sprite(new Texture("bg.png"));
//		background.setPosition(0, 0);
//		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bird = new Sprite[2];
		bird[0] = new Sprite(new Texture("bird.png"));
		bird[1] = new Sprite(new Texture("bird2.png"));

		topTube = new Sprite(new Texture("toptube.png"));
		bottomTube = new Sprite(new Texture("bottomtube.png"));
		gameover = new Sprite(new Texture("game-over.png"));

		shapeRenderer = new ShapeRenderer();
		circle = new Circle();
		topRectangles = new Rectangle[numberOfTubes];
		bottomRectangles = new Rectangle[numberOfTubes];


		// Displays the scores
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		// Displays the high score.
		highscoreFont = new BitmapFont();
		highscoreFont.setColor(Color.WHITE);
		highscoreFont.getData().setScale(5);

		// Random number to randomly place the tubes up and down.
		random = new Random();

		yPosition = Gdx.graphics.getHeight() / 2 - bird[birdState].getHeight() / 2;

//		tubeX = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2;
//		tubeX = Gdx.graphics.getWidth();

		dis = 1.25f;
//		distanceBetweenTubes = Gdx.graphics.getWidth() / 3f; // Gdx.graphics.getWidth() / 1.25f
//		distanceBetweenTubes = Gdx.graphics.getWidth() / dis;
		if(Integer.parseInt(this.difficulty.getData()) == 1){
			distanceBetweenTubes = Gdx.graphics.getWidth() / 1.25f;
		}else if(Integer.parseInt(this.difficulty.getData()) == 2){
			distanceBetweenTubes = Gdx.graphics.getWidth() / 2;
		}


		for(int i = 0; i < numberOfTubes; i++){
//			if(Integer.parseInt(this.difficulty.getData()) == 2){
//				distanceBetweenTubes = Gdx.graphics.getWidth() / (float)(1.25f + (random.nextDouble() * (1.75 - 1.25)));
//			}
			tubeOffset[i] = (random.nextFloat() - 0.5f) * Gdx.graphics.getHeight() / 2;
//			tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			if(i == 0){
				tubeX[i] = Gdx.graphics.getWidth();
			}else{
				tubeX[i] = tubeX[i - 1] + distanceBetweenTubes; // Gdx.graphics.getWidth() / 2
			}
			topRectangles[i] = new Rectangle();
			bottomRectangles[i] = new Rectangle();
		}


//========================================================================
//		newBird = new Texture(Gdx.files.internal("birdTest.png"));
//		TextureRegion[][] tmp = TextureRegion.split(newBird,
//				newBird.getWidth() / 2, // FRAME_COLS
//				newBird.getHeight() / 1); // FRAME_ROWS
//
//		// Place the regions into a 1D array in the correct order, starting from the top
//		// left, going across first. The Animation constructor requires a 1D array.
//		TextureRegion[] walkFrames = new TextureRegion[2]; // [FRAME_COLS * FRAME_ROWS]
//		int index = 0;
//		for (int i = 0; i < 1; i++) {
//			for (int j = 0; j < 2; j++) {
//				walkFrames[index++] = tmp[i][j];
//			}
//		}
//		birdWidth = walkFrames[0].getRegionWidth();
//		birdHeight = walkFrames[0].getRegionHeight();
//
//
//		System.out.println("Tmp: " + Arrays.toString(tmp));
//		System.out.println("WalFrames: " + Arrays.toString(walkFrames));
//		animation = new Animation<>(0.00001f, walkFrames);
//========================================================================

		Gdx.app.log("Gdx Height", "" + Gdx.graphics.getHeight());
		Gdx.app.log("Top Tube", "" + topTube.getHeight());
		Gdx.app.log("Bottom Tube", "" + bottomTube.getHeight());

		tubeWidth = tubeX[3] + topTube.getWidth();
		Gdx.app.log("Tube Width: ", "" + tubeWidth);
		Gdx.app.log("Tube Width: ", "" + (Gdx.graphics.getWidth()));
		Gdx.app.log("Tube Width: ", "" + (tubeWidth - Gdx.graphics.getWidth()));

//		Gdx.app.log("FPS", Gdx.graphics.getFramesPerSecond() + "");

		GAME_WORLD_WIDTH = Gdx.graphics.getWidth();
		GAME_WORLD_HEIGHT = Gdx.graphics.getHeight();

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
//		float aspectRatio = (float) GAME_WORLD_WIDTH / (float) GAME_WORLD_HEIGHT;
		camera = new OrthographicCamera();
//		camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);

		viewport = new ExtendViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, camera);
		viewport.apply();
		camera.position.set(GAME_WORLD_WIDTH / 2, GAME_WORLD_HEIGHT / 2, 0);
//		camera.update();

		Gdx.app.log("Diff: ", this.difficulty.getData());

		valid = false;
		file = Gdx.files.local("highscore"+ this.difficulty.getData() +".txt");

		if(Integer.parseInt(this.difficulty.getData()) == 1){
			gap = 550;
		}
	}

	@Override
	public void render () {
		Gdx.app.log("Distance between tubes", "" + (1.25f + (random.nextDouble() * (1.75 - 1.25))));
//		elapsedTime += Gdx.graphics.getDeltaTime();

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1){
			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 - bird[birdState].getWidth()){
				score++;

				Gdx.app.log("Score", score + "");

				if(scoringTube < numberOfTubes - 1){
					scoringTube++;
				}else{
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()){
				Gdx.app.log("Message", "Screen Touched " + counter++ + " / " + yPosition);
				velocity = -30; // -15 in real device // -30 in Emulator

				Gdx.app.log("FPS", Gdx.graphics.getFramesPerSecond() + "");

				Gdx.app.log("Bird X", bird[0] + "");
				Gdx.app.log("Bird Y", bird[0].getHeight() + "");
			}

			for(int i = 0; i < numberOfTubes; i++) {
				if(tubeX[i] < - topTube.getWidth()){
//					if(Integer.parseInt(this.difficulty.getData()) == 2){
////						if(i == 0){
////							distanceBetweenTubes = Gdx.graphics.getWidth() / (float)(1.75f + (random.nextDouble() * (1.75 - 1.25)));
////						}else{
////							distanceBetweenTubes = Gdx.graphics.getWidth() / (float)(1.25f + (random.nextDouble() * (1.75 - 1.25)));
////						}
////						distanceBetweenTubes = Gdx.graphics.getWidth() / (float)(1.75f + (random.nextDouble() * (1.75 - 1.25)));
//						distanceBetweenTubes = Gdx.graphics.getWidth() / 2;
//					}
					tubeOffset[i] = (random.nextFloat() - 0.5f) * Gdx.graphics.getHeight() / 2;
//					tubeX[i] = numberOfTubes * distanceBetweenTubes - distanceBetweenTubes / 1.25f;
//					tubeX[i] += numberOfTubes * distanceBetweenTubes; //numberOfTubes * distanceBetweenTubes; // - topTube.getWidth();
//					if(i-1 < 0){
//						tubeX[numberOfTubes - 1] += Gdx.graphics.getWidth() / (float)(1.25f + (random.nextDouble() * (1.75 - 1.25)));
//					}else{
//						tubeX[i - 1] += Gdx.graphics.getWidth() / (float)(1.25f + (random.nextDouble() * (1.75 - 1.25)));
//					}

					if(i == 0){
						tubeX[i] = tubeX[numberOfTubes - 1] + distanceBetweenTubes;
					}else{
						tubeX[i] = tubeX[i - 1] + distanceBetweenTubes;
					}

					Gdx.app.log("Tuve value", "" + tubeX[(i-1 < 0) ? numberOfTubes - 1 : i - 1]);
//					tubeX[i] += tubeX[(i-1 < 0) ? numberOfTubes - 1 : i - 1] * (float)(1.25f + (random.nextDouble() * (1.75 - 1.25)));


					Gdx.app.log("Tube Width: ", "" + tubeX[i]);
				}else{
					tubeX[i] -= tubeVelocity;
				}

				Gdx.app.log("Tube offset", "" + tubeOffset[i]);

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]); // 0 - 400
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 + tubeOffset[i] - bottomTube.getHeight());

				topRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 + tubeOffset[i] - bottomTube.getHeight(), topTube.getWidth(), topTube.getHeight());
			}

			if(yPosition > 0){ //  || velocity < 0
				velocity += gravity;
				yPosition -= velocity;
			}else{
				gameState = 2;
				valid = true;
			}

			for(int i = 0; i < numberOfTubes; i++){
//			shapeRenderer.rect(topRectangles[i].x, topRectangles[i].y, topRectangles[i].width, topRectangles[i].height);
//			shapeRenderer.rect(bottomRectangles[i].x, bottomRectangles[i].y, bottomRectangles[i].width, bottomRectangles[i].height);

				if(Intersector.overlaps(circle, topRectangles[i]) || Intersector.overlaps(circle, bottomRectangles[i])){
					Gdx.app.log("Collision: ", "" + tubeX[i]);
					gameState = 2;
					valid = true;
					break;
				}
			}
		}else if(gameState == 0){
			if(Gdx.input.justTouched()){
				Gdx.app.log("Message", "Screen Touched 1 " + counter++ + " / " + yPosition);
				// Bird flies
//				yPosition += 20f;
				gameState = 1;
			}
		}else if(gameState == 2){
			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

			if(valid){

				Gdx.app.log("Exists", "" + file.exists());

				if(file.exists()){
					String highscore = file.readString();
					Gdx.app.log("Highscore", highscore);
					if(Integer.parseInt(highscore) < score){
						file.writeString(String.valueOf(score), false);
						Gdx.app.log("Score", String.valueOf(score));
					}
				}
				else{
					file.writeString(String.valueOf(score), false);
					Gdx.app.log("File", "created");
				}

				Gdx.app.log("Path", "" + file.path());

//				file.writeString("Hello", false);

//				String data = file.readString();
//				Gdx.app.log("Path", file.path());
//				Gdx.app.log("Data", data);

				valid = false;

				this.gameOverInterface.switchActivity();
			}




			if(Gdx.input.justTouched()){
				Gdx.app.log("Message", "Screen Touched 2 " + counter++ + " / " + yPosition);
				gameState = 1;

				yPosition = Gdx.graphics.getHeight() / 2 - bird[birdState].getHeight() / 2;
				velocity = 0;
				score = 0;
				scoringTube = 0;

				for(int i = 0; i < numberOfTubes; i++){
					tubeOffset[i] = (random.nextFloat() - 0.5f) * Gdx.graphics.getHeight() / 2;
					tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes;
					topRectangles[i] = new Rectangle();
					bottomRectangles[i] = new Rectangle();
				}

			}
		}

		if(birdState == 0){
			birdState = 1;
		}else{
			birdState = 0;
		}


//		batch.draw(topTube, Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2, topTube.getHeight() - (Gdx.graphics.getHeight() / 2 - 0)); // 0 - 400
//		batch.draw(bottomTube, Gdx.graphics.getWidth() / 2 - bottomTube.getWidth() / 2,- bottomTube.getHeight() + (Gdx.graphics.getHeight() / 2 - 0));


		batch.draw(bird[birdState], Gdx.graphics.getWidth() / 2 - bird[birdState].getWidth() / 2, yPosition);
		font.draw(batch, "S:" + String.valueOf(score), 50, 200);

		if(file.exists()) highscoreFont.draw(batch, "H:" + file.readString(), 210, 200);
		else highscoreFont.draw(batch, "H:0", 210, 200);



//		batch.draw(animation.getKeyFrame(elapsedTime, true), Gdx.graphics.getWidth() / 2 - birdWidth / 2, Gdx.graphics.getHeight() / 2 - birdHeight / 2, birdWidth, birdHeight);
		batch.end();

		circle.set(Gdx.graphics.getWidth() / 2, yPosition + bird[birdState].getHeight() / 2, bird[birdState].getWidth() / 2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);


//		shapeRenderer.circle(circle.x, circle.y, circle.radius);

//		shapeRenderer.end();

//		tubeX -= 4;
//		if(tubeX < - topTube.getWidth()){
//			tubeX = Gdx.graphics.getWidth();
//		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(GAME_WORLD_WIDTH / 2, GAME_WORLD_HEIGHT / 2, 0);
	}
}
