package br.com.nrobot.network.server;

import java.util.Map;

import br.com.midnight.protocol.handshake.StringHandShaker;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.server.model.ServerPlayer;

public class NRobotHandshaker extends StringHandShaker {

	private Map<String, ServerPlayer> players;
	
	public NRobotHandshaker(Map<String, ServerPlayer> players) {
		super();
		this.players = players;
	}

	@Override
	public String handshakeText(String sessionId) {
		String message = NRobotClientProtocol.PREFIX_NINJA_ROBOT+" "+NRobotClientProtocol.PREFIX_INIT+" "+sessionId+" ";
		
		for(ServerPlayer player: players.values()) {
			message += sendPlayer(player)+" ";
		}
		
		return message;
	}

	public static String sendPlayer(ServerPlayer player) {
		return player.id+" "+player.name;
	}
}
