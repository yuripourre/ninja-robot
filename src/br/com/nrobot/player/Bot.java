package br.com.nrobot.player;

import br.com.nrobot.network.server.ai.AI;
import br.com.nrobot.network.server.model.PlayerRole;

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
	public void update(long now) {
		ai.act(this);
		super.update(now);
	}
}
