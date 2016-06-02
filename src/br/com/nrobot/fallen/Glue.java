package br.com.nrobot.fallen;

import br.com.nrobot.player.ServerPlayer;


public class Glue extends Leaf {

	public Glue(int x, int y) {
		super(x, y, "items/glue.png");
	}

	@Override
	public FallenType getType() {
		return FallenType.GLUE;
	}

	@Override
	public void affectPlayer(ServerPlayer player) {
		player.item = ServerPlayer.ITEM_GLUE;
	}
}
