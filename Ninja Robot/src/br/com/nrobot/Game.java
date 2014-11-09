package br.com.nrobot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.player.Player;

public class Game extends Application {

	private ImageLayer background;
	
	private Player robot;
	
	
	private int fallenSpeed = 3;
	
	private long lastCreation = 0;
	private final long delay = 400;
	
	private List<Fallen> fallen = new ArrayList<Fallen>();
				
	public Game(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
				
		background = new ImageLayer("background.png");
		
		robot = new Player(0, 540);
		
		createPiece();
						
		updateAtFixedRate(50);
		
		loading = 100;
	}
	
	private void createPiece() {
		
		int x = new Random().nextInt(w);
		
		Nut nut = new Nut(x, -20);
		
		fallen.add(nut);
	}
	
	public void timeUpdate(long now) {
		robot.update(now);
		
		for(int i = fallen.size()-1; i > 0; i--) {
			
			Fallen nut = fallen.get(i);
						
			nut.setOffsetY(fallenSpeed);
			
			if(nut.getY()>h)
				fallen.remove(nut);
		}
		
		if(now > lastCreation+delay) {
			lastCreation = now;
			createPiece();
		}
		/*for(Fallen layer: fallen) {
			layer.update(now);
		}*/
		
	}
	
	@Override
	public void draw(Graphic g) {
		background.draw(g);
		
		robot.draw(g);
		
		for(ImageLayer layer: fallen) {
			layer.draw(g);
		}
		
	}
	
	public GUIEvent updateKeyboard(KeyEvent event) {
		
		robot.handleEvent(event);
		
		return null;
		
	}

}
