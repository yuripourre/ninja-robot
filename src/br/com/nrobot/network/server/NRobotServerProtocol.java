package br.com.nrobot.network.server;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringServerProtocol;
import br.com.nrobot.network.client.NRobotClientProtocol;

public class NRobotServerProtocol extends StringServerProtocol {
	
	private Set<String> players = new LinkedHashSet<String>();
	private Map<String, Integer> positions = new LinkedHashMap<String, Integer>();
	
	public NRobotServerProtocol(String prefix) {
		super(prefix);
	}
	
	@Override
	public void addPeer(Peer peer) {
		super.addPeer(peer);
		players.add(peer.getSessionID());
		sendTCPtoAll(NRobotClientProtocol.PREFIX_JOIN+" "+peer.getSessionID());
	}
	
	public void removePeer(Peer peer) {
		super.removePeer(peer);
		players.remove(peer.getSessionID());
		sendTCPtoAll(NRobotClientProtocol.PREFIX_EXIT+" "+peer.getSessionID());
	}
	
	@Override
	public void receiveUDP(Peer peer, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void receiveTCP(Peer peer, String msg) {
		System.out.println(getClass().getSimpleName()+" - Received TCP: "+msg);
						
		if (msg.startsWith(NRobotClientProtocol.PREFIX_ACTION)) {
			System.out.println("ACTION: "+msg.split(" ")[0]);
			System.out.println("STATE: "+msg.split(" ")[1]);
			System.out.println("KEY: "+msg.split(" ")[2]);
		} else {
			//sendTCPtoAll(peer.getSessionID()+" "+msg);	
		}
	}

	public void updatePositions() {
		String message = NRobotClientProtocol.PREFIX_POSITIONS+" ";
		
		for(String player: players) {
			message += player+" ";
		}
		
		sendTCPtoAll(message);
	}
	
	public Set<String> getPlayers() {
		return players;
	}
	
}
