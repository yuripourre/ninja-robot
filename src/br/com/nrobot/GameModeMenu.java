package br.com.nrobot;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.ui.NRButton;

public class GameModeMenu extends Application {
	
	private ImageLayer background;
	private ImageLayer logo;
	
	private NRButton battleButton;
	private NRButton storyButton;
		
	public GameModeMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		background = new ImageLayer("background.png");
		
		logo = new ImageLayer(0, 70, "logo.png");
		logo.centralizeX(background);
		
		battleButton = new NRButton(232, 300, "battle_mode.png");
		
		storyButton = new NRButton(232, 400, "story_mode.png");
		
		loading = 100;
	}
	
	@Override
	public void draw(Graphics g) {
		background.draw(g);
		
		logo.draw(g);
		
		storyButton.draw(g);
		battleButton.draw(g);
	}
	
	public void updateMouse(PointerEvent event) {
		
		battleButton.handleEvent(event);
		storyButton.handleEvent(event);
		
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
			
			if(storyButton.isOnMouse()) {
				session.put(MainMenu.PARAM_MODE, GameMode.STORY);
				nextApplication = new RoomMenu(w, h);
			}
			
			if(battleButton.isOnMouse()) {
				session.put(MainMenu.PARAM_MODE, GameMode.BATTLE);
				nextApplication = new RoomMenu(w, h);
			}
		}
	}
	
	@Override
	public void updateKeyboard(KeyEvent event) {
		if (event.isKeyDown(KeyEvent.VK_ESC)) {
			nextApplication = new MainMenu(w, h);
		}
	}

}
