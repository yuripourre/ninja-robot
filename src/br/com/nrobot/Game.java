package br.com.nrobot;

import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.event.EventType;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.network.PlayerData;
import br.com.nrobot.network.client.Client;
import br.com.nrobot.network.client.ClientListener;
import br.com.nrobot.network.client.model.ClientGameState;
import br.com.nrobot.network.server.BattleServerProtocol;
import br.com.nrobot.network.server.model.NetworkRole;
import br.com.nrobot.network.server.model.ServerGameState;
import br.com.nrobot.player.BlueNinja;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.RobotNinja;

public abstract class Game extends Application implements OnAnimationFinishListener, UpdateIntervalListener, ClientListener {

	protected String me = "0";

	protected Client client;
	protected boolean stateReady = false;
	protected boolean gameIsOver = false;

	protected ImageLayer ice;
	protected ImageLayer skull;
	protected ImageLayer gameOver;

	protected ClientGameState state;
	protected List<Fallen> fallen = new ArrayList<>();

	public Game(int w, int h, ClientGameState state) {
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
		for (Player player : state.getPlayers().values()) {
			player.updatePlayer(now);
		}
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		if (NetworkRole.SERVER == client.getRole()) {
			if (event.isKeyDown(KeyEvent.VK_ENTER)) {
				client.ressurrect();
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
	public void updateMouse(PointerEvent event) {
		if (gameIsOver) {
			if (event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
				nextApplication = new MainMenu(w, h);
			}
		}
	}

	@Override
	public void exitClient(String id) {
		state.getPlayers().remove(id);
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
			state.getPlayers().put(id, player);
		} else {
			BlueNinja player = new BlueNinja(0, 540);
			player.setName(name);
			state.getPlayers().put(id, player);
		}
	}

	@Override
	public void updatePlayers(List<PlayerData> playersData) {
		if (!stateReady) {
			return;
		}
		for (PlayerData data : playersData) {
			Player player = state.getPlayers().get(data.id);
			if (player == null) {
				continue;
			}
			data.setTo(player);
		}
	}

	@Override
	public void updateFallen(List<Fallen> fallen) {
		if (!stateReady) {
			return;
		}
		this.fallen = fallen;
	}

	@Override
	public void updateName(String id, String name) {
		state.getPlayers().get(id).setName(name);
	}

	@Override
	public void updateSprite(String id, String sprite) {
		//state.players.get(id).setSprite(sprite);
	}
	
	@Override
	public void updateEvent(EventType event) {
		//state.players.get(id).setSprite(sprite);
		if (event == EventType.RESSURRECT) {
			for (Player player:state.getPlayers().values()) {
				player.ressurrect();	
			}
			
		}
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
