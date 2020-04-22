package misc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {
	
	private List<BufferedImage> sprites;
	private int currentSpriteIndex = 0;
	private int updateTime;
	private long lastUpdateTime = 0;
	
	public Animation(int updateTime) {
		this.updateTime = updateTime;
		sprites = new ArrayList<BufferedImage>();
	}
	
	// updating sprite every set milliseconds
	public void updateSprite() {
		if(System.currentTimeMillis() - lastUpdateTime >= updateTime) {
			currentSpriteIndex++;
			if(currentSpriteIndex >= sprites.size())
				currentSpriteIndex = 0;
			lastUpdateTime = System.currentTimeMillis();
		}
	}
	
	public void addSprite(BufferedImage sprite) {
		sprites.add(sprite);
	}
	
	public BufferedImage getSprite() {
		if(sprites.size() > 0) {
			return sprites.get(currentSpriteIndex);
		}
		return null;
	}
	
}
