package br.com.nrobot.player;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.GeometricLayer;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.network.client.ClientProtocol;

public abstract class Player {
	protected String name;
	protected String sprite = ClientProtocol.SPRITE_BLUE;
	protected String item = ServerPlayer.ITEM_NONE;
	protected String state = ServerPlayer.STATE_NONE;

	protected boolean walkRight = false;
	protected boolean walkLeft = false;

	protected boolean turnRight = false;

	protected int speed = 10;

	protected GeometricLayer hitbox;

	private int points = 0;

	private boolean dead = false;

	public abstract void turnRight();
	public abstract void turnLeft();
	public abstract void die();
	public abstract void stand();
	public abstract void walk();
	public abstract void attack();
	public abstract void jumpUp();
	public abstract void jumpDown();

	public Player(int x, int y) {
		super();
	}

	public abstract void update(long now);

	public abstract void draw(Graphics g);

	public boolean colide(Fallen fallen) {
		return hitbox.colideRect(fallen);
	}

	public void handleEvent(KeyEvent event) {
		if(event.isKeyDown(KeyEvent.VK_RIGHT_ARROW)) {
			walkRight = true;
			turnRight = true;
			turnRight();
		}

		if(event.isKeyUp(KeyEvent.VK_RIGHT_ARROW)) {
			walkRight = false;
		}

		if(event.isKeyDown(KeyEvent.VK_LEFT_ARROW)) {
			walkLeft = true;
			turnRight = false;
			turnLeft();
		}

		if(event.isKeyUp(KeyEvent.VK_LEFT_ARROW)) {
			walkLeft = false;
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

	public abstract void setPosition(int x, int y);

	public void setState(String state) {
		if (this.state.equals(state)) {
			return;
		}

		this.state = state;

		if(ServerPlayer.STATE_WALK_LEFT.equals(state)) {
			turnLeft();
			walk();
			walkLeft = true;
		} else if(ServerPlayer.STATE_WALK_RIGHT.equals(state)) {
			turnRight();
			walk();
			walkRight = true;
		} else if(ServerPlayer.STATE_STAND.equals(state)) {
			stand();
			walkLeft = false;
			walkRight = false;
		} else if(ServerPlayer.STATE_JUMPING_DOWN.equals(state)) {
			jumpDown();
		} else if(ServerPlayer.STATE_JUMPING_UP.equals(state)) {
			jumpUp();
		} else if(ServerPlayer.STATE_DEAD.equals(state))  {
			die();
		} else if(ServerPlayer.STATE_ATTACK.equals(state))  {
			attack();
		}
	}

	public abstract void updatePlayer(long now);
	public abstract void changeSprite();

	public void setPoints(int points) {
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSprite() {
		return sprite;
	}

	public void setSprite(String sprite) {
		this.sprite = sprite;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getState() {
		return state;
	}

	public abstract int getX();
	public abstract int getY();

}
