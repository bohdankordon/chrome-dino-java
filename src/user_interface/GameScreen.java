package user_interface;

import static game_object.Dino.DINO_DOWN_RUN;
import static game_object.Dino.DINO_JUMP;
import static game_object.Dino.DINO_RUN;
import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import game_object.Clouds;
import game_object.Dino;
import game_object.Land;
import game_object.Score;
import manager.ControlsManager;
import manager.EnemyManager;
import manager.SoundManager;
import misc.Controls;

@SuppressWarnings(value = { "serial" })
public class GameScreen extends JPanel implements Runnable {

	private Thread thread;
	
	private static final int STARTING_SPEED_X = -5;
	private static final double DIFFICULTY_INC = -0.0002;
	
	public static final int GAME_STATE_START = 0;
	public static final int GAME_STATE_IN_PROGRESS = 1;
	public static final int GAME_STATE_OVER = 2;
	public static final double GRAVITY = 0.4;
	public static final int GROUND_Y = 280;
	public static final double SPEED_Y = -12;
	
	private double speedX = STARTING_SPEED_X;
	private int gameState = GAME_STATE_START;
	
	private boolean showHitboxes = false;
	private boolean collisions = true;
	
	private final int FPS = 100;
	private final int NS_PER_FRAME = 1_000_000_000 / FPS;
	
	private Controls controls;
	private Score score;
	private Dino dino;
	private Land land;
	private Clouds clouds;
	private EnemyManager eManager;
	private SoundManager gameOverSound;
	private ControlsManager cManager;;
	
	public GameScreen() {
		thread = new Thread(this);
		controls = new Controls(this);
		super.add(controls.pressUp);
		super.add(controls.releaseUp);
		super.add(controls.pressDown);
		super.add(controls.releaseDown);
		super.add(controls.pressDebug);
//		super.add(controls.releaseDebug);
		cManager = new ControlsManager(controls, this);
		score = new Score(this);
		dino = new Dino(controls);
		land = new Land(this);
		clouds = new Clouds(this);
		eManager = new EnemyManager(this);
		gameOverSound = new SoundManager("resources/dead.wav");
		gameOverSound.startThread();
	}
	
	public void startThread() {
		thread.start();
	}
	
	@Override
	public void run() {
		long prevFrameTime = System.nanoTime();
		int waitingTime = 0;
		while(true) {
			cManager.update();
			updateFrame();
			repaint();
			waitingTime = (int)((NS_PER_FRAME - (System.nanoTime() - prevFrameTime)) / 1_000_000);
			if(waitingTime < 0)
				waitingTime = 1;
			SoundManager.WAITING_TIME = waitingTime;
			// little pause to not start new game if you are spamming your keys
			if(gameState == GAME_STATE_OVER)
				waitingTime = 1000;
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			prevFrameTime = System.nanoTime();
		}
	}
	
	public double getSpeedX() {
		return speedX;
	}

	public int getGameState() {
		return gameState;
	}

	// update all entities positions
	private void updateFrame() {
		switch (gameState) {
		case GAME_STATE_IN_PROGRESS:
			speedX += DIFFICULTY_INC;
			dino.updatePosition();
			land.updatePosition();
			clouds.updatePosition();
			eManager.updatePosition();
			if(collisions && eManager.isCollision(dino.getHitbox())) {
				gameState = GAME_STATE_OVER;
				dino.dinoGameOver();
				score.writeScore();
				gameOverSound.play();
			}
			score.scoreUp();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(246, 246, 246));
		g.fillRect(0, 0, getWidth(), getHeight());
		switch (gameState) {
		case GAME_STATE_START:
			startScreen(g);
			break;
		case GAME_STATE_IN_PROGRESS:
			inProgressScreen(g);
			break;
		case GAME_STATE_OVER:
			gameOverScreen(g);
			break;
		default:
			break;
		}
	}
	
	private void drawDebugMenu(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine(0, GROUND_Y, getWidth(), GROUND_Y);
//		clouds.drawHitBox(g);
		dino.drawHitbox(g);
		eManager.drawHitboxes(g);
		String speedInfo = "SPEED_X: " + String.valueOf(Math.round(speedX * 1000D) / 1000D);
		g.drawString(speedInfo, (int)(SCREEN_WIDTH / 100), (int)(SCREEN_HEIGHT / 25));
	}
	
	private void startScreen(Graphics g) {
		land.draw(g);
		dino.draw(g);
	}
	
	private void inProgressScreen(Graphics g) {
		clouds.draw(g);
		land.draw(g);
		eManager.draw(g);
		dino.draw(g);
		score.draw(g);
		if(showHitboxes)
			drawDebugMenu(g);
	}
	
	private void gameOverScreen(Graphics g) {
		inProgressScreen(g);
		BufferedImage gameOverImage = getImage("resources/game-over.png");
		BufferedImage replayImage = getImage("resources/replay.png");
		g.drawImage(gameOverImage, SCREEN_WIDTH / 2 - gameOverImage.getWidth() / 2, SCREEN_HEIGHT / 2 - gameOverImage.getHeight() * 2, null);
		g.drawImage(replayImage, SCREEN_WIDTH / 2 - replayImage.getWidth() / 2, SCREEN_HEIGHT / 2, null);
	}
	
	public void pressUpAction() {
		if(gameState == GAME_STATE_IN_PROGRESS) {
			dino.jump();
			dino.setDinoState(DINO_JUMP);
		}
	}
	
	public void releaseUpAction() {
		if(gameState == GAME_STATE_START)
			gameState = GAME_STATE_IN_PROGRESS;
		if(gameState == GAME_STATE_OVER) {
			speedX = STARTING_SPEED_X;
			score.scoreReset();
			eManager.clearEnemies();
			dino.resetDino();
			clouds.clearClouds();
			land.resetLand();
			gameState = GAME_STATE_IN_PROGRESS;
		}
	}
	
	public void pressDownAction() {
		if(dino.getDinoState() != DINO_JUMP && gameState == GAME_STATE_IN_PROGRESS)
			dino.setDinoState(DINO_DOWN_RUN);
	}
	
	public void releaseDownAction() {
		if(dino.getDinoState() != DINO_JUMP && gameState == GAME_STATE_IN_PROGRESS)
			dino.setDinoState(DINO_RUN);
	}
	
	public void pressDebugAction() {
		if(showHitboxes == false)
			showHitboxes = true;
		else
			showHitboxes = false;
		if(collisions == true)
			collisions = false;
		else
			collisions = true;
	}
	
}
