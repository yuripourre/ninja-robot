package br.com.nrobot.network.server;

import br.com.midnight.model.Peer;
import br.com.midnight.server.TCPServer;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.network.client.ClientProtocol;
import br.com.nrobot.network.client.Client;

public class Server extends TCPServer {

	private long delay = 120;
	private long lastUpdate = 0;

	private ServerProtocol listener;

	public Server(GameMode mode) {
		super();
		name = "NRobot Server";

		if(GameMode.BATTLE == mode) {
			this.port = Client.BATTLE_PORT;
			listener = new BattleServerProtocol(ClientProtocol.PREFIX_NINJA_ROBOT);
		} else {
			this.port = Client.STORY_PORT;
			listener = new StoryServerProtocol(ClientProtocol.PREFIX_NINJA_ROBOT);
		}

		openPort(this.port);

		handshaker = new Handshaker(listener.getPlayers());

		addProtocol(ClientProtocol.PREFIX_NINJA_ROBOT, listener);
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
