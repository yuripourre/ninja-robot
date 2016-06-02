package br.com.nrobot.network.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.midnight.model.Peer;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.FallenType;
import br.com.nrobot.network.client.ClientProtocol;
import br.com.nrobot.network.server.ai.AI;
import br.com.nrobot.network.server.ai.DumbAI;
import br.com.nrobot.network.server.model.ServerGameState;
import br.com.nrobot.player.Bot;
import br.com.nrobot.player.ServerPlayer;

public class BattleServerProtocol extends ServerProtocol {

	public static final String PREFIX_BOT = "B";
	public static final String PREFIX_FALLEN = "F";

	private static final int NUMBER_OF_BOTS = 1;
	private static final AI ai = new DumbAI();

	private Random random = new Random();
	private long lastCreation = 0;
	private final long delay = 400;
	private boolean roomReady = false;

	private ServerGameState gameState = new ServerGameState();
	private List<Fallen> fallen = new ArrayList<>();

	public BattleServerProtocol() {
		super();
		
		gameState.setFallen(fallen);
		gameState.setPlayers(players);
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

		} else if (msg.startsWith(ClientProtocol.PREFIX_EVENT)) {

			String command = msg.split(" ")[1];

			if (ClientProtocol.EVENT_RESSURRECT.equals(command)) {
				for (ServerPlayer player : players.values()) {
					player.ressurrect();
				}
				
				sendTCPtoAll(ClientProtocol.PREFIX_EVENT + " " + command);
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
			player.update(gameState);
			message += player.asText() + " ";
		}

		message += PREFIX_FALLEN + " ";
		for (Fallen f : fallen) {
			message += f.getType().prefix + " ";
			message += f.asText() + " ";
		}

		sendTCPtoAll(message);
	}

	public void update(long now) {
		if (!roomReady) {
			return;
		}

		gameState.setNow(now);
		updatePlayers(now);
		updateFallen(now);

		if (now > lastCreation + delay) {
			lastCreation = now;
			createFallen();
		}
	}

	private void createFallen() {
		final Map<FallenType, Integer> probabilities = new HashMap<>();
		probabilities.put(FallenType.LEAF, 40); // 40%
		probabilities.put(FallenType.HIVE, 20); // 20%
		probabilities.put(FallenType.BOMB, 2);  // 2%
		probabilities.put(FallenType.GLUE, 1);  // 1%

		final int x = random.nextInt(WIDTH), y = -20;
		final int value = random.nextInt(100);
		int runningSum = 0;

		for (FallenType type : probabilities.keySet()) {
			runningSum += probabilities.get(type);
			if (value < runningSum) {
				fallen.add(type.create(x, y));
				break;
			}
		}
	}

	private void updateFallen(long now) {
		final List<Fallen> newFallen = new ArrayList<>();
		for (Fallen f : fallen) {
			f.update(now);

			if (f.getY() > HEIGHT) {
				f.markForRemoval();
			}

			checkCollisions(f);

			if (!f.isMarkedForRemoval()) {
				newFallen.add(f);
			}
		}
		fallen = newFallen;
	}

	private void checkCollisions(Fallen fallen) {
		players.values().stream().filter(player -> !player.isDead())
								 .forEach(fallen::colide);
	}
}
