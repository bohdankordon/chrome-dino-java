package user_interface;

import misc.Controls;

import javax.swing.JFrame;

@SuppressWarnings(value = { "serial" })
public class GameWindow extends JFrame {
	
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 300;

	private GameScreen gameScreen;
	private Controls controls;
	
	public GameWindow() {
		super("Dino");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		gameScreen = new GameScreen();
		controls = new Controls(gameScreen);
		add(controls.pressUp);
		add(controls.releaseUp);
		add(controls.pressDown);
		add(controls.releaseDown);
		add(controls.pressDebug);
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
