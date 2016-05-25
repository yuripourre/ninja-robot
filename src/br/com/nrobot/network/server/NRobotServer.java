package br.com.nrobot.network.server;

import br.com.midnight.model.Peer;
import br.com.midnight.server.TCPServer;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.client.NinjaRobotClient;

public class NRobotServer extends TCPServer {

	private long delay = 120;
	private long lastUpdate = 0;
	
	private NRobotServerProtocol listener;

	public NRobotServer(GameMode mode) {
		super();
		name = "NRobot Server";
		
		if(GameMode.BATTLE == mode) {
			this.port = NinjaRobotClient.BATTLE_PORT;
			listener = new NRobotBattleServerProtocol(NRobotClientProtocol.PREFIX_NINJA_ROBOT);
		} else {
			this.port = NinjaRobotClient.STORY_PORT;
			listener = new NRobotStoryServerProtocol(NRobotClientProtocol.PREFIX_NINJA_ROBOT);
		}
		
		openPort(this.port);
		
		handshaker = new NRobotHandshaker(listener.getPlayers());

		addProtocol(NRobotClientProtocol.PREFIX_NINJA_ROBOT, listener);
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void update(long now) {
		if(lastUpdate + delay < now) {
			listener.update(now);
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
