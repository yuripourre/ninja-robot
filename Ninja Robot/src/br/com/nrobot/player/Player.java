package br.com.nrobot.player;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.GeometricLayer;
import br.com.nrobot.fallen.Fallen;

public class Player {
	
	private AnimatedLayer layer;
	
	private boolean walkRight = false;
	private boolean walkLeft = false;
	
	private boolean turnRight = false;
	
	private int speed = 10;
	
	private GeometricLayer hitbox;
	
	public Player(int x, int y) {
		layer = new AnimatedLayer(x, y, 64, 64, "robot.png");
		layer.setFrames(4);
		layer.setSpeed(100);
		
		hitbox = new GeometricLayer(x, y, layer.getTileW()-16*2, layer.getTileH()-30);
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
		
		updateHitbox();
	}
	
	private void updateHitbox() {
		hitbox.setCoordinates(16+layer.getX(), layer.getY()+4);
	}

	public void draw(Graphic g) {
		layer.draw(g);
		
		g.setAlpha(60);
		g.fillRect(hitbox);
		g.setAlpha(100);
	}
	
	public boolean colide(Fallen fallen) {
		return hitbox.colideRect(fallen);
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
