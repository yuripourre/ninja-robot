package br.com.nrobot;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.animation.script.OpacityAnimation;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.nrobot.network.client.model.ClientGameState;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.ServerPlayer;

public class BattleModeGame extends Game implements OnAnimationFinishListener, UpdateIntervalListener {

	private ImageLayer background;
	private String gameOverMessage = "Voce nao fez nenhum ponto.";
	public boolean isDrawing = false;

	public BattleModeGame(int w, int h, ClientGameState state) {
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
		for (Player player : state.getPlayers().values()) {
			g.drawShadow(60 + 120 * i, 60, "Pts: " + Integer.toString(player.getPoints()));
			g.drawShadow(60 + 120 * i, 90, "Item: " + player.getItem());

			player.draw(g);
			g.drawShadow(player.getX(), player.getY() - 20, player.getName());

			drawPlayerModifier(g, player);

			i++;
		}

		isDrawing = true;
		for (ImageLayer layer : fallen) {
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
	public void onAnimationFinish(long now) {
		gameIsOver = true;

		int points = state.getPlayers().get(me).getPoints();

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
}
