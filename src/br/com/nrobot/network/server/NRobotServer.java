package br.com.nrobot.network.server;

import br.com.midnight.model.Peer;
import br.com.midnight.server.TCPServer;
import examples.action.client.ActionClientProtocol;

public class NRobotServer extends TCPServer {

	private long delay = 100;
	private long lastUpdate = 0;
	
	private NRobotServerProtocol listener;

	public NRobotServer(int port) {
		super(port);

		name = "NRobot Server";
				
		listener = new NRobotServerProtocol(ActionClientProtocol.PREFIX_ACTION);		
		handshaker = new NRobotHandshaker(listener.getPlayers());

		addProtocol(ActionClientProtocol.PREFIX_ACTION, listener);
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void update(long now) {
		if(lastUpdate + delay < now) {
			listener.updatePositions();
		}
	}
	
	@Override
	public void joinPeer(Peer peer) {
		System.out.println("ActionPeer "+peer.getSessionID()+" connected.");
		
		listener.addPeer(peer);
	}
	
	@Override
	public void leftPeer(Peer peer) {
		System.out.println("Player "+peer.getSessionID()+" disconnected.");
		
		listener.removePeer(peer);
	}
	
}
