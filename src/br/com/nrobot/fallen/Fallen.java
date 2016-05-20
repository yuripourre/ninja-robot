package br.com.nrobot.fallen;

import br.com.etyllica.core.Updatable;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.network.server.model.ServerPlayer;
import br.com.nrobot.player.Player;

public abstract class Fallen extends ImageLayer implements Updatable {

	protected int speed = 3;
	
	public Fallen(int x, int y, String path) {
		super(x, y, path);
	}

	@Override
	public void update(long now) {}

	public abstract void colide(Player player);
	
	public void colide(ServerPlayer player) {
		if(!visible)
			return;
		
		if(player.colide(this)) {
			setVisible(false);
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
		return x+" "+y;
	}

}
