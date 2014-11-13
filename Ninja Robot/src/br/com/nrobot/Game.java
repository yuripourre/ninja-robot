package br.com.nrobot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.etyllica.animation.listener.OnAnimationFinishListener;
import br.com.etyllica.animation.scripts.complex.VerticalShakeScript;
import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.fallen.Bomb;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.fx.Explosion;
import br.com.nrobot.player.Player;

public class Game extends Application implements OnAnimationFinishListener {

	private ImageLayer background;
	
	private Player robot;
			
	private long lastCreation = 0;
	private final long delay = 400;
	
	private Explosion explosion;
	
	private List<Fallen> pieces = new ArrayList<Fallen>();
					
	public Game(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
				
		background = new ImageLayer("background.png");
		
		robot = new Player(0, 540);
		
		createPiece();
		
		explosion = new Explosion();
		
		loading = 100;
		
		updateAtFixedRate(50);
				
	}
	
	private void createPiece() {
		
		final int bombPercentage = 8;
		
		Random random = new Random();
		
		int x = random.nextInt(w);
		
		int type = random.nextInt(100);
		
		if(type > bombPercentage) {
			pieces.add(new Nut(x, -20));
		} else {
			pieces.add(new Bomb(x, -20));
		}			
		
	}
	
	public void timeUpdate(long now) {
		robot.update(now);
		
		for(int i = pieces.size()-1; i > 0; i--) {
			
			Fallen fallen = pieces.get(i);
			
			fallen.setOffsetY(fallen.getSpeed());
			
			if(fallen.getY()>h)
				pieces.remove(fallen);
			
			fallen.update(now);
			
			fallen.colide(robot);
		}
		
		if(now > lastCreation+delay) {
			lastCreation = now;
			createPiece();
		}
				
	}
	
	@Override
	public void draw(Graphic g) {
		background.draw(g);
		
		g.setFont(g.getFont().deriveFont(28f));
		
		g.escreveX(60, "Points: "+Integer.toString(robot.getPoints()));
				
		robot.draw(g);
		
		for(ImageLayer layer: pieces) {
			layer.draw(g);
		}
		
		explosion.draw(g);
	}
	
	@Override
	public void onAnimationFinish(long now) {	
		VerticalShakeScript shake = new VerticalShakeScript(background, 500);
		shake.setStrength(8);
		shake.repeat(3);
		
		this.scene.addAnimation(shake);
	}
	
	public GUIEvent updateKeyboard(KeyEvent event) {
		
		robot.handleEvent(event);
		
		return null;
		
	}

}
