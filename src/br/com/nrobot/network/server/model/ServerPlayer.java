package br.com.nrobot.network.server.model;

import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.server.NRobotServerProtocol;

public class ServerPlayer {

	private static final int SPRITE_SIZE = 64;
	public static final String STATE_WALK_LEFT = "l";
	public static final String STATE_WALK_RIGHT = "r";
	public static final String STATE_STAND = "s";
	public static final String STATE_EXPLODED = "e";
	
	public int x = 0;
	public int y = 540;
	public int speed = 15;
	public int points = 0;
	public String name = "";
	public String state = STATE_STAND;
	public String sprite = "robot.png";
	
	public GamePad pad;
	
	public boolean dead = false;
	
	public ServerPlayer(String name) {
		super();
		this.name = name;
		pad = new GamePad();
	}
	
	public void handleEvent(String msg) {
		if(NRobotClientProtocol.STATE_PRESS.equals(msg.split(" ")[1])) {
			
			String key = msg.split(" ")[2];
			
			if(NRobotClientProtocol.KEY_RIGHT.equals(key)) {
				pad.right = true;
			} else if (NRobotClientProtocol.KEY_LEFT.equals(key)) {
				pad.left = true;
			}
		} else if (NRobotClientProtocol.STATE_RELEASE.equals(msg.split(" ")[1])) {
			
			String key = msg.split(" ")[2];
			
			if (NRobotClientProtocol.KEY_RIGHT.equals(key)) {
				pad.right = false;
			} else if(NRobotClientProtocol.KEY_LEFT.equals(key)) {
				pad.left = false;
			}
		}
	}

	public void update(long now) {
		if(dead)
			return;
		
		if (pad.left) {
			if (x > 0) {
				x-= speed;
			}
			state = STATE_WALK_LEFT;
		} else if (pad.right) {
			if(x + SPRITE_SIZE < NRobotServerProtocol.WIDTH-speed) {
				x+= speed;
			}
			state = STATE_WALK_RIGHT;
		} else {
			state = STATE_STAND;
		}
	}
	
	public void addPoint() {
		points++;
	}
	
	public String asText() {
		return name+" "+x+" "+y+" "+state+" "+points;
	}

	public boolean colide(Fallen b) {
		int px = x+16;
		int py = y+4;
		int pw = SPRITE_SIZE-32;
		int ph = SPRITE_SIZE-30;
		
		if(b.getX() + b.utilWidth() < px) return false;
		if(b.getX() > px + pw) return false;

		if(b.getY() + b.utilHeight() < py) return false;
		if(b.getY() > py + ph) return false;

		return true;
	}

	public boolean isDead() {
		return dead || ServerPlayer.STATE_EXPLODED.equals(state);
	}

	public void dead() {
		dead = true;
		pad.left = false;
		pad.right = false;
		state = STATE_EXPLODED;
	}
}
