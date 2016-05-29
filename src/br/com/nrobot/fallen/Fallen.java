package br.com.nrobot.fallen;

import br.com.etyllica.core.Updatable;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public abstract class Fallen extends ImageLayer implements Updatable {

	protected int speed = 3;

	private boolean markedForRemoval = false;

	public Fallen(int x, int y, String path) {
		super(x, y, path);
	}

	public void markForRemoval() {
		markedForRemoval = true;
	}

	public boolean isMarkedForRemoval() {
		return markedForRemoval;
	}

	@Override
	public void update(long now) {
		if (visible) {
			setOffsetY(speed);
		}
	}

	public abstract void colide(Player player);

	public void colide(ServerPlayer player) {
		if (!visible)
			return;

		if (player.colide(this)) {
			markForRemoval();
			affectPlayer(player);
		}
	}

	public abstract void affectPlayer(ServerPlayer player);

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String asText() {
		return x + " " + y;
	}
}
