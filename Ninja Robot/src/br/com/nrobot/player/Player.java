package br.com.nrobot.player;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;

public class Player {

	private AnimatedLayer layer;
	
	private boolean walkRight = false;
	private boolean walkLeft = false;
	
	private boolean turnRight = false;
	
	private int speed = 10;
	
	public Player(int x, int y) {
		layer = new AnimatedLayer(x, y, 64, 64, "robot.png");
		layer.setFrames(4);
		layer.setSpeed(100);
	}
	
	public void update(long now) {
		if(walkRight||walkLeft) {
			
			if(turnRight) {
				if(layer.getX()+layer.getTileW()<800-speed)
					layer.setOffsetX(speed);
			} else {
				if(layer.getX()>speed)
					layer.setOffsetX(-speed);
			}
			
			layer.animate(now);
		}
	}

	public void draw(Graphic g) {
		layer.draw(g);
	}

	public void handleEvent(KeyEvent event) {
		if(event.isKeyDown(KeyEvent.TSK_SETA_DIREITA)) {
			walkRight = true;
			layer.setYImage(0);
			
			turnRight = true;
		}
		
		if(event.isKeyUp(KeyEvent.TSK_SETA_DIREITA)) {
			walkRight = false;
		}
		
		if(event.isKeyDown(KeyEvent.TSK_SETA_ESQUERDA)) {
			walkLeft = true;
			layer.setYImage(64);
			
			turnRight = false;
		}
		
		if(event.isKeyUp(KeyEvent.TSK_SETA_ESQUERDA)) {
			walkLeft = false;
		}	
	}
	
}
