package br.com.nrobot;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.config.Config;
import br.com.nrobot.network.client.NinjaRobotClient;
import br.com.nrobot.network.client.model.GameState;
import br.com.nrobot.network.server.NRobotServer;
import br.com.nrobot.network.server.model.NetworkRole;
import br.com.nrobot.ui.NRButton;

public class RoomMenu extends Application {

	private ImageLayer background;
	private ImageLayer logo;
	
	private NRButton createButton;
	private NRButton joinButton;
		
	public RoomMenu(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
				
		background = new ImageLayer("background.png");
		
		logo = new ImageLayer(0, 70, "logo.png");
		logo.centralizeX(background);
		
		createButton = new NRButton(232, 300, "create_room.png");
		
		joinButton = new NRButton(232, 400, "join_room.png");
		
		loading = 100;
	}
	
	@Override
	public void draw(Graphic g) {
		background.draw(g);
		
		logo.draw(g);
		
		createButton.draw(g);
		
		joinButton.draw(g);
	}
	
	public void updateMouse(PointerEvent event) {
		
		createButton.handleEvent(event);
		joinButton.handleEvent(event);
		
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
			
			if(createButton.isOnMouse()) {
				NRobotServer server = new NRobotServer(NinjaRobotClient.PORT);
				server.start();
				
				LoungeMenu game = new LoungeMenu(w, h);
				NinjaRobotClient client =  new NinjaRobotClient(game, "127.0.0.1");
				client.setRole(NetworkRole.SERVER);
				session.put(MainMenu.PARAM_CLIENT, client);
				session.put(MainMenu.PARAM_GAME, new GameState());
				nextApplication = game;
			}
			
			if(joinButton.isOnMouse()) {
								
				Config config = (Config)session.get(MainMenu.PARAM_CONFIG);
				String ip = config.getServerIp();
				
				LoungeMenu game = new LoungeMenu(w, h);
				NinjaRobotClient client =  new NinjaRobotClient(game, ip);
				session.put(MainMenu.PARAM_CLIENT, client);
				session.put(MainMenu.PARAM_GAME, new GameState());
				nextApplication = game;
			}
			
		}
	}

}
