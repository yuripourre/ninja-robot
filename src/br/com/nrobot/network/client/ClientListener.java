package br.com.nrobot.network.client;

import br.com.nrobot.config.Config;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.network.PlayerData;

import java.util.List;

public interface ClientListener {
	void startGame();
	void exitClient(String id);
	void joinedClient(String id, String name);
	void receiveMessage(String id, String message);
	void init(String[] ids);
	void updatePlayers(List<PlayerData> playersData);
	void updateFallen(List<Fallen> fallen);
	void updateName(String id, String name);
	void updateSprite(String id, String sprite);
	void updateReady(String id);
	Config getConfig();
}
