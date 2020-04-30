package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Resource {
	
	public static BufferedImage getImage(String path) {
		File file = new File(path);
		BufferedImage image = null;
		try {
			// ClassLoader here is because i just made jar file of this game so i can access all sprites
			if(file.exists())
				image = ImageIO.read(file);
			else {
				path = path.substring(path.indexOf("/") + 1);
				image = ImageIO.read(ClassLoader.getSystemClassLoader().getResource(path));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static Clip getSound(String path) {
		File file = new File(path);
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			// same situation why i have ClassLoader here
			if(file.exists())
				clip.open(AudioSystem.getAudioInputStream(file));
			else {
				path = path.substring(path.indexOf("/") + 1);
				clip.open(AudioSystem.getAudioInputStream(ClassLoader.getSystemClassLoader().getResource(path)));
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return clip;
	}
	
	public static boolean isJar() {
		Matcher m = Pattern.compile("^file:").matcher(ClassLoader.getSystemClassLoader().getResource("").toString());
		return !m.find();
	}
	
}
