package br.com.nrobot;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.network.client.NRobotClientListener;
import br.com.nrobot.network.client.NinjaRobotClient;
import br.com.nrobot.player.BlueNinja;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public class LoungeMenu extends Application implements NRobotClientListener {

	private String me = "0";

	private NinjaRobotClient client;
	private ImageLayer background;

	private Map<String, Player> players = new LinkedHashMap<String, Player>();
	
	public LoungeMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		loadingInfo = "Carregando Imagens";

		background = new ImageLayer("background.png");

		loading = 40;

		client = (NinjaRobotClient) session.get(MainMenu.PARAM_CLIENT);

		loading = 100;
	}
	
	@Override
	public void updateKeyboard(KeyEvent event) {
		
		if(event.isKeyDown(KeyEvent.VK_ENTER)) {
			client.getProtocol().sendStart();
		}
		
	}

	@Override
	public void exitClient(String id) {
		players.remove(id);
	}

	@Override
	public void joinedClient(String id, String name) {
		addPlayer(id, name);
	}

	@Override
	public void receiveMessage(String id, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(String[] ids) {
		me = ids[0];

		for(int i = 1; i < ids.length; i+=2) {
			addPlayer(ids[i], ids[i+1]);
		}
	}

	private void addPlayer(String id, String name) {
		//Player player = new RobotNinja(0, 540);
		Player player = new BlueNinja(0, 540);
		player.setName(name);
		players.put(id, player);
	}

	@Override
	public void updatePositions(String positions) {

	}

	@Override
	public void updateName(String id, String name) {
		Player player = players.get(id);
		player.setName(name);
	}

	@Override
	public void updateSprite(String id, String sprite) {
		Player player = players.get(id);
		//player.setSprite(sprite);
	}

	@Override
	public Config getConfig() {
		return (Config) session.get(MainMenu.PARAM_CONFIG);
	}

	@Override
	public void draw(Graphic g) {
		background.draw(g);
		
		int i = 1;
		for(Player player: players.values()) {
			
			String suffix = "";
			if(!ServerPlayer.STATE_READY.equals(player.getState())) {
				suffix = " (not ready)";
			}
			
			g.drawString("Player "+i+": "+player.getName()+suffix, 30, 60+30*i);
			
			i++;
		}
	}

	@Override
	public void startGame() {
		Game game = new Game(w, h);
		client.setListener(game);
		nextApplication = new Game(w,h);
	}
}
