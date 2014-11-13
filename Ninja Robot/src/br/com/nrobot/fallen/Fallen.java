package br.com.nrobot.fallen;

import br.com.etyllica.core.Updatable;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.player.Player;

public abstract class Fallen extends ImageLayer implements Updatable {

	protected int speed = 3;
	
	public Fallen(int x, int y, String path) {
		super(x, y, path);
	}

	@Override
	public void update(long now) {}

	public abstract void colide(Player player);
	
	public int getSpeed() {
		return speed;
	}
		
}
