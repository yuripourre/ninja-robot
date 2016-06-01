package br.com.nrobot;

import java.awt.Color;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.network.client.NRobotClientListener;
import br.com.nrobot.network.client.NRobotClientProtocol;
import br.com.nrobot.network.client.NinjaRobotClient;
import br.com.nrobot.network.client.model.GameState;
import br.com.nrobot.player.BlueNinja;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;
import br.com.nrobot.ui.SelectionButton;

public class LoungeMenu extends Application implements NRobotClientListener {

	private String me = "0";

	private NinjaRobotClient client;
	private ImageLayer background;

	private ImageLayer slot;
	private ImageLayer blueNinja;
	private ImageLayer darkNinja;
	
	private SelectionButton blueNinjaButton;
	private SelectionButton darkNinjaButton;

	GameState state;
	GameMode mode;

	public LoungeMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		mode = (GameMode) session.get(MainMenu.PARAM_MODE);
		state = (GameState) session.get(MainMenu.PARAM_GAME);
		client = (NinjaRobotClient) session.get(MainMenu.PARAM_CLIENT);
		
		loadingInfo = "Carregando Imagens";

		background = new ImageLayer("background.png");
		slot = new ImageLayer("ui/slot.png");
		blueNinja = new ImageLayer(0,0,64,64,"player/blue_ninja.png");
		darkNinja = new ImageLayer(0,0,64,64,"player/dark_ninja.png");

		loading = 40;

		blueNinjaButton = new SelectionButton(40, 370, "player/blue_ninja.png");
		darkNinjaButton = new SelectionButton(40+160, 370, "player/dark_ninja.png");
		
		loading = 100;
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		if(event.isKeyDown(KeyEvent.VK_ENTER)) {
			client.getProtocol().sendStart();
		}
	}
	
	@Override
	public void updateMouse(PointerEvent event) {
		if(blueNinjaButton.updateMouse(event)) {
			client.getProtocol().sendSprite(NRobotClientProtocol.SPRITE_BLUE);
		} else if(darkNinjaButton.updateMouse(event)) {
			client.getProtocol().sendSprite(NRobotClientProtocol.SPRITE_DARK);
		}
	}

	@Override
	public void exitClient(String id) {
		state.players.remove(id);
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
		state.players.put(id, player);
	}

	@Override
	public void updatePositions(String positions) {

	}

	@Override
	public void updateName(String id, String name) {
		Player player = state.players.get(id);
		player.setName(name);
	}

	@Override
	public void updateSprite(String id, String sprite) {
		Player player = state.players.get(id);
		if(!player.getSprite().equals(sprite)) {
			player.setSprite(sprite);
			player.changeSprite();
		}
	}
	

	@Override
	public void updateReady(String id) {
		Player player = state.players.get(id);
		player.setState(ServerPlayer.STATE_READY);
	}

	@Override
	public Config getConfig() {
		return (Config) session.get(MainMenu.PARAM_CONFIG);
	}

	@Override
	public void draw(Graphics g) {
		if (loading!=100) {
			return;
		}		
		background.draw(g);
		
		blueNinjaButton.draw(g);
		darkNinjaButton.draw(g);

		g.setColor(Color.BLACK);
		g.setFontSize(30f);

		int cx = 30, cy = 70, padding = 30, offset = 160;

		int i = 0;
		for(Player player: state.players.values()) {

			slot.simpleDraw(g, cx+160*i, cy);
			g.drawString(player.getName(), cx+padding+offset*i, cy+padding+6);

			if(NRobotClientProtocol.SPRITE_BLUE.equals(player.getSprite())) {
				blueNinja.simpleDraw(g, cx+padding+10+160*i, cy+padding+20);	
			} else if(NRobotClientProtocol.SPRITE_DARK.equals(player.getSprite())) {
				darkNinja.simpleDraw(g, cx+padding+10+160*i, cy+padding+20);
			}
			
			if(ServerPlayer.STATE_READY.equals(player.getState())) {
				String text = "READY";
				g.drawString(text, cx+padding+offset*i, cy+padding+120);
			}

			i++;
		}

		if(state.players.size() > 0) {
			Player player = state.players.get(me);
			if(!ServerPlayer.STATE_READY.equals(player.getState())) {
				g.drawString(this, "PRESS ENTER TO START");
			} else {
				g.drawString(this, "WAITING...");
			}
		}
	}

	@Override
	public void startGame() {
		
		Game game;
		
		if(GameMode.BATTLE == mode) {
			game = new BattleModeGame(w, h, state);
		} else {
			//game = new StoryModeGame(w, h, state);
			game = new PresentationModeGame(w, h, state);
		}
		
		client.setListener(game);		
		nextApplication = game;
	}
	
}
