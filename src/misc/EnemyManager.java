package misc;

import game_object.Birds;
import game_object.Cactuses;
import user_interface.GameScreen;

import java.awt.Graphics;
import java.awt.Rectangle;

public class EnemyManager {
	
	// value by which chance of creating new enemy increasing
	private static final double PERCENTAGE_INC = 0.0001;
	// distance between enemies
	private static final double DISTANCE_DESC = -0.005;
	// min distance
	private static final int MINIMUM_DISTANCE = 250;
	
	// number of possible enemies (cactus, birds)
	private static final int ENEMY_TYPES = 2;
	
	// starting distance between enemies
	private double DISTANCE_BETWEEN_ENEMIES = 750;
	// staring chance of creating new enemy
	private double CACTUSES_PERCENTAGE = 2;
	private double BIRDS_PERCENTAGE = 1;
	
	private Cactuses cactuses;
	private Birds birds;
	
	public EnemyManager(GameScreen gameScreen) {
		cactuses = new Cactuses(gameScreen, this);
		birds = new Birds(gameScreen, this);
	}
	
	public double getDISTANCE_BETWEEN_ENEMIES() {
		return DISTANCE_BETWEEN_ENEMIES;
	}

	public double getCACTUSES_PERCENTAGE() {
		return CACTUSES_PERCENTAGE;
	}

	public double getBIRDS_PERCENTAGE() {
		return BIRDS_PERCENTAGE;
	}

	public void updatePosition() {
		CACTUSES_PERCENTAGE += PERCENTAGE_INC;
		BIRDS_PERCENTAGE += PERCENTAGE_INC;
		if(DISTANCE_BETWEEN_ENEMIES > MINIMUM_DISTANCE)
			DISTANCE_BETWEEN_ENEMIES += DISTANCE_DESC;
//		System.out.println(DISTANCE_BETWEEN_ENEMIES);
		cactuses.updatePosition();
		birds.updatePosition();
		if(cactuses.spaceAvailable() && birds.spaceAvailable()) {
			// "randomly" choosing new enemy type 
			switch ((int)(Math.random() * ENEMY_TYPES)) {
			case 0:
				if(cactuses.createCactuses())
					break;
			case 1:
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
	
	public void clearEnemies() {
		cactuses.clearCactuses();
		birds.clearBirds();
	}
	
	public void draw(Graphics g) {
		cactuses.draw(g);
		birds.draw(g);
	}
	
	public void drawHitBoxes(Graphics g) {
		cactuses.drawHitBox(g);
		birds.drawHitBox(g);
	}
	
}
