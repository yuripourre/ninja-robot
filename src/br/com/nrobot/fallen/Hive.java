package br.com.nrobot.fallen;

import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public class Hive extends Fallen {

	public Hive(int x, int y) {
		super(x, y, "items/hive.png");
		speed = 6;
	}

	@Override
	public void colide(Player player) {
		if(!visible)
			return;

		if(player.colide(this)) {
			setVisible(false);
			player.setDead(true);
		}
	}

	@Override
	public void affectPlayer(ServerPlayer player) {
		player.dead();
	}
}
