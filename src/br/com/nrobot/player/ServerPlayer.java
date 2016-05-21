package br.com.nrobot.player;

import java.util.Collection;

import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.server.NRobotServerProtocol;
import br.com.nrobot.network.server.model.GamePad;

public class ServerPlayer {

	private static final int SPRITE_SIZE = 64;
	
	public static final String STATE_READY = "R";
	public static final String STATE_NONE = "N";
	public static final String STATE_WALK_LEFT = "l";
	public static final String STATE_WALK_RIGHT = "r";
	public static final String STATE_STAND = "s";
	public static final String STATE_DEAD = "e";
	public static final String STATE_FREEZE = "f";
	public static final String STATE_JUMPING_UP = "j";
	public static final String STATE_JUMPING_DOWN = "d";

	public static final String ITEM_NONE = "N";
	public static final String ITEM_GLUE = "Glue";
	public static final String ITEM_TONIC = "Tonic";
	public static final String ITEM_RESSURRECT = "Dead";

	public int x = 0;
	public int y = 520;
	
	public int playerY = 520;

	public int speed = 15;
	public int jumpSpeed = 10;

	public int jumpDelay = 600;
	public int freezeDelay = 2500;
	public int points = 0;
	public String id = "";
	public String name = "Robot";
	public String state = STATE_STAND;
	public String sprite = "robot.png";

	public String item = ITEM_NONE;

	public GamePad pad;
	public boolean dead = false;
	public boolean jumping = false;
	public boolean fallen = false;
	public long when = 0;
	public long jumpStart = 0;

	public ServerPlayer(String id) {
		super();
		this.id = id;
		pad = new GamePad();
	}

	public void handleEvent(String msg, Collection<ServerPlayer> players) {
		if(NRobotClientProtocol.STATE_PRESS.equals(msg.split(" ")[1])) {

			String key = msg.split(" ")[2];

			if(NRobotClientProtocol.KEY_RIGHT.equals(key)) {
				pad.right = true;
			} else if (NRobotClientProtocol.KEY_LEFT.equals(key)) {
				pad.left = true;
			} else if (NRobotClientProtocol.KEY_JUMP.equals(key)) {
				pad.jump = true;
			} else if (NRobotClientProtocol.KEY_ITEM.equals(key)) {
				useItem(players);
			}
		} else if (NRobotClientProtocol.STATE_RELEASE.equals(msg.split(" ")[1])) {

			String key = msg.split(" ")[2];

			if (NRobotClientProtocol.KEY_RIGHT.equals(key)) {
				pad.right = false;
			} else if(NRobotClientProtocol.KEY_LEFT.equals(key)) {
				pad.left = false;
			} else if (NRobotClientProtocol.KEY_JUMP.equals(key)) {
				pad.jump = false;
			}
		}
	}

	private void useItem(Collection<ServerPlayer> players) {
		long now = System.currentTimeMillis();

		if(ITEM_GLUE.equals(item)) {
			for(ServerPlayer player : players) {
				if (player.id == id) {
					continue;
				}

				player.state = STATE_FREEZE;
				player.when = now;
			}
		}

		item = ITEM_NONE;
	}

	public void update(long now) {
		if(dead)
			return;

		if(STATE_FREEZE.equals(state)) {
			if(when + freezeDelay > now)
				return;
		}

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

		if (pad.jump) {
			if(!jumping) {
				jumping = true;
				jumpStart = now;
			}
		}

		if (jumping) {
			if (now - jumpStart< jumpDelay) {
				y -= jumpSpeed;
			} else if(fallen) {
				if(y < playerY) {
					y += jumpSpeed;	
				} else {
					y = playerY;
					jumping = false;
					fallen = false;
					state = STATE_STAND;
				}
			} else {
				fallen = true;
			}
			
			if (!fallen) {
				state = STATE_JUMPING_UP;	
			} else {
				state = STATE_JUMPING_DOWN;
			}
		}
	}

	public void addPoint() {
		points++;
	}

	public String asText() {
		return id+" "+x+" "+y+" "+state+" "+item+" "+points;
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
		return dead || ServerPlayer.STATE_DEAD.equals(state);
	}

	public void dead() {
		dead = true;
		pad.left = false;
		pad.right = false;
		state = STATE_DEAD;
	}

	public void ressurrect() {
		dead = false;
		state = STATE_STAND;
		item = ITEM_RESSURRECT;
	}
}
