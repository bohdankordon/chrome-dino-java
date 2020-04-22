package user_interface;

import static game_object.Dino.DINO_DOWN_RUN;
import static game_object.Dino.DINO_JUMP;
import static game_object.Dino.DINO_RUN;
import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import game_object.Clouds;
import game_object.Dino;
import game_object.Land;
import game_object.Score;
import misc.EnemyManager;
import misc.SoundManager;

@SuppressWarnings(value = { "serial" })
public class GameScreen extends JPanel implements Runnable, KeyListener {

	private Thread thread;
	
	private static final int GAME_STATE_START = 0;
	private static final int GAME_STATE_IN_PROGRESS = 1;
	public static final int GAME_STATE_OVER = 2;
	
	private static final int STARTING_SPEED_X = -5;
	// speed is constantly increasing by that value
	private static final double DIFFICULTY_INC = -0.0002;
	public static final double GRAVITY = 0.5;
	public static final int GROUND_Y = 280;
	// speed of dino jumping
	public static final double SPEED_Y = -12.5;
	
	private double SPEED_X = STARTING_SPEED_X;
	private int GAME_STATE = GAME_STATE_START;
	
	// hitboxes of dino, enemies and ground
	private boolean SHOW_HITBOXES;
	private boolean COLLISIONS;
	
	// values to calculate waiting time at line 83
	private final int FPS = 100;
	private final int NS_PER_FRAME = 1_000_000_000 / FPS;
	
	private Score score;
	private Dino dino;
	private Land land;
	private Clouds clouds;
	private EnemyManager eManager;
	private SoundManager gameOverSound;
	
	public GameScreen() {
		thread = new Thread(this);
		score = new Score(this);
		dino = new Dino();
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
		// previous frame time to calculate how much time to wait until showing the next frame
		long prevFrameTime = System.nanoTime();
		int waitingTime = 0;
		while(true) {
			updateFrame();
			repaint();
			waitingTime = (int)((NS_PER_FRAME - (System.nanoTime() - prevFrameTime)) / 1_000_000);
			if(waitingTime < 0)
				waitingTime = 1;
			SoundManager.WAITING_TIME = waitingTime;
//			System.out.println(SPEED_X);
			// little pause to not start new game if you are spamming your keys
			if(GAME_STATE == GAME_STATE_OVER)
				waitingTime = 1000;
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			prevFrameTime = System.nanoTime();
		}
	}
	
	public double getSPEED_X() {
		return SPEED_X;
	}

	public int getGAME_STATE() {
		return GAME_STATE;
	}

	public void setSHOW_HITBOXES(boolean SHOW_HITBOXES) {
		this.SHOW_HITBOXES = SHOW_HITBOXES;
	}

	public void setCOLLISIONS(boolean COLLISIONS) {
		this.COLLISIONS = COLLISIONS;
	}

	// update all entities positions
	private void updateFrame() {
		switch (GAME_STATE) {
		case GAME_STATE_IN_PROGRESS:
			SPEED_X += DIFFICULTY_INC;
			dino.updatePosition();
			land.updatePosition();
			clouds.updatePosition();
			eManager.updatePosition();
			if(COLLISIONS && eManager.isCollision(dino.getHitBox())) {
				GAME_STATE = GAME_STATE_OVER;
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
		switch (GAME_STATE) {
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
	
	private void drawHitBoxes(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine(0, GROUND_Y, getWidth(), GROUND_Y);
//		clouds.drawHitBox(g);
		dino.drawHitBox(g);
		eManager.drawHitBoxes(g);
	}
	
	private void startScreen(Graphics g) {
		land.draw(g);
		dino.draw(g);
//		g.setColor(new Color(246, 246, 246));
//		g.fillRect((int)(Dino.X + dino.dinoJump.getWidth() + SCREEN_WIDTH / 50), 0, SCREEN_WIDTH, SCREEN_HEIGHT);
//		g.fillRect(0, 0, (int)(Dino.X - SCREEN_WIDTH / 50), SCREEN_HEIGHT);
	}
	
	private void inProgressScreen(Graphics g) {
		clouds.draw(g);
		land.draw(g);
		eManager.draw(g);
		dino.draw(g);
		score.draw(g);
		if(SHOW_HITBOXES)
			drawHitBoxes(g);
	}
	
	private void gameOverScreen(Graphics g) {
		inProgressScreen(g);
		BufferedImage gameOverImage = getImage("resources/game-over.png");
		BufferedImage replayImage = getImage("resources/replay.png");
		g.drawImage(gameOverImage, SCREEN_WIDTH / 2 - gameOverImage.getWidth() / 2, SCREEN_HEIGHT / 2 - gameOverImage.getHeight() * 2, null);
		g.drawImage(replayImage, SCREEN_WIDTH / 2 - replayImage.getWidth() / 2, SCREEN_HEIGHT / 2, null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println("Key Typed");
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println("Key Pressed");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			if(GAME_STATE == GAME_STATE_IN_PROGRESS) {
				dino.jump();
				dino.setDinoState(DINO_JUMP);
			}
			if(GAME_STATE == GAME_STATE_OVER) {
				SPEED_X = STARTING_SPEED_X;
				score.scoreReset();
				eManager.clearEnemies();
				dino.resetDino();
				clouds.clearClouds();
				land.resetLand();
				GAME_STATE = GAME_STATE_IN_PROGRESS;
			}
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			if(dino.getDinoState() != DINO_JUMP && GAME_STATE == GAME_STATE_IN_PROGRESS)
				dino.setDinoState(DINO_DOWN_RUN);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
//		System.out.println("Key Released");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			if(GAME_STATE == GAME_STATE_START)
				GAME_STATE = GAME_STATE_IN_PROGRESS;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			if(dino.getDinoState() != DINO_JUMP && GAME_STATE == GAME_STATE_IN_PROGRESS)
				dino.setDinoState(DINO_RUN);
			break;
		default:
			break;
		}
	}
	
}
