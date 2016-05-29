package br.com.nrobot.fallen;

import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public class Leaf extends Fallen {

	public Leaf(int x, int y) {
		super(x, y, "items/leaf.png");
	}

	public Leaf(int x, int y, String path) {
		super(x, y, path);
	}

	@Override
	public void colide(Player player) {
		if(!visible)
			return;

		if(player.colide(this)) {
			setVisible(false);
			player.addPoint();
		}
	}

	@Override
	public void affectPlayer(ServerPlayer player) {
		player.addPoint();
	}
}
