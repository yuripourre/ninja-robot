package br.com.nrobot.fallen;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.graphics.Graphics;
import br.com.nrobot.fx.Explosion;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public class Bomb extends Fallen {

	private Explosion explosion;

	public Bomb(int x, int y, OnAnimationFinishListener listener) {
		super(x, y, "items/bomb.png");
		explosion = new Explosion();
		explosion.setOnAnimationFinishListener(listener);
		speed = 6;
	}

	public Bomb(int x, int y) {
		super(x, y, "items/bomb.png");
		explosion = new Explosion();
		speed = 6;
	}

	@Override
	public void update(long now) {
		explosion.animate(now);
	}

	@Override
	public void colide(Player player) {
		if (!visible)
			return;

		if (player.colide(this)) {
			setVisible(false);
			explosion.explode(this);
			player.setDead(true);
		}
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
