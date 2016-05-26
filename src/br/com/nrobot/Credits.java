package br.com.nrobot;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
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
		
		names = new ImageLayer(0, 220, "ui/names.png");
		names.centralizeX(this);
		
		loading = 100;
	}
	
	@Override
	public void draw(Graphics g) {
		background.draw(g);
		
		credits.draw(g);
		
		names.draw(g);
	}
	
	@Override
	public void updateMouse(PointerEvent event) {
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
			nextApplication = new MainMenu(w, h);
		}
	}
	
	@Override
	public void updateKeyboard(KeyEvent event) {
		if (event.isKeyDown(KeyEvent.VK_ESC)) {
			nextApplication = new MainMenu(w, h);
		}
	}

}
