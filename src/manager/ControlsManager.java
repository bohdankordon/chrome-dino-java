package manager;

import misc.Controls;
import user_interface.GameScreen;

public class ControlsManager {

	Controls controls;
	GameScreen gameScreen;
	
	public ControlsManager(Controls controls, GameScreen gameScreen) {
		this.controls = controls;
		this.gameScreen = gameScreen;
	}
	
	public void update() {
		if(controls.isPressedUp())
			gameScreen.pressUpAction();
		if(controls.isPressedDown())
			gameScreen.pressDownAction();
	}
	
}
