package br.com.nrobot.network.server;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringServerProtocol;
import examples.action.client.ActionClientProtocol;

public class ActionServerProtocol extends StringServerProtocol {
	
	private Set<String> players = new LinkedHashSet<String>();
	
	public ActionServerProtocol(String prefix) {
		super(prefix);
	}

	private boolean receivedTcp = false;
	
	@Override
	public void addPeer(Peer peer) {
		super.addPeer(peer);
		players.add(peer.getSessionID());
		sendTCPtoAll(ActionClientProtocol.PREFIX_JOIN+" "+peer.getSessionID());
	}
	
	public void removePeer(Peer peer) {
		super.removePeer(peer);
		players.remove(peer.getSessionID());
		sendTCPtoAll(ActionClientProtocol.PREFIX_EXIT+" "+peer.getSessionID());
	}
	
	@Override
	public void receiveUDP(Peer peer, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void receiveTCP(Peer peer, String msg) {
		System.out.println(getClass().getSimpleName()+" - Received TCP: "+msg);
		receivedTcp = true;
		
		sendTCPtoAll(peer.getSessionID()+" "+msg);
	}

	public boolean receivedTcp() {
		return receivedTcp;
	}

	public Set<String> getPlayers() {
		return players;
	}
	
}
