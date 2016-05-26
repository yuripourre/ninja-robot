package br.com.nrobot;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.config.ConfigLoader;
import br.com.nrobot.ui.NRButton;

public class MainMenu extends Application {

	private ImageLayer background;
	
	private ImageLayer logo;
	
	private NRButton playButton;
	private NRButton creditsButton;
	
	public static final String PARAM_CONFIG = "config";
	public static final String PARAM_CLIENT = "client";
	public static final String PARAM_GAME = "game";
	public static final String PARAM_MODE = "mode";
		
	public MainMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
				
		loading = 10;
		background = new ImageLayer("background.png");
		
		loading = 20;
		
		logo = new ImageLayer(0, 70, "logo.png");
		logo.centralizeX(background);
		
		loading = 50;
		playButton = new NRButton(232, 300, "play.png");
		
		creditsButton = new NRButton(232, 400, "credits.png");
		
		loading = 60;
		
		Config config = ConfigLoader.loadConfiguration();
		session.put(PARAM_CONFIG, config);
		
		loading = 100;
	}
	
	@Override
	public void draw(Graphics g) {
		//Fix in etyllica
		if (loading < 100)
			return;
		
		background.draw(g);
		
		logo.draw(g);
		
		playButton.draw(g);
		creditsButton.draw(g);
	}
	
	public void updateMouse(PointerEvent event) {
		
		playButton.handleEvent(event);
		creditsButton.handleEvent(event);
		
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
			
			if(playButton.isOnMouse()) {
				nextApplication = new GameModeMenu(w, h);
			}
			
			if(creditsButton.isOnMouse()) {
				nextApplication = new Credits(w, h);
			}
			
		}
	}

}
