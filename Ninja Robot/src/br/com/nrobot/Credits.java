package br.com.nrobot;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.input.mouse.MouseButton;
import br.com.etyllica.layer.ImageLayer;

public class Credits extends Application {

	private ImageLayer background;
	
	private ImageLayer credits;
	
	private ImageLayer names;
	
	public Credits(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
				
		background = new ImageLayer("background.png");
		
		credits = new ImageLayer(0, 80, "buttons/credits.png");
		credits.centralizeX(this);
		
		names = new ImageLayer(0, 220, "names.png");
		names.centralizeX(this);
		
		loading = 100;
	}
	
	@Override
	public void draw(Graphic g) {
		background.draw(g);
		
		credits.draw(g);
		
		names.draw(g);
	}
	
	public GUIEvent updateMouse(PointerEvent event) {
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
			nextApplication = new MainMenu(w, h);
		}
		
		return null;
	}

}
