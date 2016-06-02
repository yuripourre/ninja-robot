package br.com.nrobot.player;

import br.com.nrobot.network.server.ai.AI;
import br.com.nrobot.network.server.model.PlayerRole;
import br.com.nrobot.network.server.model.ServerGameState;

public class Bot extends ServerPlayer {

	public boolean goingLeft = false;
	public boolean goingRight = false;

	private final AI ai;

	public Bot(String id, AI ai) {
		super(id);
		this.ai = ai;
		role = PlayerRole.BOT;
	}

	@Override
	public void update(ServerGameState gameState) {
		ai.act(this, gameState);
		super.update(gameState);
	}
}
