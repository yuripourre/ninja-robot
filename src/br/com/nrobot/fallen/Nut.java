package br.com.nrobot.fallen;

import br.com.nrobot.network.server.model.ServerPlayer;
import br.com.nrobot.player.Player;


public class Nut extends Fallen {

	public Nut(int x, int y) {
		super(x, y, "items/piece.png");
	}
	
	public Nut(int x, int y, String path) {
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
