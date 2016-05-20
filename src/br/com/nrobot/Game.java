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
import br.com.nrobot.fallen.Bomb;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.network.client.NRobotClientListener;
import br.com.nrobot.network.client.NinjaRobotClient;
import br.com.nrobot.network.server.NRobotServerProtocol;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.RobotNinja;

public class Game extends Application implements OnAnimationFinishListener, UpdateIntervalListener, NRobotClientListener {

	private String me = "0";

	private NinjaRobotClient client;

	private ImageLayer background;
	private ImageLayer gameOver;

	private boolean gameIsOver = false;

	private Set<Fallen> pieces = new HashSet<Fallen>();
	private Map<String, Player> players = new LinkedHashMap<String, Player>();

	public Game(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		loadingInfo = "Carregando Imagens";

		background = new ImageLayer("background.png");

		loading = 40;

		gameOver = new ImageLayer("gameover.png");		

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

		int i=0;
		for(Player player : players.values()) {
			g.drawShadow(60+100*i, 60, "Pts: "+Integer.toString(player.getPoints()));
			player.draw(g);

			i++;
		}

		for(ImageLayer layer: pieces) {
			layer.draw(g);
		}

		if(gameIsOver) {
			gameOver.draw(g);

			g.setOpacity(gameOver.getOpacity());

			g.drawStringShadowX(350, gameOverMessage);

			g.drawStringShadowX(420, "Clique para voltar ao menu inicial...");

			g.resetOpacity();
		}

	}

	@Override
	public void updateKeyboard(KeyEvent event) {
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
	public void joinedClient(String id) {
		addPlayer(id);
	}

	@Override
	public void receiveMessage(String id, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(String[] ids) {
		me = ids[0];

		for(int i = 1; i < ids.length; i++) {
			addPlayer(ids[i]);
		}
	}

	private void addPlayer(String id) {
		players.put(id, new RobotNinja(0, 540));
	}

	public void setClient(NinjaRobotClient client) {
		this.client = client;
	}

	@Override
	public void updatePositions(String positions) {
		//System.out.println(positions);
		String[] values = positions.split(" ");

		for (int i = 0;i < values.length; i += 5) {
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
			int points = Integer.parseInt(values[i+4]);

			player.setPosition(x, y);
			player.setState(state);
			player.setPoints(points);
		}
	}

	private void updateNuts(int index, String[] values) {
		pieces.clear();
				
		for(int i = index; i < values.length; i+=2) {
			if(NRobotServerProtocol.PREFIX_BOMB.equals(values[i])) {
				updateBoms(i+1, values);
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i+1]);
			pieces.add(new Nut(x, y));
		}
	}

	private void updateBoms(int index, String[] values) {
		for(int i = index; i < values.length; i+=2) {
			if(NRobotServerProtocol.PREFIX_BOMB.equals(values[i])) {
				updateBoms(i, values);
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i+1]);
			pieces.add(new Bomb(x, y));
		}
		
	}
}
