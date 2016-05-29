package br.com.nrobot.network.client;

import br.com.nrobot.config.Config;

public interface ClientListener {
	public void startGame();
	public void exitClient(String id);
	public void joinedClient(String id, String name);
	public void receiveMessage(String id, String message);
	public void init(String[] ids);
	public void updatePositions(String positions);
	public void updateName(String id, String name);
	public void updateSprite(String id, String sprite);
	public void updateReady(String id);
	public Config getConfig();
}
