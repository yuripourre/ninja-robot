package br.com.nrobot.player;

import br.com.nrobot.network.server.model.PlayerRole;

public class Bot extends ServerPlayer {

	public boolean goingLeft = false;
	public boolean goingRight = false;
	
	public Bot(String id) {
		super(id);
		role = PlayerRole.BOT;
	}
	
}
