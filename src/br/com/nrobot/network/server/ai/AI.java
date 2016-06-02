package br.com.nrobot.network.server.ai;

import br.com.nrobot.network.server.model.ServerGameState;
import br.com.nrobot.player.Bot;

public interface AI {
	void act(Bot player, ServerGameState gameState);
}
