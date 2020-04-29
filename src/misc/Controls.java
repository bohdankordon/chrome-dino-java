package misc;

import user_interface.GameScreen;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

@SuppressWarnings(value = { "serial" })
public class Controls {

	private static final int FOCUS_STATE = JComponent.WHEN_IN_FOCUSED_WINDOW;
	
	private static final String UP = "UP";
	private static final String DOWN = "DOWN";
	private static final String W_UP = "W_UP";
	private static final String S_DOWN = "S_DOWN";
	private static final String SPACE_UP = "SPACE_UP";
	private static final String DEBUG_MENU = "DEBUG_MENU";
	private static final String P_PAUSE = "P";
	private static final String ESCAPE_PAUSE = "ESCAPE";
	
	private static final String RELEASED_UP = "RELEASED_UP";
	private static final String RELEASED_DOWN = "RELEASED_DOWN";
	private static final String RELEASED_W_UP = "RELEASED_W_UP";
	private static final String RELEASED_S_DOWN = "RELEASED_S_DOWN";
	private static final String RELEASED_SPACE_UP = "RELEASED_SPACE_UP";
	
	public JLabel pressUp = new JLabel();
	public JLabel releaseUp = new JLabel();
	public JLabel pressDown = new JLabel();
	public JLabel releaseDown = new JLabel();
	public JLabel pressDebug = new JLabel();
	public JLabel pressPause = new JLabel();
	
	private boolean isPressedUp = false;
	private boolean isPressedDown = false;
	
	GameScreen gameScreen;
	
	public Controls(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		// PRESS RELEASE ARROW UP //
		pressUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("UP"), UP);
		pressUp.getActionMap().put(UP, new PressUpAction());
		releaseUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released UP"), RELEASED_UP);
		releaseUp.getActionMap().put(RELEASED_UP, new ReleaseUpAction());
		// PRESS RELEASE ARROW DOWN //
		pressDown.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("DOWN"), DOWN);
		pressDown.getActionMap().put(DOWN, new PressDownAction());
		releaseDown.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released DOWN"), RELEASED_DOWN);
		releaseDown.getActionMap().put(RELEASED_DOWN, new ReleaseDownAction());
		// PRESS RELEASE W //
		pressUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("W"), W_UP);
		pressUp.getActionMap().put(W_UP, new PressUpAction());
		releaseUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released W"), RELEASED_W_UP);
		releaseUp.getActionMap().put(RELEASED_W_UP, new ReleaseUpAction());
		// PRESS RELEASE S //
		pressDown.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("S"), S_DOWN);
		pressDown.getActionMap().put(S_DOWN, new PressDownAction());
		releaseDown.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released S"), RELEASED_S_DOWN);
		releaseDown.getActionMap().put(RELEASED_S_DOWN, new ReleaseDownAction());
		// PRESS RELEASE SPACE //
		pressUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("SPACE"), SPACE_UP);
		pressUp.getActionMap().put(SPACE_UP, new PressUpAction());
		releaseUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released SPACE"), RELEASED_SPACE_UP);
		releaseUp.getActionMap().put(RELEASED_SPACE_UP, new ReleaseUpAction());
		// PRESS RELEASE BACKTICK //
		pressDebug.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("BACK_QUOTE"), DEBUG_MENU);
		pressDebug.getActionMap().put(DEBUG_MENU, new PressDebugAction());
		// PRESS RELEASE P //
		pressPause.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("P"), P_PAUSE);
		pressPause.getActionMap().put(P_PAUSE, new PressPauseAction());
		// PRESS RELEASE ESCAPE //
		pressPause.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("ESCAPE"), ESCAPE_PAUSE);
		pressPause.getActionMap().put(ESCAPE_PAUSE, new PressPauseAction());
	}
	
	public boolean isPressedUp() {
		return isPressedUp;
	}

	public boolean isPressedDown() {
		return isPressedDown;
	}

	private class PressUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("up");
			isPressedUp = true;
		}
	}
	
	private class ReleaseUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("released up");
			gameScreen.releaseUpAction();
			isPressedUp = false;
		}
	}
	
	private class PressDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("down");
			isPressedDown = true;
		}
	}
	
	private class ReleaseDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("released down");
			gameScreen.releaseDownAction();
			isPressedDown = false;
		}
	}
	
	private class PressDebugAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("press debug");
			gameScreen.pressDebugAction();
		}
	}
	
	private class PressPauseAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("press pause");
			gameScreen.pressPauseAction();
		}
	}
	
}
