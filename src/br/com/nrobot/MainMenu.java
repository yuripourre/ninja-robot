package br.com.nrobot;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
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
		
	public MainMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
				
		background = new ImageLayer("background.png");
		
		logo = new ImageLayer(0, 70, "logo.png");
		logo.centralizeX(background);
		
		playButton = new NRButton(232, 300, "play.png");
		
		creditsButton = new NRButton(232, 400, "credits.png");
		
		Config config = ConfigLoader.loadConfiguration();
		session.put(PARAM_CONFIG, config);
		
		loading = 100;
	}
	
	@Override
	public void draw(Graphic g) {
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
				//nextApplication = new Game(w, h);
				nextApplication = new RoomMenu(w, h);
			}
			
			if(creditsButton.isOnMouse()) {
				nextApplication = new Credits(w, h);
			}
			
		}
	}

}
