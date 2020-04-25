package user_interface;

import javax.swing.JFrame;

@SuppressWarnings(value = { "serial" })
public class GameWindow extends JFrame {
	
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 300;

	private GameScreen gameScreen;
	
	public GameWindow() {
		super("Dino");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		gameScreen = new GameScreen();
		add(gameScreen);
	}
	
	private void startGame() {
		gameScreen.startThread();
	}
	
	public static void main(String[] args) {
		GameWindow gameWindow = new GameWindow();
		gameWindow.startGame();
		gameWindow.setVisible(true);
	}
	
}
