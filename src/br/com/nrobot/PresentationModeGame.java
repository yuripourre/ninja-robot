package br.com.nrobot;

import java.awt.Color;

import br.com.etyllica.cinematics.parallax.ImageParallax;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.network.client.NRobotClientListener;
import br.com.nrobot.network.client.model.GameState;
import br.com.nrobot.network.server.NRobotBattleServerProtocol;
import br.com.nrobot.network.server.NRobotServerProtocol;
import br.com.nrobot.network.server.model.NetworkRole;
import br.com.nrobot.player.BlueNinja;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.RobotNinja;

public class PresentationModeGame extends Game implements UpdateIntervalListener, NRobotClientListener {

	private ImageParallax background;
	private ImageLayer oldBackground;
	private RobotNinja oldNinja;

	Graphics g;
	float titleSize = 42;
	float textSize = 28;
	int slide;
	int offset;
	
	int oldSlide = 3;
	
	int lineOne = 170;
	int lineTwo = 230;
	int lineThree = 290;

	public PresentationModeGame(int w, int h, GameState state) {
		super(w, h, state);
	}

	@Override
	public void load() {
		super.load();

		NRobotServerProtocol.WIDTH = 900000;
		
		loadingInfo = "Loading background";

		background = new ImageParallax("background/forest.png");
		oldBackground = new ImageLayer("background.png");
		oldNinja = new RobotNinja(w*oldSlide, 540);

		loading = 60;

		loadingInfo = "Carregando Jogador";

		loading = 100;

		updateAtFixedRate(50, this);
	}

	@Override
	public void timeUpdate(long now) {

		for(Player player : state.players.values()) {
			player.updatePlayer(now);
			offset = player.getX();
			background.setOffset(player.getX());
			oldBackground.setX(w*oldSlide-offset);
			oldNinja.setX(w*oldSlide-offset+w/2);
		}
	}

	@Override
	public void draw(Graphics g) {

		background.draw(g);

		drawSlides(g);

		for(Player player : state.players.values()) {
			player.draw(g, w/2);
		}
	}

	private void drawSlides(Graphics g) {
		this.g = g;
		g.setColor(Color.WHITE);
		
		oldBackground.draw(g);
		oldNinja.draw(g);
		
		slide = 0;
		title("Ninja Robot");
		text("Apresentado por Yuri Pourre", lineTwo);

		slide = 1;
		title("O que é?");
		text("Jogo Casual onde você controla um ninja", lineOne);
		text("É preciso pegar itens que caem de cima", lineTwo);
		text("Alguns itens somam pontos mas outros matam", lineThree);
		
		slide = 2;
		title("Surgimento");
		text("Foi desenvolvido do dia 9 ao dia 10", lineOne);
		text("Recebeu pequenas correções no dia 13 de Novembro 2014", lineTwo);
		text("Em Novembro de 2014", lineThree);
		
		slide = 3;
		title("Ideia Original");
		text("A ideia original era controlar um robô ninja", lineOne);
		text("Daí surgiu o nome", lineTwo);
		
		slide = 4;
		title("Multiplayer");
		text("No dia 18 de Maio de 2016", lineOne);
		text("André Pardal sugeriu uma melhoria", lineTwo);		
		text("E no dia seguinte já era possível desafiar outros jogadores", lineThree);
		
		slide = 5;
		title("Power UPs");
		text("No dia 20 de Maio de 2016", lineOne);
		text("Foram introduzidos itens especiais", lineTwo);		
		text("Era possível então congelar os adversários", lineThree);
		
		slide = 6;
		title("Novo Sprites");
		text("Durante o feriado do dia 21 de Maio de 2016", lineOne);
		text("As imagens foram alteradas", lineTwo);
		text("Foram corrigidos vários bugs", lineThree);
		
		slide = 7;
		title("Bibliotecas");
		text("Etyllica", lineOne);
		text("Midnight", lineTwo);
		
		slide = 8;
		title("Vamos ver funcionar!");
		
	}

	void title(String text) {
		int y = 80;
		g.setFont(g.getFont().deriveFont(titleSize));
		g.setColor(Color.BLACK);
		g.drawString(text, slide*w-offset+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(text, slide*w-offset, y);
	}

	void text(String text, int y) {
		g.setFont(g.getFont().deriveFont(textSize));
		g.setColor(Color.BLACK);
		g.drawString(text, slide*w-offset+1, y+1);
		g.setColor(Color.WHITE);
		g.drawString(text, slide*w-offset, y);
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

		if(event.isKeyDown(KeyEvent.VK_ESC)) {
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
		if(id.startsWith(NRobotBattleServerProtocol.PREFIX_BOT)) {
			RobotNinja player = new RobotNinja(0, 540);
			player.setName(name);
			state.players.put(id, player);	
		} else {
			BlueNinja player = new BlueNinja(0, 540);
			player.setName(name);
			state.players.put(id, player);	
		}

	}

	/**
	 * @see ServerPlayer.asText()
	 */
	@Override
	public void updatePositions(String positions) {
		if (!stateReady)
			return;

		//System.out.println(positions);
		String[] values = positions.split(" ");

		int attributes = 6;

		for (int i = 0;i < values.length; i += attributes) {
			String id = values[i];

			if(NRobotBattleServerProtocol.PREFIX_NUT.equals(id)) {
				break;
			}

			Player player = state.players.get(id);
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

	@Override
	public void updateName(String id, String name) {
		Player player = state.players.get(id);
		player.setName(name);
	}

	@Override
	public void updateSprite(String id, String sprite) {
		Player player = state.players.get(id);
		//player.setSprite(sprite);
	}

	@Override
	public void onAnimationFinish(long now) {
		// TODO Auto-generated method stub

	}

}
