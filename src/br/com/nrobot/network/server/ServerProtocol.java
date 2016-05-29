package br.com.nrobot.network.server;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.midnight.protocol.common.StringServerProtocol;
import br.com.nrobot.player.ServerPlayer;

public abstract class ServerProtocol extends StringServerProtocol {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	protected Map<String, ServerPlayer> players = new LinkedHashMap<String, ServerPlayer>();

	public ServerProtocol(String prefix) {
		super(prefix);
		// TODO Auto-generated constructor stub
	}

	public abstract void update(long now);

	public Map<String, ServerPlayer>  getPlayers() {
		return players;
	}

}
