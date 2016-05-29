package br.com.nrobot.network.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.midnight.model.Peer;
import br.com.nrobot.fallen.Hive;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Glue;
import br.com.nrobot.fallen.Leaf;
import br.com.nrobot.network.client.ClientProtocol;
import br.com.nrobot.network.server.ai.AI;
import br.com.nrobot.network.server.ai.DumbAI;
import br.com.nrobot.network.server.model.PlayerRole;
import br.com.nrobot.player.Bot;
import br.com.nrobot.player.ServerPlayer;

public class BattleServerProtocol extends ServerProtocol {

	public static final String PREFIX_BOT = "B";
	public static final String PREFIX_HIVE = "H";
	public static final String PREFIX_LEAF = "L";
	public static final String PREFIX_GLUE = "G";

	private static final int NUMBER_OF_BOTS = 1;
	private static final AI ai = new DumbAI();

	private long lastCreation = 0;
	private final long delay = 400;
	private boolean roomReady = false;

	private List<Fallen> leaves = new ArrayList<Fallen>();
	private List<Fallen> hives = new ArrayList<Fallen>();
	private List<Fallen> glues = new ArrayList<Fallen>();

	public BattleServerProtocol() {
		super();
	}

	@Override
	public void addPeer(Peer peer) {
		super.addPeer(peer);
		ServerPlayer player = new ServerPlayer(peer.getSessionID());
		players.put(peer.getSessionID(), player);
		sendTCPtoAll(ClientProtocol.PREFIX_JOIN + " " + Handshaker.sendPlayer(player));
	}

	private void addBot(String botId, String name) {
		Bot player = new Bot(botId, ai);
		player.name = name;

		players.put(botId, player);
		sendTCPtoAll(ClientProtocol.PREFIX_JOIN + " " + Handshaker.sendPlayer(player));
	}

	public void removePeer(Peer peer) {
		super.removePeer(peer);
		players.remove(peer.getSessionID());
		sendTCPtoAllExcept(peer, ClientProtocol.PREFIX_EXIT + " " + peer.getSessionID());
	}

	@Override
	public void receiveUDP(Peer peer, String msg) {
	}

	@Override
	public void receiveTCP(Peer peer, String msg) {
		System.out.println(getClass().getSimpleName() + " - Received TCP: " + msg);

		if (msg.startsWith(ClientProtocol.PREFIX_COMMAND)) {

			ServerPlayer player = players.get(peer.getSessionID());
			if (!player.isDead()) {
				player.handleEvent(msg, players.values());
			}
		} else if (msg.startsWith(ClientProtocol.PREFIX_CONFIG)) {

			updateConfig(peer, msg);

		} else if (msg.startsWith(ClientProtocol.PREFIX_CHEAT_CODE)) {

			String command = msg.split(" ")[1];

			if (ClientProtocol.CHEAT_RESSURRECT.equals(command)) {
				for (ServerPlayer player : players.values()) {
					player.ressurrect();
				}
			}

		} else {
			//sendTCPtoAll(peer.getSessionID()+" "+msg);
		}
	}

	private void updateConfig(Peer peer, String msg) {
		ServerPlayer player = players.get(peer.getSessionID());

		if (ClientProtocol.CONFIG_NAME.equals(msg.split(" ")[1])) {
			String name = msg.split(" ")[2];
			player.name = name;

			sendTCPtoAll(ClientProtocol.PREFIX_CONFIG + " " + peer.getSessionID() + " " + ClientProtocol.CONFIG_NAME + " " + name);
		} else if (ClientProtocol.CONFIG_SPRITE.equals(msg.split(" ")[1])) {
			String sprite = msg.split(" ")[2];
			player.sprite = sprite;

			sendTCPtoAll(ClientProtocol.PREFIX_CONFIG + " " + peer.getSessionID() + " " + ClientProtocol.CONFIG_SPRITE + " " + sprite);
		}

		if (ClientProtocol.CONFIG_START.equals(msg.split(" ")[1])) {
			player.state = ServerPlayer.STATE_READY;
			sendTCPtoAll(ClientProtocol.PREFIX_CONFIG + " " + peer.getSessionID() + " " + ClientProtocol.CONFIG_READY + " :P");

			if (players.size() == 0) {
				return;
			}

			boolean start = true;

			for (ServerPlayer p : players.values()) {
				if (!ServerPlayer.STATE_READY.equals(p.state)) {
					start = false;
					break;
				}
			}
			if (start) {
				sendTCPtoAll(ClientProtocol.PREFIX_CONFIG + " " + peer.getSessionID() + " " + ClientProtocol.CONFIG_START + " :P");
				startGame();
			}

		}
	}

	private void startGame() {
		// create bots
		for (int i = 0; i < NUMBER_OF_BOTS; i++) {
			String id = PREFIX_BOT + i;
			addBot(id, "BOT" + (i + 1));
		}
		roomReady = true;
	}

	public void updatePlayers(long now) {
		String message = ClientProtocol.PREFIX_POSITIONS + " ";

		for (ServerPlayer player : players.values()) {
			player.update(now);
			message += player.asText() + " ";
		}

		//Add hives and fallen items
		message += PREFIX_LEAF + " ";
		for (Fallen leaf : leaves) {
			message += leaf.asText() + " ";
		}

		message += PREFIX_HIVE + " ";
		for (Fallen hive : hives) {
			message += hive.asText() + " ";
		}

		message += PREFIX_GLUE + " ";
		for (Fallen glue : glues) {
			message += glue.asText() + " ";
		}

		sendTCPtoAll(message);
	}

	private void createPiece() {
		final int leafInterval = 50; // 50%
		final int hiveInterval = 25; // 25%
		final int bombInterval = 8;  // etc...
		final int glueInterval = 5;

		Random random = new Random();

		int x = random.nextInt(WIDTH);

		int type = random.nextInt(100);

		int speed = 3 + random.nextInt(3);

		if (type < hiveInterval) {
			hives.add(new Hive(x, -20));
		} else if (type < glueInterval) {
			glues.add(new Glue(x, -20));
		} else {
			Leaf leaf = new Leaf(x, -20);
			leaf.setSpeed(speed);

			leaves.add(leaf);
		}
	}

	public void update(long now) {
		if (!roomReady) {
			return;
		}

		updatePlayers(now);

		updatePieceList(now, leaves);
		updatePieceList(now, hives);
		updatePieceList(now, glues);

		if (now > lastCreation + delay) {
			lastCreation = now;
			createPiece();
		}
	}

	private void updatePieceList(long now, List<Fallen> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			Fallen fallen = list.get(i);
			updatePiece(fallen, list);
			fallen.update(now);
		}
	}

	private void updatePiece(Fallen fallen, List<Fallen> list) {
		fallen.setOffsetY(fallen.getSpeed());

		if ((fallen.getY() > HEIGHT) || !fallen.isVisible())
			list.remove(fallen);

		for (ServerPlayer player : players.values()) {
			if (!player.isDead()) {
				fallen.colide(player);
			}
		}
	}

}
