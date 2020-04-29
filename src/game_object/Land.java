package game_object;

import user_interface.GameScreen;

import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Land {
	
	private GameScreen gameScreen;
	private double x = 0;
	private int y;
	// here i made land 2x times bigger
	private int landWidthScaled;
	private int landHeightScaled;
	private BufferedImage land;
	
	public Land(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		land = getImage("resources/land.png");
		y = SCREEN_HEIGHT - land.getHeight() * 2 - 4;
		landWidthScaled = land.getWidth() * 2;
		landHeightScaled = land.getHeight() * 2;
	}
	
	public void updatePosition() {
		// + SPEED_X to 2 decimal points
		x += Math.round(gameScreen.getSpeedX() * 100d) / 100d;
	}
	
	public void resetLand() {
		x = 0;
	}
	
	public void draw(Graphics g) {
		g.drawImage(land, (int)x, y, landWidthScaled, landHeightScaled, null);
		// drawing another land if image is ending
		if(landWidthScaled - SCREEN_WIDTH <= (int)Math.abs(x))
			g.drawImage(land, (int)(landWidthScaled + x), y, landWidthScaled, landHeightScaled, null);
		// if land out of screen set it to 0
		if(landWidthScaled <= (int)Math.abs(x))
			x = 0;
	}
	
}
