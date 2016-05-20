package br.com.nrobot.fallen;

import br.com.nrobot.player.ServerPlayer;


public class Tonic extends Nut {

	public Tonic(int x, int y) {
		super(x, y, "items/potion.png");
	}
	
	@Override
	public void affectPlayer(ServerPlayer player) {
		player.item = ServerPlayer.ITEM_TONIC;
	}
}
