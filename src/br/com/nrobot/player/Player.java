package br.com.nrobot.player;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.GeometricLayer;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.network.client.NinjaRobotClient;

public abstract class Player {
	
	private AnimatedLayer layer;
	
	private boolean walkRight = false;
	private boolean walkLeft = false;
	
	private boolean turnRight = false;
	
	private int speed = 10;
	
	private GeometricLayer hitbox;
	
	private int points = 0;
	
	private boolean dead = false;
	
	public Player(int x, int y, String path) {
		layer = new AnimatedLayer(x, y, 64, 64, path);
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
		
		/*g.setAlpha(60);
		g.fillRect(hitbox);
		g.setAlpha(100);*/
	}
	
	public boolean colide(Fallen fallen) {
		return hitbox.colideRect(fallen);
	}

	public void handleEvent(KeyEvent event) {
		if(event.isKeyDown(KeyEvent.VK_RIGHT_ARROW)) {
			walkRight = true;
			layer.setYImage(0);
			
			turnRight = true;
		}
		
		if(event.isKeyUp(KeyEvent.VK_RIGHT_ARROW)) {
			walkRight = false;
		}
		
		if(event.isKeyDown(KeyEvent.VK_LEFT_ARROW)) {
			walkLeft = true;
			layer.setYImage(64);
			
			turnRight = false;
		}
		
		if(event.isKeyUp(KeyEvent.VK_LEFT_ARROW)) {
			walkLeft = false;
		}
	}
	
	public void handleEvent(KeyEvent event, NinjaRobotClient client) {
		if(event.isKeyDown(KeyEvent.VK_RIGHT_ARROW)) {
			walkRight = true;
			layer.setYImage(0);
			
			turnRight = true;
			client.getProtocol().sendPressKeyRight();
		}
		
		if(event.isKeyUp(KeyEvent.VK_RIGHT_ARROW)) {
			walkRight = false;
			client.getProtocol().sendReleaseKeyRight();
		}
		
		if(event.isKeyDown(KeyEvent.VK_LEFT_ARROW)) {
			walkLeft = true;
			layer.setYImage(64);
			
			turnRight = false;
			client.getProtocol().sendPressKeyLeft();
		}
		
		if(event.isKeyUp(KeyEvent.VK_LEFT_ARROW)) {
			walkLeft = false;
			client.getProtocol().sendReleaseKeyLeft();
		}
	}

	public int getPoints() {
		return points;
	}	

	public void addPoint() {
		points++;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public void setPosition(int position) {
		layer.setX(position);
	}
}
