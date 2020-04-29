package game_object;

import static user_interface.GameScreen.GRAVITY;
import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameScreen.SPEED_Y;
import static util.Resource.getImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import manager.SoundManager;
import misc.Animation;
import misc.Controls;

public class Dino {
	
	// values to subtract from x, y, width, height to get accurate hitbox
	private static final int[] HITBOX_RUN = {12, 26, -32, -42};
	private static final int[] HITBOX_DOWN_RUN = {24, 8, -60, -24};
	
	public static final double X = 120;
	// states of dino
	public static final int DINO_RUN = 0;
	public static final int DINO_DOWN_RUN = 1;
	public static final int DINO_JUMP = 2;
	public static final int DINO_DEAD = 3;
	
	Controls controls;
	
	private double maxY;
	private double highJumpMaxY;
	private double lowJumpMaxY;
	
	private double y = 0;
	// jumping speed
	private double speedY = 0;
	private int dinoState;
	private BufferedImage dinoJump;
	private BufferedImage dinoDead;
	private Animation dinoRun;
	private Animation dinoDownRun;
	private SoundManager jumpSound;
	
	public Dino(Controls controls) {
		this.controls = controls;
		dinoRun = new Animation(150);
		dinoRun.addSprite(getImage("resources/dino-run-1.png"));
		dinoRun.addSprite(getImage("resources/dino-run-2.png"));
		dinoDownRun = new Animation(150);
		dinoDownRun.addSprite(getImage("resources/dino-down-run-1.png"));
		dinoDownRun.addSprite(getImage("resources/dino-down-run-2.png"));
		dinoJump = getImage("resources/dino-jump.png");
		dinoDead = getImage("resources/dino-dead.png");
		jumpSound = new SoundManager("resources/jump.wav");
		jumpSound.startThread();
		y = GROUND_Y - dinoJump.getHeight();
		maxY = y;
		highJumpMaxY = setJumpMaxY(GRAVITY);
		lowJumpMaxY = setJumpMaxY(GRAVITY + GRAVITY / 2);
		dinoState = DINO_JUMP;
	}
	
	public int getDinoState() {
		return dinoState;
	}

	public void setDinoState(int dinoState) {
		this.dinoState = dinoState;
	}
	
	public double setJumpMaxY(double gravity) {
		speedY = SPEED_Y;
		y += speedY;
		double jumpMaxY = y;
		while(true) {
			speedY += gravity;
			y += speedY;
			if(y < jumpMaxY)
				jumpMaxY = y;
			if(y + speedY >= GROUND_Y - dinoRun.getSprite().getHeight()) {
				speedY = 0;
				y = GROUND_Y - dinoRun.getSprite().getHeight();
				break;
			}
		}
		return jumpMaxY;
	}

	public Rectangle getHitBox() {
		switch (dinoState) {
		case DINO_RUN:
		case DINO_JUMP:
		case DINO_DEAD:
			return new Rectangle((int)X + HITBOX_RUN[0], (int)y + HITBOX_RUN[1], 
					dinoDead.getWidth() + HITBOX_RUN[2], dinoDead.getHeight() + HITBOX_RUN[3]);
		case DINO_DOWN_RUN:
			return new Rectangle((int)X + HITBOX_DOWN_RUN[0], (int)y + HITBOX_DOWN_RUN[1], 
					dinoDownRun.getSprite().getWidth() + HITBOX_DOWN_RUN[2], dinoDownRun.getSprite().getHeight() + HITBOX_DOWN_RUN[3]);
		}
		return null;
	}
	
	public void updatePosition() {
		if(y < maxY)
			maxY = y;
		dinoRun.updateSprite();
		dinoDownRun.updateSprite();
		switch (dinoState) {
		case DINO_RUN:
			y = GROUND_Y - dinoRun.getSprite().getHeight();
			maxY = y;
			break;
		case DINO_DOWN_RUN:
			y = GROUND_Y - dinoDownRun.getSprite().getHeight();
			break;
		case DINO_JUMP:
			if(y + speedY >= GROUND_Y - dinoRun.getSprite().getHeight()) {
				speedY = 0;
				y = GROUND_Y - dinoRun.getSprite().getHeight();
				dinoState = DINO_RUN;
			} else if(controls.isPressedUp()) {
				speedY += GRAVITY;
				y += speedY;
			} else {
				if(maxY <= lowJumpMaxY - (lowJumpMaxY - highJumpMaxY) / 2)
					speedY += GRAVITY;
				else
					speedY += GRAVITY + GRAVITY / 2;
				if(controls.isPressedDown())
					speedY += GRAVITY;
				y += speedY;
			}
			break;
		default:
			break;
		}
		
	}
	
	public void jump() {
		if(y == GROUND_Y - dinoRun.getSprite().getHeight()) {
			jumpSound.play();
			speedY = SPEED_Y;
			y += speedY;
		}
	}
	
	public void resetDino() {
		y = GROUND_Y - dinoJump.getHeight();
		dinoState = DINO_RUN;
	}
	
	public void dinoGameOver() {
		if(y > GROUND_Y - dinoDead.getHeight())
			y = GROUND_Y - dinoDead.getHeight();
		dinoState = DINO_DEAD;
	}
	
	public void draw(Graphics g) {
		switch (dinoState) {
		case DINO_RUN:
			g.drawImage(dinoRun.getSprite(), (int)X, (int)y, null);
			break;
		case DINO_DOWN_RUN:
			g.drawImage(dinoDownRun.getSprite(), (int)X, (int)y, null);
			break;
		case DINO_JUMP:
			g.drawImage(dinoJump, (int)X, (int)y, null);
			break;
		case DINO_DEAD:
			g.drawImage(dinoDead, (int)X, (int)y, null);
			break;
		default:
			break;
		}
	}
	
	public void drawHitBox(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawRect(getHitBox().x, getHitBox().y, getHitBox().width, getHitBox().height);
	}
	
}
