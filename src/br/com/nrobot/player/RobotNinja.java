package br.com.nrobot.player;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.GeometricLayer;


public class RobotNinja extends Player {

	private AnimatedLayer layer;
	
	public RobotNinja(int x, int y) {
		this(x, y, "player/robot.png");
	}
	
	public RobotNinja(int x, int y, String path) {
		super(x, y);
				
		layer = new AnimatedLayer(x, y, 64, 64, path);
		layer.setFrames(4);
		layer.setSpeed(100);
		
		hitbox = new GeometricLayer(x, y, layer.getTileW()-16*2, layer.getTileH()-30);
	}
	
	public void update(long now) {
		
		if (walkRight||walkLeft) {
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
	
	@Override
	public void draw(Graphic g) {
		layer.draw(g);
	}
	
	public void setPosition(int x, int y) {
		layer.setCoordinates(x, y);
		updateHitbox();
	}
	
	public void turnRight() {
		layer.setYImage(0);
	}
	
	public void turnLeft() {
		layer.setYImage(64);
	}
	
	public void stand() {
		layer.stopAnimation();
	}
	
	public void die() {
		layer.setVisible(false);
	}
	
	public void walk() {
		
	}
	
	public void updatePlayer(long now) {
		if(walkLeft||walkRight) {
			layer.animate(now);
		}
 	}
	
	@Override
	public int getX() {
		return layer.getX();
	}
	
	@Override
	public int getY() {
		return layer.getY();
	}

	@Override
	public void jumpUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jumpDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeSprite() {
		// TODO Auto-generated method stub
		
	}

}
