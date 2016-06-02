package br.com.nrobot.network.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.player.Player;

public class ClientGameState {

	List<Fallen> fallen;
	Map<String, Player> players = new HashMap<>();
	
	public ClientGameState() {
		super();
	}

	public List<Fallen> getFallen() {
		return fallen;
	}

	public void setFallen(List<Fallen> fallen) {
		this.fallen = fallen;
	}

	public Map<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Player> players) {
		this.players = players;
	}
		
}

