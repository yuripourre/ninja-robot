package br.com.nrobot.network.server.ai;

import br.com.nrobot.network.server.NRobotBattleServerProtocol;
import br.com.nrobot.player.Bot;

public class DumbAI implements AI {

	private static final int SPRITE_SIZE = 64;
	private static final int BORDER = 20;

	@Override
	public void act(Bot player) {

		if (player.goingLeft) {
			if (player.x > BORDER + player.speed) {
				if (!player.pad.left) {
					player.pad.left = true;
					player.pad.right = false;
				}

			} else {
				player.goingLeft = false;
				player.goingRight = true;
			}
		} else if (player.goingRight) { 
			if(player.x + SPRITE_SIZE + player.speed + BORDER < NRobotBattleServerProtocol.WIDTH) {
				if (!player.pad.right) {
					player.pad.left = false;
					player.pad.right = true;
				}
				
			} else {
				player.goingRight = false;
				player.goingLeft = true;
			}
		} else {
			player.goingRight = true;
		}
		
	}

}
