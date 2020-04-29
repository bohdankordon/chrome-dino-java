package game_object;

import user_interface.GameScreen;

import static user_interface.GameScreen.GAME_STATE_OVER;
import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;
import static util.Resource.getImage;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manager.SoundManager;

public class Score {
	
	// value by which score is increasing
	private static final double SCORE_INC = 0.1;
	// length of score on screen, max 99999 but i dont think that anyone will play that long so.....
	private static final int SCORE_LENGTH = 5;
	// width and height of single number on sprite
	private static final int NUMBER_WIDTH = 20;
	private static final int NUMBER_HEIGHT = 21;
	// here i calculate position of score on screen
	private static final int CURRENT_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100);
	private static final int HI_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2;
	private static final int HI_X = SCREEN_WIDTH - ((SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2 + NUMBER_WIDTH * 2 + SCREEN_WIDTH / 100);
	private static final int SCORE_Y = SCREEN_HEIGHT / 25;
	
	private GameScreen gameScreen;
	private String scoreFileName;
	private File scoreFile;
	private BufferedImage hi;
	private BufferedImage numbers;
	private double score;
	private int hiScore;
	private SoundManager scoreUpSound;
	
	public Score(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		score = 0;
		scoreFileName = "best-scores.txt";
		scoreFile = new File("resources/" + scoreFileName);
		readScore();
		hi = getImage("resources/hi.png");
		numbers = getImage("resources/numbers.png");
		scoreUpSound = new SoundManager("resources/scoreup.wav");
		scoreUpSound.startThread();
	}
	
	public void scoreUp() {
		score += SCORE_INC;
		// play sound every 100 points
		if((int)score != 0 && score % 100 <= 0.1)
			scoreUpSound.play();
	}
	
	// getting single number from sprite
	private BufferedImage cropImage(BufferedImage image, int number) {
		return image.getSubimage(number * NUMBER_WIDTH, 0, NUMBER_WIDTH, NUMBER_HEIGHT);
	}
	
	private int[] scoreToArray(double scoreType) {
		int scoreArray[] = new int[SCORE_LENGTH];
		int tempScore = (int)scoreType;
		for(int i = 0; i < SCORE_LENGTH; i++) {
			int number = tempScore % 10;
			tempScore = (tempScore - number) / 10;
			scoreArray[i] = number;
		}
		return scoreArray;
	}
	
	public void writeScore() {
		if(score > hiScore) {
			File file;
			// here i check if program is running from jar file so that i know where to store best results
			// again because of that i use here ClassLoader
			if(isJar())
				file = new File(ClassLoader.getSystemClassLoader().getResource("").getPath() + scoreFileName);
			else
				file = scoreFile;
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
				// just format of results, storing here result, date, player, where player is just Dino because i dont have any friends.....
				bw.write(String.format("result=%s,date=%s,player=%s\n", Integer.toString((int)score), new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(Calendar.getInstance().getTime()), "Dino"));
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readScore() {
		// another ClassLoader to know from where to read best scores
		if(scoreFile.exists() || new File(ClassLoader.getSystemClassLoader().getResource("").getPath() + scoreFileName).exists()) {
			String line = "";
			File file;
			// again jar file check
			if(isJar())
				file = new File(ClassLoader.getSystemClassLoader().getResource("").getPath() + scoreFileName);
			else
				file = scoreFile;
			if(file.exists()) {				
				try(BufferedReader br =  new BufferedReader(new FileReader(file))) {
					while((line = br.readLine()) != null) {
						Matcher m = Pattern.compile("result=(\\d+),date=([\\d_]+),player=(\\w+)").matcher(line);
						if(m.find()) {
							if(Integer.parseInt(m.group(1)) > hiScore)
								hiScore = Integer.parseInt(m.group(1));
//						System.out.printf("result = %s date = %s player = %s\n", m.group(1), m.group(2), m.group(3));					
						}					
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			hiScore = (int)score;
	}
	
	private boolean isJar() {
		Matcher m = Pattern.compile("^file:").matcher(this.getClass().getResource("Score.class").toString());
		return !m.find();
	}
	
	public void scoreReset() {
		if(score > hiScore)
			hiScore = (int)score;
		score = 0;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		int scoreArray[] = scoreToArray(score);
		for(int i = 0; i < SCORE_LENGTH; i++) {
			// this if needed to make blinking animation when score increased by 100
			if((!((int)score >= 12 && (int)score % 100 <= 12) || (int)score % 3 == 0) || gameScreen.getGAME_STATE() == GAME_STATE_OVER)
				g2d.drawImage(cropImage(numbers, scoreArray[SCORE_LENGTH - i - 1]), CURRENT_SCORE_X + i * NUMBER_WIDTH, SCORE_Y, null);
		}
		// showing best score from read file or after game ended
		if(hiScore > 0) {
			int hiScoreArray[] = scoreToArray(hiScore);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			for(int i = 0; i < SCORE_LENGTH; i++) {
				g2d.drawImage(cropImage(numbers, hiScoreArray[SCORE_LENGTH - i - 1]), HI_SCORE_X + i * NUMBER_WIDTH, SCORE_Y, null);
			}
			g2d.drawImage(hi, HI_X, SCORE_Y, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
	
}
