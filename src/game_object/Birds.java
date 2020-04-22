package game_object;

import misc.Animation;
import misc.EnemyManager;
import user_interface.GameScreen;

import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Birds {
	
	// class for single bird
	private class Bird {
		
		private double x;
		private int y;
		private Animation birdFly;
		
		private Bird(double x, int y, Animation birdFly) {
			this.x = x;
			this.y = y;
			this.birdFly = birdFly;
		}
		
	}
	
	// again this weird numbers to calculate hitboxes
	// this on is difference in two sprites of birds, with wings up and down so that bird is not jumping as crazy, it could be done easier but...
	private static final int HITBOX_MODELS_DIFF_IN_Y = -12;
	// numbers to calculate hitboxes of bird with wings pointing up and down
	private static final int[] HITBOX_WINGS_UP = {20, 4, -40, -20};
	private static final int[] HITBOX_WINGS_DOWN = {20, 4, -40, -28};
	// value to check sprite at line 97 and 100
	private final int WINGS_DOWN_HEIGHT = getImage("resources/bird-fly-1.png").getHeight();
	
	private EnemyManager eManager;
	private GameScreen gameScreen;
	private List<Bird> birds;
	
	public Birds(GameScreen gameScreen, EnemyManager eManager) {
		this.eManager = eManager;
		this.gameScreen = gameScreen;
		birds = new ArrayList<Bird>();
		
	}
	
	public void updatePosition() {
		for(Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = i.next();
			// divided by 5 and summed to make clouds slower than land and other entities
			bird.x += (gameScreen.getSPEED_X() + gameScreen.getSPEED_X() / 5);
			bird.birdFly.updateSprite();
		}
	}
	
	public boolean spaceAvailable() {
		for(Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = i.next();
			if(SCREEN_WIDTH - (bird.x + bird.birdFly.getSprite().getWidth()) < eManager.getDISTANCE_BETWEEN_ENEMIES()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean createBird() { 
		if(Math.random() * 100 < eManager.getBIRDS_PERCENTAGE()) {
			Animation birdFly = new Animation(400);
			birdFly.addSprite(getImage("resources/bird-fly-1.png"));
			birdFly.addSprite(getImage("resources/bird-fly-2.png"));
			birds.add(new Bird(SCREEN_WIDTH, (int)(Math.random() * (GROUND_Y - birdFly.getSprite().getHeight())), birdFly));
			return true;
		}
		return false;
	}
	
	public boolean isCollision(Rectangle dinoHitBox) {
		for(Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = i.next();
			Rectangle birdHitBox = getHitBox(bird);
			if(birdHitBox.intersects(dinoHitBox))
				return true;
//				System.out.println("collision");
		}
		return false;
	}
	
	private Rectangle getHitBox(Bird bird) {
		// checking here which sprite is currently being used to calculate hitbox
		return new Rectangle((int)bird.x + HITBOX_WINGS_UP[0], 
				bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ? bird.y + HITBOX_WINGS_UP[1] : 
					bird.y + HITBOX_WINGS_DOWN[1],
				bird.birdFly.getSprite().getWidth() + HITBOX_WINGS_UP[2], 
				bird.birdFly.getSprite().getHeight() < WINGS_DOWN_HEIGHT ? bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_UP[3] : 
					 bird.birdFly.getSprite().getHeight() + HITBOX_WINGS_DOWN[3]);
	}
	
	public void clearBirds() {
		birds.clear();
	}
	
	public void draw(Graphics g) {
		for(Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = (Bird)i.next();
			// cheking here which sprite is currently being used to calc position
			g.drawImage(bird.birdFly.getSprite(), (int)bird.x, bird.birdFly.getSprite().getHeight() < 68 ? bird.y + HITBOX_MODELS_DIFF_IN_Y : bird.y, null);
		}
	}
	
	public void drawHitBox(Graphics g) {
		g.setColor(Color.RED);
		for(Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = (Bird)i.next();
			Rectangle birdHitBox = getHitBox(bird);
			g.drawRect(birdHitBox.x, birdHitBox.y, (int)birdHitBox.getWidth(), (int)birdHitBox.getHeight());
		}
	}
	
}
