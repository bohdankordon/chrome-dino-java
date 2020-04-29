package game_object;

import user_interface.GameScreen;

import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Clouds {
	
	private class Cloud {
		
		private BufferedImage cloudImage;
		private double x;
		private int y;
		
		private Cloud(BufferedImage cloudImage, double x, int y) {
			this.cloudImage = cloudImage;
			this.x = x;
			this.y = y;
		}
		
	}
	
	// number of max clouds on screen
	private static final int CLOUDS_AMOUNT = 5;
	// chance of getting cloud
	private static final double CLOUD_PERCENTAGE = 0.4;
	
	private GameScreen gameScreen;
	private Set<Cloud> clouds;
	// made clouds 2x bigger 
	private int cloudWidthScaled;
	private int cloudHeightScaled;
	
	public Clouds(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		clouds = new HashSet<Cloud>();
		cloudWidthScaled = getImage("resources/cloud.png").getWidth() * 2;
		cloudHeightScaled = getImage("resources/cloud.png").getHeight() * 2;
		
	}
	
	public void updatePosition() {
		isOutOfScreen();
		createClouds();
	}
	
	private void isOutOfScreen() {
		for(Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
			Cloud cloud = (Cloud)i.next();
			cloud.x += gameScreen.getSpeedX() / 7;
			if(cloud.x + cloudWidthScaled < 0) {
				i.remove();
			}
		}
	}
	
	private void createClouds() {
		if(clouds.size() < CLOUDS_AMOUNT) {
			for(Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
				Cloud temp = (Cloud)i.next();
				// checking if enough space for next cloud 
				if(temp.x >= SCREEN_WIDTH - cloudWidthScaled)
					return;
			}
			if(Math.random() * 100 < CLOUD_PERCENTAGE)
				clouds.add(new Cloud(getImage("resources/cloud.png"), SCREEN_WIDTH, (int)(Math.random() * (SCREEN_HEIGHT / 2))));
		}
	}
	
	public void clearClouds() {
		clouds.clear();
	}
	
	public void draw(Graphics g) {
		for(Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
			Cloud cloud = (Cloud)i.next();
			g.drawImage(cloud.cloudImage, (int)cloud.x, cloud.y, cloudWidthScaled, cloudHeightScaled, null);
		}
	}
	
	public void drawHitbox(Graphics g) {
		g.setColor(Color.GREEN);
		for(Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
			Cloud cloud = (Cloud)i.next();
			g.drawRect((int)cloud.x, cloud.y, cloudWidthScaled, cloudHeightScaled);
		}
	}
	
}
