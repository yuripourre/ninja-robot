package br.com.nrobot.network.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringServerProtocol;
import br.com.nrobot.fallen.Bomb;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.server.model.ServerPlayer;

public class NRobotServerProtocol extends StringServerProtocol {

	public static final String PREFIX_BOMB = "B";
	public static final String PREFIX_NUT = "N";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private long lastCreation = 0;
	private final long delay = 400;

	private List<Fallen> pieces = new ArrayList<Fallen>();
	private List<Fallen> bombs = new ArrayList<Fallen>();

	private Map<String, ServerPlayer> players = new LinkedHashMap<String, ServerPlayer>();

	public NRobotServerProtocol(String prefix) {
		super(prefix);
	}

	@Override
	public void addPeer(Peer peer) {
		super.addPeer(peer);
		players.put(peer.getSessionID(), new ServerPlayer(peer.getSessionID()));
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

		if (msg.startsWith(NRobotClientProtocol.PREFIX_COMMAND)) {

			ServerPlayer player = players.get(peer.getSessionID());
			if (!player.isDead()) {
				player.handleEvent(msg);
			}
		} else {
			//sendTCPtoAll(peer.getSessionID()+" "+msg);	
		}
	}

	public void updatePlayers(long now) {
		String message = NRobotClientProtocol.PREFIX_POSITIONS+" ";

		for(ServerPlayer player: players.values()) {
			player.update(now);
			message += player.asText()+" ";
		}

		//Add bombs and fallen items
		message += PREFIX_NUT+" ";
		for(Fallen nut : pieces) {
			message += nut.asText()+" ";
		}

		message += PREFIX_BOMB+" ";
		for(Fallen bomb : bombs) {
			message += bomb.asText()+" ";
		}

		sendTCPtoAll(message);
	}

	private void createPiece() {

		final int bombPercentage = 8;

		Random random = new Random();

		int x = random.nextInt(WIDTH);

		int type = random.nextInt(100);

		int speed = 3+random.nextInt(3);

		if(type > bombPercentage) {
			Nut nut = new Nut(x, -20);
			nut.setSpeed(speed);

			pieces.add(nut);
		} else {
			bombs.add(new Bomb(x, -20));
		}
	}

	public void update(long now) {
		updatePlayers(now);

		for(int i = pieces.size()-1; i > 0; i--) {

			Fallen fallen = pieces.get(i);

			updatePiece(fallen);
			fallen.update(now);
		}
		
		for(int i = bombs.size()-1; i > 0; i--) {

			Fallen fallen = bombs.get(i);

			updatePiece(fallen);
			fallen.update(now);
		}

		if(now > lastCreation + delay) {
			lastCreation = now;
			createPiece();
		}
	}

	private void updatePiece(Fallen fallen) {
		fallen.setOffsetY(fallen.getSpeed());

		if ((fallen.getY() > HEIGHT) ||!fallen.isVisible()) 
			pieces.remove(fallen);

		for (ServerPlayer player : players.values()) {
			if (!player.isDead()) {
				fallen.colide(player);
			}
		}
	}

	public Set<String> getPlayers() {
		return players.keySet();
	}

}
