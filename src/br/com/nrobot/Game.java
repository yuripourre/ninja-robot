package br.com.nrobot;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.animation.script.OpacityAnimation;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.midnight.protocol.Protocol;
import br.com.nrobot.config.Config;
import br.com.nrobot.fallen.Bomb;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Glue;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.network.NetworkRole;
import br.com.nrobot.network.client.NRobotClientListener;
import br.com.nrobot.network.client.NinjaRobotClient;
import br.com.nrobot.network.server.NRobotServerProtocol;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.RobotNinja;
import br.com.nrobot.player.ServerPlayer;

public class Game extends Application implements OnAnimationFinishListener, UpdateIntervalListener, NRobotClientListener {

	private String me = "0";

	private NinjaRobotClient client;

	private ImageLayer ice;
	private ImageLayer skull;
	private ImageLayer background;
	private ImageLayer gameOver;

	private boolean gameIsOver = false;

	private Set<Fallen> pieces = new HashSet<Fallen>();
	private Map<String, Player> players = new LinkedHashMap<String, Player>();
	
	public boolean isDrawing = false;

	public Game(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		loadingInfo = "Carregando Imagens";

		background = new ImageLayer("background.png");

		loading = 40;

		gameOver = new ImageLayer("gameover.png");
		
		ice = new ImageLayer("state/ice.png");
		skull = new ImageLayer("state/skull.png");

		loading = 60;

		loadingInfo = "Carregando Jogador";

		loading = 100;

		updateAtFixedRate(50, this);
	}

	@Override
	public void timeUpdate(long now) {

		for(Player player : players.values()) {
			player.updatePlayer(now);
		}
	}
		
	@Override
	public void draw(Graphic g) {

		background.draw(g);

		g.setFont(g.getFont().deriveFont(28f));

		int i = 0;
		for(Player player : players.values()) {
			g.drawShadow(60+120*i, 60, "Pts: "+Integer.toString(player.getPoints()));
			g.drawShadow(60+120*i, 90, "Item: "+player.getItem());
			
			player.draw(g);
			g.drawShadow(player.getX(), player.getY()-20, player.getName());
			
			drawPlayerModifier(g, player);

			i++;
		}

		isDrawing = true;
		for(ImageLayer layer: pieces) {
			layer.simpleDraw(g);
			//layer.draw(g);
		}
		isDrawing = false;

		if (gameIsOver) {
			gameOver.draw(g);

			g.setOpacity(gameOver.getOpacity());

			g.drawStringShadowX(350, gameOverMessage);

			g.drawStringShadowX(420, "Clique para voltar ao menu inicial...");

			g.resetOpacity();
		}

	}

	private void drawPlayerModifier(Graphic g, Player player) {
		if(ServerPlayer.STATE_FREEZE.equals(player.getState())) {
			ice.simpleDraw(g, player.getX(), player.getY());
		} else if(ServerPlayer.STATE_DEAD.equals(player.getState())) {
			skull.simpleDraw(g, player.getX(), player.getY());
		}
		
		if(ServerPlayer.ITEM_RESSURRECT.equals(player.getItem())) {
			skull.simpleDraw(g, player.getX(), player.getY());
		}
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		
		if(NetworkRole.SERVER == client.getRole()) {
			if(event.isKeyDown(KeyEvent.VK_ENTER)) {
				client.getProtocol().sendRessurrect();
			}
		}
		
		if (client != null) {
			client.handleEvent(event);
		}
	}

	@Override
	public void updateMouse(PointerEvent event) {

		if(gameIsOver) {
			if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
				nextApplication = new MainMenu(w, h);
			}
		}

	}

	private String gameOverMessage = "Voce nao fez nenhum ponto.";

	@Override
	public void onAnimationFinish(long now) {

		gameIsOver = true;

		int points = players.get(me).getPoints(); 

		if(points > 0) {
			if(points == 1) {
				gameOverMessage = "Voce fez um ponto.";
			} else {
				gameOverMessage = "Voce fez "+points+" pontos.";
			}
		}

		OpacityAnimation gameOverAnimation = new OpacityAnimation(gameOver, 10000);
		gameOverAnimation.setInterval(0, 255);

		this.scene.addAnimation(gameOverAnimation);
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
		RobotNinja player = new RobotNinja(0, 540);
		player.setName(name);
		players.put(id, player);
	}

	public void setClient(NinjaRobotClient client) {
		this.client = client;
	}

	/**
	 * @see ServerPlayer.asText()
	 */
	@Override
	public void updatePositions(String positions) {
		//System.out.println(positions);
		String[] values = positions.split(" ");

		int attributes = 6;

		for (int i = 0;i < values.length; i += attributes) {
			String id = values[i];

			if(NRobotServerProtocol.PREFIX_NUT.equals(id)) {
				updateNuts(i+1, values);
				break;
			}

			Player player = players.get(id);
			if(player == null) {
				continue;
			}

			int x = Integer.parseInt(values[i+1]);
			int y = Integer.parseInt(values[i+2]);
			String state = values[i+3];
			String item = values[i+4];
			int points = Integer.parseInt(values[i+5]);

			player.setPosition(x, y);
			player.setState(state);
			player.setItem(item);
			player.setPoints(points);
		}
	}

	private void updateNuts(int index, String[] values) {
		Set<Fallen> updatedPieces = new HashSet<Fallen>();
		
		for(int i = index; i < values.length; i+=2) {
			if(NRobotServerProtocol.PREFIX_BOMB.equals(values[i])) {
				updateBombs(i+1, values, updatedPieces);
				pieces = updatedPieces;
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i+1]);
			updatedPieces.add(new Nut(x, y));
		}
	}

	private void updateBombs(int index, String[] values, Set<Fallen> updatedPieces) {
		for(int i = index; i < values.length; i+=2) {
			if(NRobotServerProtocol.PREFIX_GLUE.equals(values[i])) {
				updateGlues(i+1, values, updatedPieces);
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i+1]);
			updatedPieces.add(new Bomb(x, y));
		}
	}

	private void updateGlues(int index, String[] values, Set<Fallen> updatedPieces) {
		for(int i = index; i < values.length; i+=2) {
			if(NRobotServerProtocol.PREFIX_TONIC.equals(values[i])) {
				//updateTonics(i+1, values);
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i+1]);
			updatedPieces.add(new Glue(x, y));
		}
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
}
