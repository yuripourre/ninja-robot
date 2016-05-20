package br.com.nrobot.network.server;

import java.util.Set;

import br.com.midnight.protocol.handshake.StringHandShaker;
import br.com.nrobot.network.client.NRobotClientProtocol;

public class NRobotHandshaker extends StringHandShaker {

	private Set<String> players;
	
	public NRobotHandshaker(Set<String> players) {
		super();
		this.players = players;
	}

	@Override
	public String handshakeText(String sessionId) {
		String message = NRobotClientProtocol.PREFIX_NINJA_ROBOT+" "+NRobotClientProtocol.PREFIX_INIT+" "+sessionId+" ";
		
		for(String player: players) {
			message += player+" ";
		}
		
		return message;
	}

}
