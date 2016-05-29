package br.com.nrobot;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.animation.script.OpacityAnimation;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.fallen.Bomb;
import br.com.nrobot.fallen.Fallen;
import br.com.nrobot.fallen.Glue;
import br.com.nrobot.fallen.Nut;
import br.com.nrobot.network.client.ClientListener;
import br.com.nrobot.network.client.model.GameState;
import br.com.nrobot.network.server.BattleServerProtocol;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class BattleModeGame extends Game implements OnAnimationFinishListener, UpdateIntervalListener, ClientListener {

	private ImageLayer background;

	private boolean gameIsOver = false;

	private Set<Fallen> pieces = new HashSet<Fallen>();
	private String gameOverMessage = "Voce nao fez nenhum ponto.";

	public boolean isDrawing = false;

	public BattleModeGame(int w, int h, GameState state) {
		super(w, h, state);
	}

	@Override
	public void load() {
		super.load();

		loadingInfo = "Loading background";

		background = new ImageLayer("background/forest.png");

		loading = 60;

		loadingInfo = "Carregando Jogador";

		loading = 100;

		updateAtFixedRate(50, this);
	}

	@Override
	public void draw(Graphics g) {
		background.draw(g);

		g.setFont(g.getFont().deriveFont(28f));

		int i = 0;
		for (Player player : state.players.values()) {
			g.drawShadow(60 + 120 * i, 60, "Pts: " + Integer.toString(player.getPoints()));
			g.drawShadow(60 + 120 * i, 90, "Item: " + player.getItem());

			player.draw(g);
			g.drawShadow(player.getX(), player.getY() - 20, player.getName());

			drawPlayerModifier(g, player);

			i++;
		}

		isDrawing = true;
		for (ImageLayer layer : pieces) {
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

	private void drawPlayerModifier(Graphics g, Player player) {
		if (ServerPlayer.STATE_FREEZE.equals(player.getState())) {
			ice.simpleDraw(g, player.getX(), player.getY());
		} else if (ServerPlayer.STATE_DEAD.equals(player.getState())) {
			skull.simpleDraw(g, player.getX(), player.getY());
		}

		if (ServerPlayer.ITEM_RESSURRECT.equals(player.getItem())) {
			skull.simpleDraw(g, player.getX(), player.getY());
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
	public void onAnimationFinish(long now) {
		gameIsOver = true;

		int points = state.players.get(me).getPoints();

		if (points > 0) {
			if (points == 1) {
				gameOverMessage = "Voce fez um ponto.";
			} else {
				gameOverMessage = "Voce fez " + points + " pontos.";
			}
		}

		OpacityAnimation gameOverAnimation = new OpacityAnimation(gameOver, 10000);
		gameOverAnimation.setInterval(0, 255);

		this.scene.addAnimation(gameOverAnimation);
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

		for (int i = 0; i < values.length; i += attributes) {
			String id = values[i];

			if (BattleServerProtocol.PREFIX_NUT.equals(id)) {
				updateNuts(i + 1, values);
				break;
			}

			Player player = state.players.get(id);
			if (player == null) {
				continue;
			}

			int x = Integer.parseInt(values[i + 1]);
			int y = Integer.parseInt(values[i + 2]);
			String state = values[i + 3];
			String item = values[i + 4];
			int points = Integer.parseInt(values[i + 5]);

			player.setPosition(x, y);
			player.setState(state);
			player.setItem(item);
			player.setPoints(points);
		}
	}

	private void updateNuts(int index, String[] values) {
		Set<Fallen> updatedPieces = new HashSet<Fallen>();

		for (int i = index; i < values.length; i += 2) {
			if (BattleServerProtocol.PREFIX_BOMB.equals(values[i])) {
				updateBombs(i + 1, values, updatedPieces);
				pieces = updatedPieces;
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i + 1]);
			updatedPieces.add(new Nut(x, y));
		}
	}

	private void updateBombs(int index, String[] values, Set<Fallen> updatedPieces) {
		for (int i = index; i < values.length; i += 2) {
			if (BattleServerProtocol.PREFIX_GLUE.equals(values[i])) {
				updateGlues(i + 1, values, updatedPieces);
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i + 1]);
			updatedPieces.add(new Bomb(x, y));
		}
	}

	private void updateGlues(int index, String[] values, Set<Fallen> updatedPieces) {
		for (int i = index; i < values.length; i += 2) {
			if (BattleServerProtocol.PREFIX_TONIC.equals(values[i])) {
				//updateTonics(i+1, values);
				return;
			}
			int x = Integer.parseInt(values[i]);
			int y = Integer.parseInt(values[i + 1]);
			updatedPieces.add(new Glue(x, y));
		}
	}
}
