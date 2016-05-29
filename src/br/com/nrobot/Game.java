package br.com.nrobot;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.network.client.ClientListener;
import br.com.nrobot.network.client.Client;
import br.com.nrobot.network.client.model.GameState;
import br.com.nrobot.network.server.BattleServerProtocol;
import br.com.nrobot.network.server.model.NetworkRole;
import br.com.nrobot.player.BlueNinja;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.RobotNinja;

public abstract class Game extends Application implements OnAnimationFinishListener, UpdateIntervalListener, ClientListener {

	protected String me = "0";

	protected Client client;
	protected boolean stateReady = false;

	protected ImageLayer ice;
	protected ImageLayer skull;
	protected ImageLayer gameOver;

	protected GameState state;

	public Game(int w, int h, GameState state) {
		super(w, h);
		this.state = state;
	}

	@Override
	public void load() {
		client = (Client) session.get(MainMenu.PARAM_CLIENT);
		stateReady = true;

		loadingInfo = "Carregando Imagens";
		gameOver = new ImageLayer("gameover.png");
		ice = new ImageLayer("state/ice.png");
		skull = new ImageLayer("state/skull.png");

		loading = 60;
	}

	@Override
	public void timeUpdate(long now) {
		for (Player player : state.players.values()) {
			player.updatePlayer(now);
		}
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		if (NetworkRole.SERVER == client.getRole()) {
			if (event.isKeyDown(KeyEvent.VK_ENTER)) {
				client.getProtocol().sendRessurrect();
			}
		}

		if (client != null) {
			client.handleEvent(event);
		}

		if (event.isKeyDown(KeyEvent.VK_ESC)) {
			nextApplication = new MainMenu(w, h);
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
	}

	@Override
	public void init(String[] ids) {
		me = ids[0];

		for (int i = 1; i < ids.length; i += 2) {
			addPlayer(ids[i], ids[i + 1]);
		}
	}

	private void addPlayer(String id, String name) {
		if (id.startsWith(BattleServerProtocol.PREFIX_BOT)) {
			RobotNinja player = new RobotNinja(0, 540);
			player.setName(name);
			state.players.put(id, player);
		} else {
			BlueNinja player = new BlueNinja(0, 540);
			player.setName(name);
			state.players.put(id, player);
		}
	}

	@Override
	public void updateName(String id, String name) {
		state.players.get(id).setName(name);
	}

	@Override
	public void updateSprite(String id, String sprite) {
		//state.players.get(id).setSprite(sprite);
	}

	@Override
	public Config getConfig() {
		return (Config) session.get(MainMenu.PARAM_CONFIG);
	}

	@Override
	public void startGame() {
	}

	@Override
	public void updateReady(String id) {
	}
}
