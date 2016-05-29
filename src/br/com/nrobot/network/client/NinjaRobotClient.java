package br.com.nrobot.network.client;

import br.com.etyllica.core.event.KeyEvent;
import br.com.midnight.client.TCPClient;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.network.server.model.NetworkRole;

public class NinjaRobotClient extends TCPClient {

	public static final int BATTLE_PORT = 9967;
	public static final int STORY_PORT = 9968;
	private static final int CLIENT_DELAY = 100;

	private NRobotClientProtocol protocol;
	private NetworkRole role = NetworkRole.CLIENT;

	public static NinjaRobotClient create(GameMode mode, NRobotClientListener listener, String ip) {
		int port = mode == GameMode.BATTLE ? BATTLE_PORT : STORY_PORT;
		return new NinjaRobotClient(ip, port, listener);
	}

	public NinjaRobotClient(String ip, int port, NRobotClientListener listener) {
		super(ip, port, true);

		protocol = new NRobotClientProtocol(listener);
		addProtocol(protocol);
		start(CLIENT_DELAY);
	}

	public NRobotClientProtocol getProtocol() {
		return protocol;
	}

	public void setRole(NetworkRole role) {
		this.role = role;
	}

	public NetworkRole getRole() {
		return role;
	}

	public void handleEvent(KeyEvent event) {
		handleKey(event, KeyEvent.VK_D, NRobotClientProtocol.KEY_RIGHT);
		handleKey(event, KeyEvent.VK_RIGHT_ARROW, NRobotClientProtocol.KEY_RIGHT);
		handleKey(event, KeyEvent.VK_A, NRobotClientProtocol.KEY_LEFT);
		handleKey(event, KeyEvent.VK_LEFT_ARROW, NRobotClientProtocol.KEY_LEFT);
		handleKey(event, KeyEvent.VK_SPACE, NRobotClientProtocol.KEY_ITEM);
		handleKey(event, KeyEvent.VK_SHIFT_LEFT, NRobotClientProtocol.KEY_ATTACK);
		handleKey(event, KeyEvent.VK_SHIFT_RIGHT, NRobotClientProtocol.KEY_ATTACK);
		handleKey(event, KeyEvent.VK_CTRL_LEFT, NRobotClientProtocol.KEY_JUMP);
		handleKey(event, KeyEvent.VK_CTRL_RIGHT, NRobotClientProtocol.KEY_JUMP);
	}

	private void handleKey(KeyEvent event, int keyCode, String keyText) {
		if (event.isKeyDown(keyCode)) {
			protocol.sendPressKey(keyText);
		} else if (event.isKeyUp(keyCode)) {
			protocol.sendReleaseKey(keyText);
		}
	}

	public void setListener(NRobotClientListener listener) {
		protocol.setListener(listener);
	}
}
