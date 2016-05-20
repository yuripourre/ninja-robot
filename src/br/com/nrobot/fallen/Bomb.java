package br.com.nrobot.fallen;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.graphics.Graphic;
import br.com.nrobot.fx.Explosion;
import br.com.nrobot.network.server.model.ServerPlayer;
import br.com.nrobot.player.Player;


public class Bomb extends Fallen {
	
	private Explosion explosion;
	
	public Bomb(int x, int y, OnAnimationFinishListener listener) {
	
		super(x, y, "bomb.png");
		
		explosion = new Explosion();
		explosion.setOnAnimationFinishListener(listener);
		
		speed = 6;
	}
	
	public Bomb(int x, int y) {
		
		super(x, y, "bomb.png");
		
		explosion = new Explosion();
		
		speed = 6;
	}

	@Override
	public void update(long now) {
		explosion.animate(now);
	}
	
	@Override
	public void colide(Player player) {
		if(!visible)
			return;
		
		if(player.colide(this)) {
			setVisible(false);
			explosion.explode(this);
			player.setDead(true);
		}
	}
	
	@Override
	public void colide(ServerPlayer player) {
		if(!visible)
			return;
		
		if(player.colide(this)) {
			System.out.println("BOOOOOOOOOOOOOOOOOOOOOOOOM!");
			setVisible(false);
			player.dead();
		}
	}
	
	@Override
	public void draw(Graphic g) {
		super.draw(g);
		explosion.draw(g);
	}
	
}
