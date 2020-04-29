package manager;

import game_object.Birds;
import game_object.Cactuses;
import misc.EnemyType;
import user_interface.GameScreen;

import java.awt.Graphics;
import java.awt.Rectangle;

public class EnemyManager {
	
	// value by which chance of creating new enemy increasing
	private static final double PERCENTAGE_INC = 0.0001;
	private static final double DISTANCE_DEC = -0.005;
	private static final int MINIMUM_DISTANCE = 250;
	
	private double distanceBetweenEnemies = 750;
	private double cactusesPercentage = 2;
	private double birdsPercentage = 1;
	
	private Cactuses cactuses;
	private Birds birds;
	
	public EnemyManager(GameScreen gameScreen) {
		cactuses = new Cactuses(gameScreen, this);
		birds = new Birds(gameScreen, this);
	}
	
	public double getDistanceBetweenEnemies() {
		return distanceBetweenEnemies;
	}

	public double getCactusesPercentage() {
		return cactusesPercentage;
	}

	public double getBirdsPercentage() {
		return birdsPercentage;
	}

	public void updatePosition() {
		cactusesPercentage += PERCENTAGE_INC;
		birdsPercentage += PERCENTAGE_INC;
		if(distanceBetweenEnemies > MINIMUM_DISTANCE)
			distanceBetweenEnemies += DISTANCE_DEC;
		cactuses.updatePosition();
		birds.updatePosition();
		if(cactuses.spaceAvailable() && birds.spaceAvailable()) {
			// "randomly" choosing new enemy type 
			switch (EnemyType.values()[(int)(Math.random() * EnemyType.values().length)]) {
			case CACTUS:
				if(cactuses.createCactuses())
					break;
			case BIRD:
				if(birds.createBird())
					break;
			default:
				cactuses.createCactuses();
				break;
			}
		}
	}
	
	public boolean isCollision(Rectangle hitBox) {
		if(cactuses.isCollision(hitBox) || birds.isCollision(hitBox))
			return true;
		return false;
	}
	
	public void clearEnemy() {
		cactuses.clearCactuses();
		birds.clearBirds();
	}
	
	public void draw(Graphics g) {
		cactuses.draw(g);
		birds.draw(g);
	}
	
	public void drawHitbox(Graphics g) {
		cactuses.drawHitbox(g);
		birds.drawHitbox(g);
	}
	
}
