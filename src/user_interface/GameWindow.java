package user_interface;
import javax.swing.JFrame;

@SuppressWarnings(value = { "serial" })
public class GameWindow extends JFrame {
	
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 300;
	
	private static boolean DEFAULT_SHOW_HITBOXES = false;
	private static boolean DEFAULT_COLLISIONS = true;
	
	private GameScreen gameScreen;
	
	public GameWindow(String arg1, String arg2) {
		super("Dino");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		gameScreen = new GameScreen();
		add(gameScreen);
		addKeyListener(gameScreen);
		
		if(arg1.equals("-showhitbox") || arg2.equals("-showhitbox"))
			gameScreen.setSHOW_HITBOXES(!DEFAULT_SHOW_HITBOXES);
		else
			gameScreen.setSHOW_HITBOXES(DEFAULT_SHOW_HITBOXES);
		if(arg1.equals("-nodeath") || arg2.equals("-nodeath"))
			gameScreen.setCOLLISIONS(!DEFAULT_COLLISIONS);
		else
			gameScreen.setCOLLISIONS(DEFAULT_COLLISIONS);
	}
	
	private void startGame() {
		gameScreen.startThread();
	}
	
	public static void main(String[] args) {
		String arg1 = "";
		String arg2 = "";
		if(args.length > 0)
			arg1 = args[0];
		if(args.length > 1)
			arg2 = args[1];
		GameWindow gameWindow = new GameWindow(arg1, arg2);
		gameWindow.startGame();
		gameWindow.setVisible(true);
	}
	
}
