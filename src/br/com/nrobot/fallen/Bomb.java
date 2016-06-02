package br.com.nrobot.fallen;

import br.com.etyllica.core.graphics.Graphics;
import br.com.nrobot.fx.Explosion;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public class Bomb extends Fallen {

	// FIXME: bomb explosion not animating

	private Explosion explosion;

	public Bomb(int x, int y) {
		super(x, y, "items/bomb.png");
		explosion = new Explosion();
		speed = 6;
	}

	@Override
	public FallenType getType() {
		return FallenType.BOMB;
	}

	@Override
	public void update(long now) {
		//super.update(now);
		explosion.animate(now);
	}

	@Override
	public void colide(Player player) {
		if (!visible) {
			return;
		}

		if (player.colide(this)) {
			markForRemoval();
			explode();
			player.setDead(true);
		}
	}
	
	public void explode() {
		explosion.explode(this);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		explosion.draw(g);
	}

	@Override
	public void affectPlayer(ServerPlayer player) {
		player.dead();
	}
}
