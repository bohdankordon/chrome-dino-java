package game_object;

import user_interface.GameScreen;

import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import manager.EnemyManager;

public class Cactuses {
	
	private class Cactus {
		
		private BufferedImage cactusImage;
		private double x;
		private int y;
		
		private Cactus(BufferedImage cactusImage, double x, int y) {
			this.cactusImage = cactusImage;
			this.x = x;
			this.y = y;
		}
		
	}
	
	// number to calculate hitbox of cactuses
	private static final double HITBOX_X = 2.7;
	private static final int HITBOX_Y = 25;
	// number of cactus sprites
	private static final int CACTUSES_AMOUNT = 9;
	// max number of cactuses grouped
	private static final int MAX_CACTUS_GROUP = 3;
	
	private EnemyManager eManager;
	private GameScreen gameScreen;
	private List<Cactus> cactuses;
	
	public Cactuses(GameScreen gameScreen, EnemyManager eManager) {
		this.eManager = eManager;
		this.gameScreen = gameScreen;
		cactuses = new ArrayList<Cactus>();
	}
	
	public void updatePosition() {
		for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			cactus.x += Math.round(gameScreen.getSpeedX() * 100d) / 100d;
			if((int)cactus.x + cactus.cactusImage.getWidth() < 0) {
				i.remove();
			}
		}
	}
	
	public boolean spaceAvailable() {
		for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			if(SCREEN_WIDTH - (cactus.x + cactus.cactusImage.getWidth()) < eManager.getDistanceBetweenEnemies()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean createCactuses() {
		if(Math.random() * 100 < eManager.getCactusesPercentage()) {
			// Math random to get number of cactuses in a group
			for(int i = 0, numberOfCactuses = (int)(Math.random() * MAX_CACTUS_GROUP + 1); i < numberOfCactuses; i++) {
				BufferedImage cactusImage = getImage("resources/cactus-" + (int)(Math.random() * CACTUSES_AMOUNT + 1) + ".png");
				int x = SCREEN_WIDTH;
				int y = GROUND_Y - cactusImage.getHeight();
				// if it is first cactus of this group x is SCREEN_WIDTH
				// if it is second or third than i take last cactus and its width to calculate x position
				if(i > 0)
					x = (int)cactuses.get(cactuses.size() - 1).x + cactuses.get(cactuses.size() - 1).cactusImage.getWidth();
				cactuses.add(new Cactus(cactusImage, x, y));
			}
			return true;
		}
		return false;
	}
	
	public boolean isCollision(Rectangle dinoHitBox) {
		for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			Rectangle cactusHitBox = getHitbox(cactus);
			if(cactusHitBox.intersects(dinoHitBox))
				return true;
		}
		return false;
	}
	
	private Rectangle getHitbox(Cactus cactus) {
		// weird calculation by its working as needed
		// basically i make it thinner from left and right and shorter to match it perfectly
		// enable hitboxes in GameScreen to see it
		return new Rectangle((int)cactus.x + (int)(cactus.cactusImage.getWidth() / HITBOX_X), 
				cactus.y + cactus.cactusImage.getHeight() / HITBOX_Y, 
				cactus.cactusImage.getWidth() - (int)(cactus.cactusImage.getWidth() / HITBOX_X) * 2, 
				cactus.cactusImage.getHeight() - cactus.cactusImage.getHeight() / HITBOX_Y);
	}
	
	public void clearCactuses() {
		cactuses.clear();
	}
	
	public void draw(Graphics g) {
		for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			g.drawImage(cactus.cactusImage, (int)(cactus.x), cactus.y, null);
		}
	}
	
	public void drawHitbox(Graphics g) {
		g.setColor(Color.RED);
		for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			Rectangle cactusHitBox = getHitbox(cactus);
			g.drawRect(cactusHitBox.x, cactusHitBox.y, (int)cactusHitBox.getWidth(), (int)cactusHitBox.getHeight());
		}
	}
	
}
