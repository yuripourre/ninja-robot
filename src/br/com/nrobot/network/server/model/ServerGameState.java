package br.com.nrobot.network.server.model;

import java.util.List;
import java.util.Map;

import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.player.ServerPlayer;

public class ServerGameState {

	long now;
	List<Fallen> fallen;
	Map<String, ServerPlayer> players;
	
	public ServerGameState() {
		super();
	}

	public List<Fallen> getFallen() {
		return fallen;
	}

	public void setFallen(List<Fallen> fallen) {
		this.fallen = fallen;
	}

	public Map<String, ServerPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, ServerPlayer> players) {
		this.players = players;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}
		
}

