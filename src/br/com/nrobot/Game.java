package br.com.nrobot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
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
import br.com.nrobot.network.client.ActionClientListener;
import br.com.nrobot.network.client.NinjaRobotClient;
import br.com.nrobot.player.PinkNinja;
import br.com.nrobot.player.Player;
import br.com.nrobot.player.RobotNinja;

public class Game extends Application implements OnAnimationFinishListener, UpdateIntervalListener, ActionClientListener {

	private NinjaRobotClient client;
	
	private ImageLayer background;
	private ImageLayer gameOver;

	private RobotNinja robot;
	private PinkNinja pink;

	private long lastCreation = 0;
	private final long delay = 400;

	private boolean gameIsOver = false;

	private List<Fallen> pieces = new ArrayList<Fallen>();
	
	private Set<String> players = new LinkedHashSet<String>();

	public Game(int w, int h) {
		super(w, h);
	}
	
	public Game(int w, int h, NinjaRobotClient client) {
		super(w, h);
		this.client = client;
		this.client.setListener(this);
	}

	@Override
	public void load() {

		loadingInfo = "Carregando Imagens";

		background = new ImageLayer("background.png");

		loading = 40;

		gameOver = new ImageLayer("gameover.png");		

		loading = 60;

		loadingInfo = "Carregando Jogador";

		robot = new RobotNinja(0, 540);
		pink = new PinkNinja(0, 540);

		loading = 100;

		updateAtFixedRate(50, this);
	}

	private void createPiece() {

		final int bombPercentage = 8;

		Random random = new Random();

		int x = random.nextInt(w);

		int type = random.nextInt(100);
		
		int speed = 3+random.nextInt(3);

		if(type > bombPercentage) {
			
			Nut nut = new Nut(x, -20);
			nut.setSpeed(speed);
			
			pieces.add(nut);			
		} else {
			pieces.add(new Bomb(x, -20, this));
		}

	}

	@Override
	public void timeUpdate(long now) {

		if(robot.isDead())
			return;

		robot.update(now);

		for(int i = pieces.size()-1; i > 0; i--) {

			Fallen fallen = pieces.get(i);

			fallen.setOffsetY(fallen.getSpeed());

			if(fallen.getY()>h)
				pieces.remove(fallen);

			fallen.update(now);

			fallen.colide(robot);
		}

		if(now > lastCreation+delay) {
			lastCreation = now;
			createPiece();
		}

	}

	@Override
	public void draw(Graphic g) {

		background.draw(g);

		g.setFont(g.getFont().deriveFont(28f));

		g.drawStringShadowX(60, "Pontos: "+Integer.toString(robot.getPoints()));
		g.drawStringShadowX(90, "Pontos: "+Integer.toString(pink.getPoints()));

		robot.draw(g);
		pink.draw(g);

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

	public void updateKeyboard(KeyEvent event) {

		if(!robot.isDead()) {
			robot.handleEvent(event);
			
			if(client!=null) {
				robot.handleEvent(event, client);
			}
		}
	}

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

		int points = robot.getPoints(); 

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
		players.add(id);
	}

	@Override
	public void receiveMessage(String id, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(String[] ids) {
		for(String id: ids) {
			players.add(id);	
		}
	}

}
