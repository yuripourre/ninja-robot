package br.com.nrobot.network.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.midnight.model.Peer;
import br.com.midnight.protocol.common.StringServerProtocol;
import br.com.nrobot.fallen.Bomb;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Glue;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.server.ia.DumbAI;
import br.com.nrobot.network.server.ia.AI;
import br.com.nrobot.network.server.model.PlayerRole;
import br.com.nrobot.player.Bot;
import br.com.nrobot.player.ServerPlayer;

public class NRobotServerProtocol extends StringServerProtocol {

	public static final String PREFIX_BOMB = "B";
	public static final String PREFIX_NUT = "N";
	public static final String PREFIX_GLUE = "G";
	public static final String PREFIX_TONIC = "T";
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private int bots = 1;
	private AI ai = new DumbAI();
	
	private long lastCreation = 0;
	private final long delay = 400;
	private boolean roomReady = false;

	private List<Fallen> pieces = new ArrayList<Fallen>();
	private List<Fallen> bombs = new ArrayList<Fallen>();
	private List<Fallen> glues = new ArrayList<Fallen>();
	
	private Map<String, ServerPlayer> players = new LinkedHashMap<String, ServerPlayer>();

	public NRobotServerProtocol(String prefix) {
		super(prefix);
	}

	@Override
	public void addPeer(Peer peer) {
		super.addPeer(peer);
		ServerPlayer player = new ServerPlayer(peer.getSessionID());
		players.put(peer.getSessionID(), player);
		sendTCPtoAll(NRobotClientProtocol.PREFIX_JOIN+" "+NRobotHandshaker.sendPlayer(player));
	}
	
	private void addBot(String botId, String name) {
		Bot player = new Bot(botId);
		player.name = name;
		
		players.put(botId, player);
		sendTCPtoAll(NRobotClientProtocol.PREFIX_JOIN+" "+NRobotHandshaker.sendPlayer(player));
	}

	public void removePeer(Peer peer) {
		super.removePeer(peer);
		players.remove(peer.getSessionID());
		sendTCPtoAllExcept(peer, NRobotClientProtocol.PREFIX_EXIT+" "+peer.getSessionID());
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
				player.handleEvent(msg, players.values());
			}
		} else if (msg.startsWith(NRobotClientProtocol.PREFIX_CONFIG)) {

			updateConfig(peer, msg);
			
		} else if (msg.startsWith(NRobotClientProtocol.PREFIX_CHEAT_CODE)) {
			
			String command = msg.split(" ")[1];
			
			if(NRobotClientProtocol.CHEAT_RESSURRECT.equals(command)) {
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
		
		if(NRobotClientProtocol.CONFIG_NAME.equals(msg.split(" ")[1])) {
			String name = msg.split(" ")[2];
			player.name = name;
			
			sendTCPtoAll(NRobotClientProtocol.PREFIX_CONFIG+" "+peer.getSessionID()+" "+NRobotClientProtocol.CONFIG_NAME+" "+name);
		} else if(NRobotClientProtocol.CONFIG_SPRITE.equals(msg.split(" ")[1])) {
			String sprite = msg.split(" ")[2];
			player.sprite = sprite;
			
			sendTCPtoAll(NRobotClientProtocol.PREFIX_CONFIG+" "+peer.getSessionID()+" "+NRobotClientProtocol.CONFIG_SPRITE+" "+sprite);
		}
		
		if(NRobotClientProtocol.CONFIG_START.equals(msg.split(" ")[1])) {
			player.state = ServerPlayer.STATE_READY;
			sendTCPtoAll(NRobotClientProtocol.PREFIX_CONFIG+" "+peer.getSessionID()+" "+NRobotClientProtocol.CONFIG_READY+" :P");
			
			if(players.size() == 0) {
				return;
			}
			
			boolean start = true;
			
			for(ServerPlayer p : players.values()) {
				if(!ServerPlayer.STATE_READY.equals(p.state)) {
					start = false;
					break;
				}
			}
			if (start) {
				sendTCPtoAll(NRobotClientProtocol.PREFIX_CONFIG+" "+peer.getSessionID()+" "+NRobotClientProtocol.CONFIG_START+" :P");
				startGame();
			}
			
		}
	}
	
	private void startGame() {
		//Start bots
		for(int i = 0; i < bots; i++) {
			String id = "B"+i;
			addBot(id, "BOT "+id);
		}
			
		roomReady = true;
	}

	public void updatePlayers(long now) {
		String message = NRobotClientProtocol.PREFIX_POSITIONS+" ";

		for(ServerPlayer player: players.values()) {
			if(PlayerRole.BOT == player.role) {
				ai.act((Bot) player);
			}
			
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
		
		message += PREFIX_GLUE+" ";
		for(Fallen glue : glues) {
			message += glue.asText()+" ";
		}

		sendTCPtoAll(message);
	}

	private void createPiece() {
		//0~8 = bomb
		//8~25 = glue
		//16~100 = nut
		
		final int bombInterval = 8;
		final int glueInterval = 25;

		Random random = new Random();

		int x = random.nextInt(WIDTH);

		int type = random.nextInt(100);

		int speed = 3+random.nextInt(3);

		if(type < bombInterval) {
			bombs.add(new Bomb(x, -20));
		} else if(type < glueInterval) {
			glues.add(new Glue(x, -20));
		} else {
			Nut nut = new Nut(x, -20);
			nut.setSpeed(speed);

			pieces.add(nut);
		}
	}

	public void update(long now) {
		if(!roomReady) {
			return;
		}
				
		updatePlayers(now);
		
		updatePieceList(now, pieces);
		updatePieceList(now, bombs);
		updatePieceList(now, glues);
		
		if(now > lastCreation + delay) {
			lastCreation = now;
			createPiece();
		}
	}
	
	private void updatePieceList(long now, List<Fallen> list) {
		for(int i = list.size()-1; i >= 0; i--) {
			Fallen fallen = list.get(i);
			updatePiece(fallen, list);
			fallen.update(now);
		}
	}

	private void updatePiece(Fallen fallen, List<Fallen> list) {
		fallen.setOffsetY(fallen.getSpeed());

		if ((fallen.getY() > HEIGHT) ||!fallen.isVisible()) 
			list.remove(fallen);

		for (ServerPlayer player : players.values()) {
			if (!player.isDead()) {
				fallen.colide(player);
			}
		}
	}

	public Map<String, ServerPlayer>  getPlayers() {
		return players;
	}

}
