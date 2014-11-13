package br.com.nrobot.fallen;

import br.com.nrobot.player.Player;


public class Nut extends Fallen {

	public Nut(int x, int y) {
		super(x, y, "piece.png");
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
	
}
