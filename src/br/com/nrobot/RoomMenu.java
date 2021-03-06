package br.com.nrobot;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.network.client.Client;
import br.com.nrobot.network.client.model.ClientGameState;
import br.com.nrobot.network.server.Server;
import br.com.nrobot.network.server.model.NetworkRole;
import br.com.nrobot.ui.NRButton;

public class RoomMenu extends Application {

	private GameMode mode;

	private ImageLayer background;
	private ImageLayer logo;

	private NRButton createButton;
	private NRButton joinButton;

	public RoomMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		mode = (GameMode) session.get(MainMenu.PARAM_MODE);
		background = new ImageLayer("background.png");

		logo = new ImageLayer(0, 70, "logo.png");
		logo.centralizeX(background);

		createButton = new NRButton(232, 300, "create_room.png");

		joinButton = new NRButton(232, 400, "join_room.png");

		loading = 100;
	}

	@Override
	public void draw(Graphics g) {
		background.draw(g);

		logo.draw(g);

		createButton.draw(g);

		joinButton.draw(g);
	}

	public void updateMouse(PointerEvent event) {
		createButton.handleEvent(event);
		joinButton.handleEvent(event);

		if (event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {

			if (createButton.isOnMouse()) {
				session.put(MainMenu.PARAM_GAME, new ClientGameState());
				LoungeMenu game = new LoungeMenu(w, h);

				Server server = new Server(mode);
				server.start();

				Client client = Client.create(mode, game, "127.0.0.1");
				client.setRole(NetworkRole.SERVER);

				session.put(MainMenu.PARAM_CLIENT, client);				
				nextApplication = game;
			}

			if (joinButton.isOnMouse()) {
				Config config = (Config) session.get(MainMenu.PARAM_CONFIG);
				String ip = config.getServerIp();

				session.put(MainMenu.PARAM_GAME, new ClientGameState());
				LoungeMenu game = new LoungeMenu(w, h);
				Client client = Client.create(mode, game, ip);
				session.put(MainMenu.PARAM_CLIENT, client);
				nextApplication = game;
			}
		}
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		if (event.isKeyDown(KeyEvent.VK_ESC)) {
			nextApplication = new GameModeMenu(w, h);
		}
	}
}
