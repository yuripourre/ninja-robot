package br.com.nrobot.fallen;

import br.com.nrobot.network.server.model.ServerPlayer;


public class Glue extends Nut {

	public Glue(int x, int y) {
		super(x, y, "items/glue.png");
	}
	
	@Override
	public void affectPlayer(ServerPlayer player) {
		player.item = ServerPlayer.ITEM_GLUE;
	}
}
