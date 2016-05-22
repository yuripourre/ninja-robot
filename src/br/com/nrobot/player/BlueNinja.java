package br.com.nrobot.player;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.GeometricLayer;
import br.com.etyllica.layer.StaticLayer;
import br.com.nrobot.network.client.NRobotClientProtocol;


public class BlueNinja extends Player {

	StaticLayer right;
	StaticLayer left;
	
	AnimatedLayer layer;
	
	public BlueNinja(int x, int y) {
		super(x, y);
		
		right = new StaticLayer("player/blue_ninja.png");
		left = new StaticLayer("player/blue_ninja_left.png");
		
		layer = new AnimatedLayer(x, y, 64, 64);
		layer.cloneLayer(right);
		layer.setFrames(1);
		layer.setSpeed(200);
		
		hitbox = new GeometricLayer(x, y, layer.getTileW()-16*2, layer.getTileH()-30); 
	}
	
	public void changeSprite() {		
		if(NRobotClientProtocol.SPRITE_BLUE.equals(sprite)) {
			right = new StaticLayer("player/blue_ninja.png");
			left = new StaticLayer("player/blue_ninja_left.png");	
		} else {
			right = new StaticLayer("player/dark_ninja.png");
			left = new StaticLayer("player/dark_ninja_left.png");
		}
		
		layer.cloneLayer(right);
	}
	
	@Override
	public void draw(Graphic g) {
		layer.draw(g);
	}
	
	public void setPosition(int x, int y) {
		layer.setCoordinates(x, y);
	}
	
	public void turnRight() {
		layer.cloneLayer(right);
	}
	
	public void turnLeft() {
		layer.cloneLayer(left);
	}
	
	public void die() {
		layer.setFrames(1);
		layer.setNeedleX(layer.getTileW()*3);
		layer.setNeedleY(layer.getTileH()*2);
		layer.resetAnimation();
		layer.setY(540);
	}

	@Override
	public void stand() {
		layer.setFrames(1);
		layer.setNeedleX(0);
		layer.setNeedleY(0);
		layer.resetAnimation();
	}
	
	@Override
	public void walk() {
		layer.setFrames(6);
		layer.setNeedleX(0);
		layer.setNeedleY(layer.getTileH());
		layer.resetAnimation();
	}

	@Override
	public void update(long now) {
		updatePlayer(now);
	}

	@Override
	public void updatePlayer(long now) {
		layer.animate(now);
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
		layer.setFrames(1);
		layer.setNeedleX(0);
		layer.setNeedleY(layer.getTileH()*2);
		layer.resetAnimation();
	}

	@Override
	public void jumpDown() {
		layer.setFrames(1);
		layer.setNeedleX(layer.getTileW());
		layer.setNeedleY(layer.getTileH()*2);
		layer.resetAnimation();
	}

}
