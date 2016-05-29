package br.com.nrobot.network.server;

import java.util.Map;

import br.com.midnight.protocol.handshake.StringHandShaker;
import br.com.nrobot.network.client.ClientProtocol;
import br.com.nrobot.player.ServerPlayer;

public class Handshaker extends StringHandShaker {

	private Map<String, ServerPlayer> players;

	public Handshaker(Map<String, ServerPlayer> players) {
		super();
		this.players = players;
	}

	@Override
	public String handshakeText(String sessionId) {
		String message = ClientProtocol.PREFIX_NINJA_ROBOT+" "+ ClientProtocol.PREFIX_INIT+" "+sessionId+" ";

		for(ServerPlayer player: players.values()) {
			message += sendPlayer(player)+" ";
		}

		return message;
	}

	public static String sendPlayer(ServerPlayer player) {
		return player.id+" "+player.name;
	}
}
