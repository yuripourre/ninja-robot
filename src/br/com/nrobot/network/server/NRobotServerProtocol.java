package br.com.nrobot.network.server;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.midnight.protocol.common.StringServerProtocol;
import br.com.nrobot.player.ServerPlayer;

public abstract class NRobotServerProtocol extends StringServerProtocol {

	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	
	protected Map<String, ServerPlayer> players = new LinkedHashMap<String, ServerPlayer>();
	
	public NRobotServerProtocol(String prefix) {
		super(prefix);
		// TODO Auto-generated constructor stub
	}

	public abstract void update(long now);

	public Map<String, ServerPlayer>  getPlayers() {
		return players;
	}
	
}
